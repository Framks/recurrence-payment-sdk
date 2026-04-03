package com.recurrence.pagbank.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.domain.commons.Address;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.Holder;
import com.recurrence.contract.domain.commons.Phone;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.core.Converter;
import com.recurrence.pagbank.domain.commons.ResultSet;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.customer.PagBankListCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PagbankCustomerMapperTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = Converter.mapper();
    }

    @Test
    void testToPagBankCustomerWithBillingInfo() {
        var holder = new Holder("John Doe");
        var card = new Card(
            "4111111111111111",
            "2025",
            "12",
            "true",
            holder,
            "token-123",
            "VISA",
            "4111",
            "1111"
        );

        var pagBankCustomer = PagBankCustomer.builder()
            .id("cust-123")
            .referenceId("ref-123")
            .name("João Silva")
            .email("joao@example.com")
            .taxId("12345678901")
            .birthDate("1990-01-15")
            .phone(List.of(new com.recurrence.pagbank.domain.commons.Phone(1, "11", "BR", "987654321")))
            .address(new com.recurrence.pagbank.domain.commons.Address(
                "Rua das Flores",
                "123",
                "Apto 101",
                "Centro",
                "São Paulo",
                "SP",
                "BR",
                "01310-100"
            ))
            .billingInfo(List.of(new com.recurrence.pagbank.domain.commons.BillingInfo(
                "CREDIT_CARD",
                new com.recurrence.pagbank.domain.commons.Card(
                    "token-123",
                    "4111111111111111",
                    "true",
                    "VISA",
                    null,
                    null,
                    "12",
                    "2025",
                    new com.recurrence.pagbank.domain.commons.Card.Holder("John Doe", null, null)
                )
            )))
            .build();


        Customer result = PagbankCustomerMapper.to(pagBankCustomer);


        assertNotNull(result);
        assertEquals("cust-123", result.getId());
        assertEquals("ref-123", result.getReferenceId());
        assertEquals("João Silva", result.getName());
        assertEquals("joao@example.com", result.getEmail());
        assertEquals("12345678901", result.getTaxId());
        assertEquals("1990-01-15", result.getBirthDate());

        assertNotNull(result.getBillingInfo());
        assertEquals("CREDIT_CARD", result.getBillingInfo().type());
        assertNotNull(result.getBillingInfo().card());
        assertEquals("4111111111111111", result.getBillingInfo().card().number());
        assertEquals("token-123", result.getBillingInfo().card().token());
        assertEquals("VISA", result.getBillingInfo().card().brand());
        assertEquals("12", result.getBillingInfo().card().expMonth());
        assertEquals("2025", result.getBillingInfo().card().expYear());
    }

    @Test
    void testToPagBankCustomerWithoutBillingInfo() {
        List<com.recurrence.pagbank.domain.commons.Phone> phones = List.of(
            new com.recurrence.pagbank.domain.commons.Phone(1, "88", "BR", "999888777")
        );

        var pagBankCustomer = PagBankCustomer.builder()
            .id("cust-456")
            .referenceId("ref-456")
            .name("Maria Santos")
            .email("maria@example.com")
            .taxId("98765432101")
            .birthDate("1992-05-20")
            .phone(phones)
            .address(new com.recurrence.pagbank.domain.commons.Address(
                "Avenida Central",
                "456",
                null,
                "Bairro Alto",
                "Curitiba",
                "PR",
                "BR",
                "80010-000"
            ))
            .billingInfo(null)
            .build();


        Customer result = PagbankCustomerMapper.to(pagBankCustomer);


        assertNotNull(result);
        assertEquals("cust-456", result.getId());
        assertEquals("Maria Santos", result.getName());
        assertNull(result.getBillingInfo());
    }

    @Test
    void testToPagBankCustomerWithMultipleBillingInfosUsesFirst() {
        var billingInfo1 = new com.recurrence.pagbank.domain.commons.BillingInfo(
            "CREDIT_CARD",
            new com.recurrence.pagbank.domain.commons.Card("token-1", "4111111111111111", "true", "VISA", null, null, "12", "2025",
                new com.recurrence.pagbank.domain.commons.Card.Holder("John Doe", null, null))
        );
        var billingInfo2 = new com.recurrence.pagbank.domain.commons.BillingInfo(
            "CREDIT_CARD",
            new com.recurrence.pagbank.domain.commons.Card("token-2", "5555555555555555", "true", "MASTERCARD", null, null, "6", "2026",
                new com.recurrence.pagbank.domain.commons.Card.Holder("Jane Doe", null, null))
        );

        List<com.recurrence.pagbank.domain.commons.Phone> phones = List.of(
            new com.recurrence.pagbank.domain.commons.Phone(1, "88", "BR", "999888777")
        );

        var pagBankCustomer = PagBankCustomer.builder()
            .id("cust-multi")
            .referenceId("ref-multi")
            .name("Multi Cards")
            .email("multi@example.com")
            .taxId("11111111111")
            .birthDate("1995-07-07")
            .phone(phones)
            .address(new com.recurrence.pagbank.domain.commons.Address(
                "Avenida Central",
                "456",
                null,
                "Bairro Alto",
                "Curitiba",
                "PR",
                "BR",
                "80010-000"
            ))
            .billingInfo(List.of(billingInfo1, billingInfo2))
            .build();


        Customer result = PagbankCustomerMapper.to(pagBankCustomer);

        assertNotNull(result.getBillingInfo());
        assertEquals("CREDIT_CARD", result.getBillingInfo().type());
        assertEquals("token-1", result.getBillingInfo().card().token());
        assertEquals("4111111111111111", result.getBillingInfo().card().number());
    }

    @Test
    void testFromCustomerWithBillingInfo() {
        var holder = new Holder("Alice Smith");
        var card = new Card(
            "6011111111111111",
            "2026",
            "3",
            "false",
            holder,
            "token-alice",
            "DISCOVER",
            "6011",
            "1111"
        );
        var billingInfo = new BillingInfo("CREDIT_CARD", card);

        var customer = Customer.builder()
            .referenceId("ref-alice")
            .name("Alice Smith")
            .email("alice@example.com")
            .taxId("33333333333")
            .birthDate("1985-09-12")
            .phone(List.of(new Phone(1, "21", "BR", "999888777")))
            .billingInfo(billingInfo)
            .build();


        PagBankCustomer result = PagbankCustomerMapper.from(customer);


        assertNotNull(result);
        assertEquals("ref-alice", result.getReferenceId());
        assertEquals("Alice Smith", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals("33333333333", result.getTaxId());

        assertNotNull(result.getBillingInfo());
        assertEquals(1, result.getBillingInfo().size());

        var resultBilling = result.getBillingInfo().get(0);
        assertEquals("CREDIT_CARD", resultBilling.type());
        assertEquals("token-alice", resultBilling.card().token());
        assertEquals("6011111111111111", resultBilling.card().number());
        assertEquals("DISCOVER", resultBilling.card().brand());
    }

    @Test
    void testFromCustomerWithoutBillingInfo() {
        var customer = Customer.builder()
            .referenceId("ref-bob")
            .name("Bob Johnson")
            .email("bob@example.com")
            .taxId("44444444444")
            .birthDate("1987-12-25")
            .phone(List.of(new Phone(1, "31", "BR", "888777666")))
            .address(new Address(
                "Rua do Sol",
                "789",
                null,
                "Centro",
                "Belo Horizonte",
                "MG",
                "BR",
                "30110-000"
            ))
            .build();


        PagBankCustomer result = PagbankCustomerMapper.from(customer);


        assertNotNull(result);
        assertEquals("ref-bob", result.getReferenceId());
        assertNull(result.getBillingInfo());
    }

    @Test
    void testToPagResponseWithMultipleCustomers() {
        
        var customers = List.of(
            createPagBankCustomer("cust-1", "João", "joao@test.com"),
            createPagBankCustomer("cust-2", "Maria", "maria@test.com"),
            createPagBankCustomer("cust-3", "Pedro", "pedro@test.com")
        );

        var resultSet = new ResultSet(0, 10, 100);
        var pagBankListCustomer = new PagBankListCustomer();
        pagBankListCustomer.setResultSet(resultSet);
        pagBankListCustomer.setCustomers(customers);


        PageResponse<Customer> result = PagbankCustomerMapper.toPagResponse(pagBankListCustomer);


        assertNotNull(result);
        assertNotNull(result.getPage());
        assertEquals(0, result.getPage().offset());
        assertEquals(10, result.getPage().limit());
        assertEquals(100, result.getPage().total());

        assertNotNull(result.getData());
        assertEquals(3, result.getData().size());
        assertEquals("João", result.getData().get(0).getName());
        assertEquals("Maria", result.getData().get(1).getName());
        assertEquals("Pedro", result.getData().get(2).getName());
    }

    @Test
    void testToPagResponseWithEmptyList() {
        var resultSet = new ResultSet(0, 10, 0);
        var pagBankListCustomer = new PagBankListCustomer();
        pagBankListCustomer.setResultSet(resultSet);
        pagBankListCustomer.setCustomers(Collections.emptyList());


        PageResponse<Customer> result = PagbankCustomerMapper.toPagResponse(pagBankListCustomer);


        assertNotNull(result);
        assertEquals(0, result.getPage().total());
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void testDeserializeJsonWithBillingInfoArray() throws Exception {
        String jsonWithBillingArray = """
            {
              "id": "cust-json-1",
              "reference_id": "ref-json-1",
              "name": "Test User",
              "email": "test@example.com",
              "tax_id": "12345678901",
              "birth_date": "1990-01-15",
              "billing_info": [
                {
                  "type": "CREDIT_CARD",
                  "card": {
                    "token": "CARD_4C300F90-5CB",
                    "number": "4111111111111111",
                    "encrypted": "true",
                    "brand": "VISA",
                    "exp_month": "12",
                    "exp_year": "2025",
                    "holder": {
                      "name": "John Doe"
                    }
                  }
                }
              ]
            }
            """;


        PagBankCustomer pagBankCustomer = objectMapper.readValue(jsonWithBillingArray, PagBankCustomer.class);

        assertNotNull(pagBankCustomer);
        assertEquals("cust-json-1", pagBankCustomer.getId());
        assertEquals("ref-json-1", pagBankCustomer.getReferenceId());
        assertEquals("Test User", pagBankCustomer.getName());

        // Verificar billing info foi desserializado como lista
        assertNotNull(pagBankCustomer.getBillingInfo());
        assertEquals(1, pagBankCustomer.getBillingInfo().size());

        var billing = pagBankCustomer.getBillingInfo().get(0);
        assertEquals("CREDIT_CARD", billing.type());
        assertEquals("CARD_4C300F90-5CB", billing.card().token());
        assertEquals("4111111111111111", billing.card().number());
        assertEquals("VISA", billing.card().brand());
    }

    @Test
    void testDeserializeJsonWithMultipleBillingInfos() throws Exception {
        String jsonWithMultipleBilling = """
            {
              "id": "cust-multi-json",
              "reference_id": "ref-multi-json",
              "name": "Multi Billing",
              "email": "multi@example.com",
              "tax_id": "98765432101",
              "birth_date": "1992-05-20",
              "billing_info": [
                {
                  "type": "CREDIT_CARD",
                  "card": {
                    "token": "token-1",
                    "number": "4111111111111111",
                    "encrypted": "true",
                    "brand": "VISA",
                    "exp_month": "12",
                    "exp_year": "2025",
                    "holder": { "name": "John Doe" }
                  }
                },
                {
                  "type": "DEBIT_CARD",
                  "card": {
                    "token": "token-2",
                    "number": "5555555555555555",
                    "encrypted": "true",
                    "brand": "MASTERCARD",
                    "exp_month": "6",
                    "exp_year": "2026",
                    "holder": { "name": "Jane Doe" }
                  }
                }
              ]
            }
            """;


        PagBankCustomer pagBankCustomer = objectMapper.readValue(jsonWithMultipleBilling, PagBankCustomer.class);


        assertNotNull(pagBankCustomer);
        assertEquals(2, pagBankCustomer.getBillingInfo().size());
        assertEquals("CREDIT_CARD", pagBankCustomer.getBillingInfo().get(0).type());
        assertEquals("DEBIT_CARD", pagBankCustomer.getBillingInfo().get(1).type());
    }

    @Test
    void testDeserializeJsonWithEmptyBillingInfo() throws Exception {
        String jsonWithEmptyBilling = """
            {
              "id": "cust-empty",
              "reference_id": "ref-empty",
              "name": "No Billing",
              "email": "nobilling@example.com",
              "tax_id": "55555555555",
              "birth_date": "1988-03-10",
              "billing_info": []
            }
            """;


        PagBankCustomer pagBankCustomer = objectMapper.readValue(jsonWithEmptyBilling, PagBankCustomer.class);


        assertNotNull(pagBankCustomer);
        assertNotNull(pagBankCustomer.getBillingInfo());
        assertTrue(pagBankCustomer.getBillingInfo().isEmpty());
    }

    @Test
    void testDeserializeJsonWithNullBillingInfo() throws Exception {
        String jsonWithoutBilling = """
            {
              "id": "cust-null",
              "reference_id": "ref-null",
              "name": "Null Billing",
              "email": "null@example.com",
              "tax_id": "77777777777",
              "birth_date": "1995-08-08"
            }
            """;


        PagBankCustomer pagBankCustomer = objectMapper.readValue(jsonWithoutBilling, PagBankCustomer.class);


        assertNotNull(pagBankCustomer);
        assertNull(pagBankCustomer.getBillingInfo());
    }

    private PagBankCustomer createPagBankCustomer(String id, String name, String email) {
        return PagBankCustomer.builder()
            .id(id)
            .referenceId("ref-" + id)
            .name(name)
            .email(email)
            .taxId("12345678901")
            .birthDate("1990-01-15")
            .phone(List.of(new com.recurrence.pagbank.domain.commons.Phone(1, "31", "BR", "888777666")))
            .address(new com.recurrence.pagbank.domain.commons.Address(
                "Avenida Central",
                "456",
                null,
                "Bairro Alto",
                "Curitiba",
                "PR",
                "BR",
                "80010-000"
            ))
            .build();
    }
}

