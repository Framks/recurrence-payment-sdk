package com.recurrence;

import com.fasterxml.jackson.databind.JsonNode;
import com.recurrence.contract.Recurrence;
import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.enums.Currency;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.enums.PaymentStatus;
import com.recurrence.contract.domain.enums.PlanIntervalType;
import com.recurrence.contract.domain.enums.PlanStatus;
import com.recurrence.contract.domain.enums.SubscriptionStatus;
import com.recurrence.contract.domain.commons.Phone;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.CustomerRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.domain.request.UpdateSubscriptionRequest;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.filters.PaymentFilter;
import com.recurrence.contract.filters.PlanFilter;
import com.recurrence.contract.filters.SubscriptionFilter;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.core.Converter;
import com.recurrence.exception.RecurrenceApiException;
import com.recurrence.exception.RecurrenceValidationException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.crypto.Cipher;

// Todo ajustar as chamadas para ter uma possibilidade de requestOptions para informar coisas como timeout, idempotencia, entre outros.
// Todo melhorar a estrutura de exceptions do sdk.
// ToDo pensar em uma estrategia de logs e como o dev pode configurar isso na sdk de forma geral.
// Todo os mappers deve ser capazes de fazer a transformação sem dar null pointer
// ToDo fazer os filtros das chamadas que permitem.


public class Library {

    private static final String TOKEN = "d229cfe4-76ad-4827-b013-083e911e85821ece519240a4b98aabf64fab3acc2e67eacf-a7ae-4614-928f-5904fd663c64";
    private static final String SANDBOX_URL = "https://sandbox.api.assinaturas.pagseguro.com";

    // Cartão aprovado no sandbox do PagBank. O 4539620659922097 é sempre recusado
    // ("Não autorizado pelo emissor") e serve para testar o fluxo de falha.
    private static final String TEST_CARD_NUMBER = "5555666677778884";
    private static final String TEST_CARD_EXP_MONTH = "12";
    private static final String TEST_CARD_EXP_YEAR = "2030";
    private static final String TEST_CARD_CVV = "123";
    private static final String TEST_CARD_HOLDER = "Joao Pereira Azul";

    // ---------------- Mercado Pago ----------------
    // No sandbox do Mercado Pago, vendedor (dono do token) e assinante precisam ser ambos usuários de teste.
    // Padrões: token do VENDEDOR de teste (TESTUSER224610420781505209) e e-mail de um COMPRADOR de teste.
    // Podem ser trocados por MP_ACCESS_TOKEN / MP_PAYER_EMAIL. Um comprador de teste recém-criado
    // é recusado ("User bad request") nos primeiros minutos; por isso o e-mail é fixo.
    private static final String MP_TOKEN = envOrDefault("MP_ACCESS_TOKEN",
            "APP_USR-8898118788019140-100610-b804125000d77e2d5a027c936bfbcad2-3743671356");
    private static final String MP_PAYER_EMAIL = envOrDefault("MP_PAYER_EMAIL",
            "test_user_1539022028588609023@testuser.com");
    // Tempo máximo esperando a primeira fatura, que o Mercado Pago gera de forma assíncrona.
    private static final Duration MP_INVOICE_WAIT = Duration.ofSeconds(90);
    private static final String MP_URL = "https://api.mercadopago.com";
    private static final String MP_BACK_URL = "https://www.exemplo.com/retorno";
    // Cartão de teste do Mercado Pago aceito em assinaturas (o 5031433215406351 é recusado com
    // Unsupported_credit_card_for_recurring_payment). O titular "APRO" aprova o pagamento ("OTHE" recusa).
    private static final String MP_TEST_CARD_NUMBER = "5480832801033311";
    private static final int MP_TEST_CARD_EXP_MONTH = 11;
    private static final int MP_TEST_CARD_EXP_YEAR = 2030;
    private static final String MP_TEST_CARD_HOLDER = "APRO";

    // TESTE DO CARD MERCADO PAGO Mastercard
    //5480 8328 0103 3311
    //123
    //11/30
    // pUBLIC Key mercado pago  = APP_USR-450b918b-1e7d-45c8-8f0a-dc87af29ab57
    // acess token mercado pago = APP_USR-8898118788019140-100610-b804125000d77e2d5a027c936bfbcad2-3743671356

    private static int success = 0;
    private static int failure = 0;

    /**
     * Executa, contra o sandbox, todas as operações do contrato.
     * Cada etapa imprime OK/FALHOU e o fluxo segue mesmo que uma etapa falhe;
     * etapas que dependem de um id ainda não obtido são puladas.
     *
     * <p>Executar: {@code ./gradlew runLibrary} (os dois provedores),
     * {@code ./gradlew runLibrary --args=pagbank} ou {@code --args=mercadopago}.</p>
     */
    public static void main(String[] args) {
        List<String> providers = args.length == 0 ? List.of("pagbank") : List.of(args);
        if (providers.contains("pagbank")) {
            runPagBank();
        }
        if (providers.contains("mercadopago")) {
            runMercadoPago();
        }
    }

    private static void runPagBank() {
        success = 0;
        failure = 0;
        System.out.println("\n################ PAGBANK ################");
        Recurrence pagbank = Recurrence.config(
                Provider.PAGBANK,
                Environment.SANDBOX,
                ProviderConfig.builder()
                        .token(TOKEN)
                        .timeout(Duration.ofSeconds(30))
                        .build()
        );

        // ---------------- planos ----------------
        Plan plan = step("plan.create", () -> pagbank.plan().create(
                createPlanRequest("Plano SDK " + RANDOM.nextInt(100000), 5000),
                RequestOptions.builder().idempotencyKey(UUID.randomUUID().toString()).build()));
        if (plan == null) {
            summary("PagBank");
            return;
        }
        String planId = plan.getId();

        step("plan.get", () -> pagbank.plan().get(planId));
        step("plan.list", () -> pagbank.plan().list(PageOptions.of(0, 5)));
        step("plan.list (status ACTIVE)", () -> pagbank.plan().list(
                PageOptions.of(0, 5),
                PlanFilter.builder().status(PlanStatus.ACTIVE).build()));
        step("plan.update (nome e valor)", () -> pagbank.plan().update(planId, UpdatePlanRequest.builder()
                .name(plan.getName() + " (editado)")
                .amount(5500, Currency.BRL)
                .build()));
        run("plan.deactivate", () -> pagbank.plan().deactivate(planId));
        step("plan.get (após deactivate)", () -> pagbank.plan().get(planId));
        run("plan.activate", () -> pagbank.plan().activate(planId));

        Plan otherPlan = step("plan.create (segundo plano, para troca)", () -> pagbank.plan().create(
                createPlanRequest("Plano SDK troca " + RANDOM.nextInt(100000), 7000)));

        // ---------------- assinaturas ----------------
        String encryptedCard = step("[apoio] chave pública + criptografia do cartão (fora do contrato)",
                Library::encryptTestCard);
        if (encryptedCard == null) {
            summary("PagBank");
            return;
        }

        String referenceId = "SDK-" + UUID.randomUUID();
        Subscription subscription = step("subscription.create", () -> pagbank.subscription().create(
                createSubscriptionRequest(planId, referenceId, encryptedCard),
                RequestOptions.builder().idempotencyKey(UUID.randomUUID().toString()).build()));

        if (subscription != null) {
            String subscriptionId = subscription.getId();

            step("subscription.get", () -> pagbank.subscription().get(subscriptionId));
            step("subscription.list", () -> pagbank.subscription().list(PageOptions.of(0, 5)));
            step("subscription.list (reference_id)", () -> pagbank.subscription().list(
                    PageOptions.of(0, 5),
                    SubscriptionFilter.builder().referenceId(referenceId).build()));
            step("subscription.list (status ACTIVE)", () -> pagbank.subscription().list(
                    PageOptions.of(0, 5),
                    SubscriptionFilter.builder().status(SubscriptionStatus.ACTIVE).build()));
            step("subscription.update (valor)", () -> pagbank.subscription().update(subscriptionId,
                    UpdateSubscriptionRequest.builder().amount(4500, Currency.BRL).build()));
            if (otherPlan != null) {
                step("subscription.update (troca de plano)", () -> pagbank.subscription().update(subscriptionId,
                        UpdateSubscriptionRequest.builder().planId(otherPlan.getId()).build()));
            }
            step("subscription.update (meio de pagamento)", () -> pagbank.subscription().update(subscriptionId,
                    UpdateSubscriptionRequest.builder().paymentMethod(cardBillingInfo(encryptedCard)).build()));

            // ---------------- faturas e pagamentos ----------------
            PageResponse<Invoice> invoices = step("invoice.listInvoiceBySubscription",
                    () -> pagbank.invoice().listInvoiceBySubscription(subscriptionId, PageOptions.of(0, 5)));
            Invoice invoice = first(invoices);
            if (invoice != null) {
                step("invoice.getInvoice", () -> pagbank.invoice().getInvoice(invoice.getId()));
                PageResponse<Payment> invoicePayments = step("invoice.listPayments (da fatura)",
                        () -> pagbank.invoice().listPayments(invoice.getId()));
                Payment payment = first(invoicePayments);
                if (payment != null) {
                    step("invoice.getPayment", () -> pagbank.invoice().getPayment(payment.getId()));
                } else {
                    skip("invoice.getPayment", "a fatura não tem pagamentos");
                }
            } else {
                skip("invoice.getInvoice / listPayments (da fatura) / getPayment", "a assinatura ainda não tem faturas");
            }

            run("subscription.suspend", () -> pagbank.subscription().suspend(subscriptionId));
            step("subscription.get (após suspend)", () -> pagbank.subscription().get(subscriptionId));
            run("subscription.activate", () -> pagbank.subscription().activate(subscriptionId));
            run("subscription.cancel", () -> pagbank.subscription().cancel(subscriptionId));
            step("subscription.get (após cancel)", () -> pagbank.subscription().get(subscriptionId));
        } else {
            skip("demais operações de assinatura e fatura", "subscription.create falhou");
        }

        step("invoice.listPayments (geral)", () -> pagbank.invoice().listPayments(PageOptions.of(0, 5)));
        // O PagBank exige as duas datas, com no máximo 90 dias entre elas.
        step("invoice.listPayments (geral, últimos 30 dias)", () -> pagbank.invoice().listPayments(
                PageOptions.of(0, 5),
                PaymentFilter.builder()
                        .createdFrom(OffsetDateTime.now().minusDays(30))
                        .createdUntil(OffsetDateTime.now())
                        .build()));
        step("invoice.listPayments (geral, status APPROVED)", () -> pagbank.invoice().listPayments(
                PageOptions.of(0, 5),
                PaymentFilter.builder().status(PaymentStatus.APPROVED).build()));

        summary("PagBank");
    }

    private static void runMercadoPago() {
        success = 0;
        failure = 0;
        System.out.println("\n################ MERCADO PAGO ################");
        Recurrence mercadoPago = Recurrence.config(
                Provider.MERCADO_PAGO,
                Environment.SANDBOX,
                ProviderConfig.builder()
                        .token(MP_TOKEN)
                        .setBackUrl(MP_BACK_URL)
                        .timeout(Duration.ofSeconds(30))
                        .build()
        );

        // ---------------- planos ----------------
        Plan plan = step("plan.create", () -> mercadoPago.plan().create(
                CreatePlanRequest.builder()
                        .name("Plano SDK " + RANDOM.nextInt(100000))
                        .amount(5050, Currency.BRL)
                        .interval(PlanIntervalType.MONTH, 1)
                        .billingCycles(12)
                        .trial(7)
                        .paymentMethods(List.of(PaymentMethodType.CREDIT_CARD))
                        .build(),
                RequestOptions.builder().idempotencyKey(UUID.randomUUID().toString()).build()));
        if (plan == null) {
            summary("Mercado Pago");
            return;
        }
        String planId = plan.getId();

        step("plan.get", () -> mercadoPago.plan().get(planId));
        step("plan.list", () -> mercadoPago.plan().list(PageOptions.of(0, 5)));
        step("plan.list (status ACTIVE)", () -> mercadoPago.plan().list(
                PageOptions.of(0, 5),
                PlanFilter.builder().status(PlanStatus.ACTIVE).build()));
        step("plan.update (nome e valor)", () -> mercadoPago.plan().update(planId, UpdatePlanRequest.builder()
                .name(plan.getName() + " (editado)")
                .amount(5500, Currency.BRL)
                .build()));
        run("plan.deactivate", () -> mercadoPago.plan().deactivate(planId));
        step("plan.get (após deactivate)", () -> mercadoPago.plan().get(planId));
        run("plan.activate", () -> mercadoPago.plan().activate(planId));
        step("plan.get (após activate)", () -> mercadoPago.plan().get(planId));

        // Plano sem período de teste: a primeira cobrança acontece logo, o que permite testar faturas.
        Plan chargePlan = step("plan.create (sem teste grátis, para a assinatura)", () -> mercadoPago.plan().create(
                createPlanRequest("Plano SDK cobrança " + RANDOM.nextInt(100000), 7000)));
        if (chargePlan == null) {
            summary("Mercado Pago");
            return;
        }

        // ---------------- assinaturas ----------------
        String cardToken = step("[apoio] card_token do cartão de teste (fora do contrato)", Library::mercadoPagoCardToken);
        if (cardToken == null) {
            summary("Mercado Pago");
            return;
        }

        String referenceId = "SDK-" + UUID.randomUUID();
        Subscription subscription = step("subscription.create", () -> mercadoPago.subscription().create(
                CreateSubscriptionRequest.builder()
                        .planId(chargePlan.getId())
                        .referenceId(referenceId)
                        .customer(CustomerRequest.builder().email(MP_PAYER_EMAIL).build())
                        .paymentMethod(cardBillingInfo(cardToken))
                        .build(),
                RequestOptions.builder().idempotencyKey(UUID.randomUUID().toString()).build()));

        if (subscription != null) {
            String subscriptionId = subscription.getId();

            step("subscription.get", () -> mercadoPago.subscription().get(subscriptionId));
            step("subscription.list", () -> mercadoPago.subscription().list(PageOptions.of(0, 5)));
            step("subscription.list (reference_id)", () -> mercadoPago.subscription().list(
                    PageOptions.of(0, 5),
                    SubscriptionFilter.builder().referenceId(referenceId).build()));
            step("subscription.list (status ACTIVE)", () -> mercadoPago.subscription().list(
                    PageOptions.of(0, 5),
                    SubscriptionFilter.builder().status(SubscriptionStatus.ACTIVE).build()));
            step("subscription.update (valor)", () -> mercadoPago.subscription().update(subscriptionId,
                    UpdateSubscriptionRequest.builder().amount(4500, Currency.BRL).build()));
            // O Mercado Pago ignora a troca de plano (responde 2xx e mantém o plano); o SDK recusa antes da chamada.
            expectRejection("subscription.update (troca de plano: recusada no Mercado Pago)",
                    () -> mercadoPago.subscription().update(subscriptionId,
                            UpdateSubscriptionRequest.builder().planId(planId).build()));
            String newCardToken = step("[apoio] novo card_token (fora do contrato)", Library::mercadoPagoCardToken);
            if (newCardToken != null) {
                step("subscription.update (meio de pagamento)", () -> mercadoPago.subscription().update(subscriptionId,
                        UpdateSubscriptionRequest.builder().paymentMethod(cardBillingInfo(newCardToken)).build()));
            }

            // ---------------- faturas e pagamentos ----------------
            PageResponse<Invoice> invoices = step("invoice.listInvoiceBySubscription (aguardando a primeira fatura)",
                    () -> waitForInvoices(mercadoPago, subscriptionId));
            Invoice invoice = first(invoices);
            if (invoice != null) {
                step("invoice.getInvoice", () -> mercadoPago.invoice().getInvoice(invoice.getId()));
                PageResponse<Payment> invoicePayments = step("invoice.listPayments (da fatura)",
                        () -> mercadoPago.invoice().listPayments(invoice.getId()));
                Payment payment = first(invoicePayments);
                if (payment != null) {
                    step("invoice.getPayment", () -> mercadoPago.invoice().getPayment(payment.getId()));
                } else {
                    skip("invoice.getPayment", "a fatura ainda não tem pagamento (o Mercado Pago cobra de forma assíncrona)");
                }
            } else {
                skip("invoice.getInvoice / listPayments (da fatura) / getPayment",
                        "a assinatura ainda não tem faturas (o Mercado Pago gera de forma assíncrona)");
            }

            run("subscription.suspend", () -> mercadoPago.subscription().suspend(subscriptionId));
            step("subscription.get (após suspend)", () -> mercadoPago.subscription().get(subscriptionId));
            run("subscription.activate", () -> mercadoPago.subscription().activate(subscriptionId));
            run("subscription.cancel", () -> mercadoPago.subscription().cancel(subscriptionId));
            step("subscription.get (após cancel)", () -> mercadoPago.subscription().get(subscriptionId));
        } else {
            skip("demais operações de assinatura e fatura", "subscription.create falhou");
        }

        step("subscription.list (geral)", () -> mercadoPago.subscription().list(PageOptions.of(0, 5)));
        step("invoice.listPayments (geral)", () -> mercadoPago.invoice().listPayments(PageOptions.of(0, 5)));
        step("invoice.listPayments (geral, últimos 30 dias)", () -> mercadoPago.invoice().listPayments(
                PageOptions.of(0, 5),
                PaymentFilter.builder()
                        .createdFrom(OffsetDateTime.now().minusDays(30))
                        .createdUntil(OffsetDateTime.now())
                        .build()));
        step("invoice.listPayments (geral, status APPROVED)", () -> mercadoPago.invoice().listPayments(
                PageOptions.of(0, 5),
                PaymentFilter.builder().status(PaymentStatus.APPROVED).build()));

        summary("Mercado Pago");
    }

    private static CreatePlanRequest createPlanRequest(String name, int amount) {
        return CreatePlanRequest.builder()
                .name(name)
                .amount(amount, Currency.BRL)
                .interval(PlanIntervalType.MONTH, 1)
                .paymentMethods(List.of(PaymentMethodType.CREDIT_CARD))
                .build();
    }

    private static CreateSubscriptionRequest createSubscriptionRequest(String planId, String referenceId, String encryptedCard) {
        return CreateSubscriptionRequest.builder()
                .planId(planId)
                .referenceId(referenceId)
                .customer(CustomerRequest.builder()
                        .name("Fulano de Tal dos Azois Pereira")
                        .email("assinante" + RANDOM.nextInt(100000) + "@exemplo.com")
                        .taxId(generateCPF())
                        .phone(Phone.builder().country("55").area("11").number("999999999").build())
                        .build())
                .paymentMethod(cardBillingInfo(encryptedCard))
                .build();
    }

    private static BillingInfo cardBillingInfo(String encryptedCard) {
        return BillingInfo.builder()
                .type(PaymentMethodType.CREDIT_CARD)
                .card(Card.builder()
                        .token(encryptedCard)
                        .securityCode(TEST_CARD_CVV)
                        .build())
                .build();
    }

    // ---------------- apoio: criptografia do cartão (papel do SDK JS do PagBank) ----------------

    /**
     * Reproduz o {@code PagSeguro.encryptCard} do SDK JS: obtém a chave pública da
     * conta e cifra {@code numero;cvv;mes;ano;titular;timestamp} com RSA/PKCS#1.
     * Uso exclusivo para teste no sandbox; em produção o cartão é criptografado no frontend.
     */
    private static String encryptTestCard() throws Exception {
        String publicKey = fetchPublicKey();
        String payload = String.join(";",
                TEST_CARD_NUMBER, TEST_CARD_CVV, TEST_CARD_EXP_MONTH, TEST_CARD_EXP_YEAR,
                TEST_CARD_HOLDER, String.valueOf(System.currentTimeMillis()));

        PublicKey key = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
    }

    /** Consulta a chave pública da conta; se ainda não existir, gera uma. */
    private static String fetchPublicKey() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(publicKeyRequest("GET"), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) {
            response = client.send(publicKeyRequest("PUT"), HttpResponse.BodyHandlers.ofString());
        }
        if (response.statusCode() / 100 != 2) {
            throw new IllegalStateException("public-keys HTTP " + response.statusCode() + ": " + response.body());
        }
        JsonNode body = Converter.mapper().readTree(response.body());
        return body.path("public_key").asText();
    }

    private static HttpRequest publicKeyRequest(String method) {
        return HttpRequest.newBuilder(URI.create(SANDBOX_URL + "/public-keys"))
                .header("Authorization", "Bearer " + TOKEN)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(30))
                .build();
    }

    // ---------------- apoio: Mercado Pago (papel do frontend / painel de testes) ----------------

    /**
     * Consulta as faturas da assinatura até a primeira aparecer já com id (o Mercado Pago
     * atribui o id alguns segundos após a cobrança) ou estourar {@link #MP_INVOICE_WAIT}.
     */
    private static PageResponse<Invoice> waitForInvoices(Recurrence mercadoPago, String subscriptionId) throws Exception {
        long deadline = System.currentTimeMillis() + MP_INVOICE_WAIT.toMillis();
        PageResponse<Invoice> invoices = mercadoPago.invoice().listInvoiceBySubscription(subscriptionId, PageOptions.of(0, 5));
        while ((first(invoices) == null || first(invoices).getId() == null) && System.currentTimeMillis() < deadline) {
            Thread.sleep(10_000);
            invoices = mercadoPago.invoice().listInvoiceBySubscription(subscriptionId, PageOptions.of(0, 5));
        }
        return invoices;
    }

    /** Tokeniza o cartão de teste, como o SDK JS do Mercado Pago faria no frontend. */
    private static String mercadoPagoCardToken() throws Exception {
        String body = "{\"card_number\":\"" + MP_TEST_CARD_NUMBER + "\","
                + "\"expiration_month\":" + MP_TEST_CARD_EXP_MONTH + ","
                + "\"expiration_year\":" + MP_TEST_CARD_EXP_YEAR + ","
                + "\"security_code\":\"" + TEST_CARD_CVV + "\","
                + "\"cardholder\":{\"name\":\"" + MP_TEST_CARD_HOLDER + "\","
                + "\"identification\":{\"type\":\"CPF\",\"number\":\"12345678909\"}}}";
        return mercadoPagoPost("/v1/card_tokens", body).path("id").asText();
    }

    private static JsonNode mercadoPagoPost(String path, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(MP_URL + path))
                .header("Authorization", "Bearer " + MP_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) {
            throw new IllegalStateException(path + " HTTP " + response.statusCode() + ": " + response.body());
        }
        return Converter.mapper().readTree(response.body());
    }

    private static String envOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    // ---------------- apoio: execução das etapas ----------------

    @FunctionalInterface
    private interface Call<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    private interface VoidCall {
        void run() throws Exception;
    }

    private static <T> T step(String name, Call<T> call) {
        System.out.println("\n========== " + name);
        try {
            T result = call.get();
            success++;
            System.out.println("OK");
            ObjectPrinter.print(result);
            return result;
        } catch (RecurrenceApiException e) {
            failure++;
            System.out.println("FALHOU: HTTP " + e.getStatusCode() + " " + e.getCategory());
            System.out.println("  erros: " + e.getErrors());
            System.out.println("  corpo: " + e.getRawBody());
        } catch (Exception e) {
            failure++;
            System.out.println("FALHOU: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return null;
    }

    private static void run(String name, VoidCall call) {
        step(name, () -> {
            call.run();
            return "(sem corpo)";
        });
    }

    /** Etapa que deve ser recusada pelo SDK antes da chamada (conta como OK se for). */
    private static void expectRejection(String name, Call<?> call) {
        System.out.println("\n========== " + name);
        try {
            call.get();
            failure++;
            System.out.println("FALHOU: era esperada uma recusa, mas a chamada foi aceita");
        } catch (RecurrenceValidationException e) {
            success++;
            System.out.println("OK (recusado como esperado): " + e.getMessage());
        } catch (Exception e) {
            failure++;
            System.out.println("FALHOU: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void skip(String name, String reason) {
        System.out.println("\n========== " + name);
        System.out.println("PULADO: " + reason);
    }

    private static <T> T first(PageResponse<T> page) {
        if (page == null || page.getData() == null || page.getData().isEmpty()) return null;
        return page.getData().get(0);
    }

    private static void summary(String provider) {
        System.out.println("\n========== RESUMO " + provider + ": " + success + " OK, " + failure + " falharam");
    }

    public class ObjectPrinter {

        // 🎨 CORES
        private static final String RESET = "\u001B[0m";
        private static final String FIELD = "\u001B[33m";   // amarelo
        private static final String VALUE = "\u001B[32m";   // verde
        private static final String TYPE = "\u001B[36m";    // ciano

        public static void print(Object obj) {
            print(obj, 0, java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>()));
        }

        private static void print(Object obj, int indent) {
            print(obj, indent, java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>()));
        }

        private static void print(Object obj, int indent, java.util.Set<Object> visiting) {
            if (obj == null) {
                println(indent, color("null", VALUE));
                return;
            }

            Class<?> clazz = obj.getClass();

            // =========================
            // TIPOS SIMPLES
            // =========================
            if (isPrimitiveLike(obj)) {
                println(indent, color(obj.toString(), VALUE));
                return;
            }

            if (visiting.contains(obj)) {
                println(indent, color("<circular reference: " + clazz.getSimpleName() + ">", VALUE));
                return;
            }

            visiting.add(obj);

            // =========================
            // COLLECTION
            // =========================
            if (obj instanceof Collection<?> col) {
                println(indent, "[");
                for (Object item : col) {
                    print(item, indent + 2, visiting);
                }
                println(indent, "]");
                visiting.remove(obj);
                return;
            }

            // =========================
            // MAP
            // =========================
            if (obj instanceof Map<?, ?> map) {
                println(indent, "{");
                map.forEach((k, v) -> {
                    println(indent + 2, color(k.toString(), FIELD) + ":");
                    print(v, indent + 4, visiting);
                });
                println(indent, "}");
                visiting.remove(obj);
                return;
            }

            // =========================
            // OBJETOS DO JDK (fallback)
            // =========================
            if (clazz.getPackageName().startsWith("java.")) {
                println(indent, color(obj.toString(), VALUE));
                visiting.remove(obj);
                return;
            }

            // =========================
            // OBJETO NORMAL
            // =========================
            println(indent, color(clazz.getSimpleName(), TYPE) + " {");

            for (Field field : clazz.getDeclaredFields()) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    println(indent + 2, color(field.getName(), FIELD) + ":");
                    print(value, indent + 4, visiting);

                } catch (Exception e) {
                    println(indent + 2, color(field.getName() + ": <erro>", FIELD));
                }
            }

            println(indent, "}");
            visiting.remove(obj);
        }

        // =========================
        // HELPERS
        // =========================

        private static boolean isPrimitiveLike(Object obj) {
            return obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj instanceof Enum<?> ||
                obj.getClass().isPrimitive();
        }

        private static void println(int indent, String text) {
            System.out.println(" ".repeat(indent) + text);
        }

        private static String color(String text, String color) {
            return color + text + RESET;
        }
    }

    private static final Random RANDOM = new Random();

    public static String generateCPF() {
        int[] cpf = new int[11];

        // Gera os 9 primeiros dígitos
        for (int i = 0; i < 9; i++) {
            cpf[i] = RANDOM.nextInt(10);
        }

        // Primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += cpf[i] * (10 - i);
        }

        int remainder = sum % 11;
        cpf[9] = (remainder < 2) ? 0 : 11 - remainder;

        // Segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += cpf[i] * (11 - i);
        }

        remainder = sum % 11;
        cpf[10] = (remainder < 2) ? 0 : 11 - remainder;

        // Monta a String
        StringBuilder builder = new StringBuilder();
        for (int digit : cpf) {
            builder.append(digit);
        }

        return builder.toString();
    }
}
