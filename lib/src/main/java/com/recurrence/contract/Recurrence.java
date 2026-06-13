package com.recurrence.contract;

import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.core.ProviderFactory;
import com.recurrence.exception.RecurrenceException;
import org.jetbrains.annotations.NotNull;

// Ideia é transformar esse cara meio que em uma fachada. com um ponto unico de acesso,
// o quem chamar diz o que quer a gente configura e retornar o recurrence
// antes de publicar devemos documentar e usar as anotações para deixar explicito o que precisa e o que não precisa.
final public class Recurrence {

    private final RecurrenceProvider provider;

    private Recurrence(final RecurrenceProvider provider) {
        this.provider = provider;
    }

    // Todo ver outras abordagens de receber as credenciais pois tem provedores que não usam só token simples
    // atualmente com o Provider config solucionamos. pois podemos apenas criar um build e passar a quelo que
    // é nescessario para cada provedor. no ato de criação é importante validar e lançar exception se não conseguir criar
    /**
     * Função responsavel por configurar um provedor.
     *
     * <p>Configura um provedor passando o enum provider, o enum do ambiente que estara executando e
     * as configurações basicas para esta integrando com ele. Retorno sera a classe Recurrence
     * com o provedor configurado</p>
     *
     * <pre>{@code
     * Recurrence config = Recurrence.config(
     *      Provider.PAGBANK,
     *      Enviroment.SANDBOX,
     *      ProviderConfig.builder()
     *          .token("your-token")
     *          .timeout(Duration.ofSeconds(15))  // Default 15s
     *          .defaultHeader("Accept", "application/json") // opcional
     *          .build()
     * );
     * }</pre>
     */
    public static Recurrence config(
            @NotNull final Provider provider,
            @NotNull final Environment environment,
            @NotNull final ProviderConfig config
            ) throws RecurrenceException {
        return new Recurrence(
                ProviderFactory.create(provider, environment, config)
        );
    }

    public PlanService plan() {
        return provider.planService();
    }
    public CouponService coupon() {
        return provider.couponService();
    }
    public CustomerService customer(){
        return provider.customerService();
    }
    public SubscriptionService subscription() {
        return provider.subscriptionService();
    }
    public InvoiceService invoice() {
        return provider.invoiceService();
    }
    public PreferencesService preferences(){
        return provider.preferencesService();
    }
}
