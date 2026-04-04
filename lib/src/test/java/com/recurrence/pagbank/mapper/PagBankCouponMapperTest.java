package com.recurrence.pagbank.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.core.Converter;
import com.recurrence.pagbank.domain.commons.ResultSet;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.domain.coupon.PagBankCouponDiscount;
import com.recurrence.pagbank.domain.coupon.PagBankCouponDuration;
import com.recurrence.pagbank.domain.coupon.PagBankListCoupon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PagBankCouponMapperTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        objectMapper = Converter.mapper();
    }

    @Test
    void testToPagBankContractCouponWithAllFields() {
        var coupon = Coupon.builder()
            .referenceId("ref-coupon-001")
            .name("Summer Discount 2026")
            .description("Get 20% off on summer products")
            .discount("PERCENTAGE", 20)
            .duration("TEMPORARY", 5)
            .redemptionLimit(100)
            .expiresAt(OffsetDateTime.parse("2026-08-31T23:59:59Z"))
            .build();

        PagBankCoupon result = PagBankCouponMapper.toPagBankContract(coupon);

        assertNotNull(result);
        assertEquals("ref-coupon-001", result.getReferenceId());
        assertEquals("Summer Discount 2026", result.getName());
        assertEquals("Get 20% off on summer products", result.getDescription());
        assertEquals(100, result.getRedemptionLimit());

        assertNotNull(result.getDiscount());
        assertEquals(20, result.getDiscount().value());
        assertEquals("PERCENTAGE", result.getDiscount().type());

        assertNotNull(result.getDuration());
        assertEquals("TEMPORARY", result.getDuration().type());
        assertEquals(5, result.getDuration().occurrences());

        assertNotNull(result.getExpiresAt());
    }

    @Test
    void testToPagBankContractCouponWithoutOptionalFields() {
        var coupon = Coupon.builder()
            .referenceId("ref-coupon-002")
            .name("Basic Coupon")
            .description("Basic discount coupon")
            .discount("FIXED", 10)
            .duration("PERMANENT", 1)
            .redemptionLimit(50)
            .build();

        PagBankCoupon result = PagBankCouponMapper.toPagBankContract(coupon);

        assertNotNull(result);
        assertEquals("ref-coupon-002", result.getReferenceId());
        assertEquals("Basic Coupon", result.getName());
        assertNull(result.getExpiresAt());
    }

    @Test
    void testToContractPagBankCouponWithAllFields() {
        var now = OffsetDateTime.now();
        var discount = new PagBankCouponDiscount(25, "PERCENTAGE");
        var duration = new PagBankCouponDuration("PERCENTAGE", 10);

        var pagBankCoupon = PagBankCoupon.builder()
            .id("coupon-123")
            .referenceId("ref-coupon-123")
            .name("Black Friday Deal")
            .description("Exclusive Black Friday discount")
            .discount(discount)
            .status("ACTIVE")
            .duration(duration)
            .redemptionLimit(500)
            .expiresAt(now)
            .inUse(true)
            .createAt(now)
            .updatedAt(now)
            .build();

        Coupon result = PagBankCouponMapper.toContract(pagBankCoupon);

        assertNotNull(result);
        assertEquals("coupon-123", result.getId());
        assertEquals("ref-coupon-123", result.getReferenceId());
        assertEquals("Black Friday Deal", result.getName());
        assertEquals("Exclusive Black Friday discount", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(500, result.getRedemptionLimit());
        assertTrue(result.getInUse());

        assertNotNull(result.getDiscount());
        assertEquals(25, result.getDiscount().value());
        assertEquals("PERCENTAGE", result.getDiscount().type());

        assertNotNull(result.getDuration());
        assertEquals("PERCENTAGE", result.getDuration().type());
        assertEquals(10, result.getDuration().occurrences());

        assertEquals(now, result.getExpiresAt());
        assertEquals(now, result.getCreatedAt());
        assertEquals(now, result.getUpdatedAt());
    }

    @Test
    void testToContractPagBankCouponWithMinimalFields() {
        var discount = new PagBankCouponDiscount(5, "FIXED");
        var duration = new PagBankCouponDuration("PERMANENT", 1);

        var pagBankCoupon = PagBankCoupon.builder()
            .id("coupon-456")
            .referenceId("ref-coupon-456")
            .name("Welcome Coupon")
            .description("Welcome new customers")
            .discount(discount)
            .duration(duration)
            .redemptionLimit(1000)
            .build();
        
        Coupon result = PagBankCouponMapper.toContract(pagBankCoupon);
        
        assertNotNull(result);
        assertEquals("coupon-456", result.getId());
        assertEquals("Welcome Coupon", result.getName());
        assertNull(result.getExpiresAt());
        assertNull(result.getInUse());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    void testToContractPageResponseWithMultipleCoupons() {
        var coupons = List.of(
            createPagBankCoupon("coupon-1", "Coupon 1", "ref-1"),
            createPagBankCoupon("coupon-2", "Coupon 2", "ref-2"),
            createPagBankCoupon("coupon-3", "Coupon 3", "ref-3")
        );

        var resultSet = new ResultSet(0, 10, 3);
        var pagBankListCoupon = new PagBankListCoupon();
        pagBankListCoupon.setResultSet(resultSet);
        pagBankListCoupon.setCoupons(coupons);

        PageResponse<Coupon> result = PagBankCouponMapper.toPageResponse(pagBankListCoupon);

        assertNotNull(result);
        assertNotNull(result.getPage());
        assertEquals(0, result.getPage().offset());
        assertEquals(10, result.getPage().limit());
        assertEquals(3, result.getPage().total());

        assertNotNull(result.getData());
        assertEquals(3, result.getData().size());
        assertEquals("Coupon 1", result.getData().get(0).getName());
        assertEquals("Coupon 2", result.getData().get(1).getName());
        assertEquals("Coupon 3", result.getData().get(2).getName());
    }

    @Test
    void testToContractPageResponseWithEmptyList() {
        
        var resultSet = new ResultSet(0, 10, 0);
        var pagBankListCoupon = new PagBankListCoupon();
        pagBankListCoupon.setResultSet(resultSet);
        pagBankListCoupon.setCoupons(Collections.emptyList());

        PageResponse<Coupon> result = PagBankCouponMapper.toPageResponse(pagBankListCoupon);

        assertNotNull(result);
        assertEquals(0, result.getPage().total());
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void testToContractPageResponseWithPagination() {
        var coupons = List.of(
            createPagBankCoupon("coupon-11", "Coupon 11", "ref-11"),
            createPagBankCoupon("coupon-12", "Coupon 12", "ref-12")
        );

        var resultSet = new ResultSet(10, 10, 25); // página 2 de 3
        var pagBankListCoupon = new PagBankListCoupon();
        pagBankListCoupon.setResultSet(resultSet);
        pagBankListCoupon.setCoupons(coupons);

        PageResponse<Coupon> result = PagBankCouponMapper.toPageResponse(pagBankListCoupon);

        assertEquals(10, result.getPage().offset());
        assertEquals(10, result.getPage().limit());
        assertEquals(25, result.getPage().total());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testDeserializeJsonWithCompleteCoupon() throws Exception {
        String json = """
            {
              "id": "coupon-json-1",
              "reference_id": "ref-json-1",
              "name": "JSON Coupon",
              "description": "Deserialized from JSON",
              "status": "ACTIVE",
              "in_use": true,
              "discount": {
                "value": 15,
                "type": "PERCENTAGE"
              },
              "duration": {
                "type": "TEMPORARY",
                "occurrences": 3
              },
              "redemption_limit": 200,
              "expires_at": "2026-12-31T23:59:59Z",
              "created_at": "2026-01-01T00:00:00Z",
              "updated_at": "2026-01-15T10:30:00Z"
            }
            """;

        PagBankCoupon result = objectMapper.readValue(json, PagBankCoupon.class);

        assertNotNull(result);
        assertEquals("coupon-json-1", result.getId());
        assertEquals("ref-json-1", result.getReferenceId());
        assertEquals("JSON Coupon", result.getName());
        assertEquals("Deserialized from JSON", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());
        assertTrue(result.getInUse());
        assertEquals(200, result.getRedemptionLimit());

        assertNotNull(result.getDiscount());
        assertEquals(15, result.getDiscount().value());
        assertEquals("PERCENTAGE", result.getDiscount().type());

        assertNotNull(result.getDuration());
        assertEquals("TEMPORARY", result.getDuration().type());
        assertEquals(3, result.getDuration().occurrences());

        assertNotNull(result.getExpiresAt());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testDeserializeJsonWithMinimalFields() throws Exception {
        String json = """
            {
              "reference_id": "ref-minimal",
              "name": "Minimal Coupon",
              "discount": {
                "value": 10,
                "type": "FIXED"
              },
              "duration": {
                "type": "PERMANENT",
                "occurrences": 1
              },
              "redemption_limit": 50
            }
            """;

        PagBankCoupon result = objectMapper.readValue(json, PagBankCoupon.class);

        assertNotNull(result);
        assertEquals("ref-minimal", result.getReferenceId());
        assertEquals("Minimal Coupon", result.getName());
        assertEquals(50, result.getRedemptionLimit());
        assertNull(result.getId());
        assertNull(result.getStatus());
        assertNull(result.getInUse());
        assertNull(result.getExpiresAt());
    }

    @Test
    void testDeserializeJsonWithNullOptionalFields() throws Exception {
        String json = """
            {
              "id": "coupon-null",
              "reference_id": "ref-null",
              "name": "Coupon with Nulls",
              "description": null,
              "status": null,
              "in_use": null,
              "discount": {
                "value": 5,
                "type": "PERCENTAGE"
              },
              "duration": {
                "type": "TEMPORARY",
                "occurrences": 2
              },
              "redemption_limit": 100,
              "expires_at": null,
              "created_at": null,
              "updated_at": null
            }
            """;

        PagBankCoupon result = objectMapper.readValue(json, PagBankCoupon.class);

        assertNotNull(result);
        assertEquals("coupon-null", result.getId());
        assertNull(result.getDescription());
        assertNull(result.getStatus());
        assertNull(result.getInUse());
        assertNull(result.getExpiresAt());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    void testDeserializeListOfCoupons() throws Exception {
        String json = """
            {
              "result_set": {
                "offset": 0,
                "limit": 10,
                "total": 2
              },
              "coupons": [
                {
                  "id": "coupon-list-1",
                  "reference_id": "ref-list-1",
                  "name": "First Coupon",
                  "discount": { "value": 10, "type": "FIXED" },
                  "duration": { "type": "PERMANENT", "occurrences": 1 },
                  "redemption_limit": 100,
                  "status": "ACTIVE"
                },
                {
                  "id": "coupon-list-2",
                  "reference_id": "ref-list-2",
                  "name": "Second Coupon",
                  "discount": { "value": 20, "type": "PERCENTAGE" },
                  "duration": { "type": "TEMPORARY", "occurrences": 5 },
                  "redemption_limit": 50,
                  "status": "INACTIVE"
                }
              ]
            }
            """;

        PagBankListCoupon result = objectMapper.readValue(json, PagBankListCoupon.class);
        
        assertNotNull(result);
        assertNotNull(result.getResultSet());
        assertEquals(0, result.getResultSet().offset());
        assertEquals(10, result.getResultSet().limit());
        assertEquals(2, result.getResultSet().total());

        assertNotNull(result.getCoupons());
        assertEquals(2, result.getCoupons().size());
        assertEquals("First Coupon", result.getCoupons().get(0).getName());
        assertEquals("Second Coupon", result.getCoupons().get(1).getName());
    }

    @Test
    void testRoundTripSerialization() throws Exception {
        var coupon = Coupon.builder()
            .referenceId("ref-roundtrip")
            .name("Round Trip Coupon")
            .description("Testing serialization roundtrip")
            .discount("PERCENTAGE", 30)
            .duration("TEMPORARY", 7)
            .redemptionLimit(250)
            .build();

        PagBankCoupon pagBankCoupon = PagBankCouponMapper.toPagBankContract(coupon);

        String json = objectMapper.writeValueAsString(pagBankCoupon);

        PagBankCoupon deserialized = objectMapper.readValue(json, PagBankCoupon.class);

        Coupon backToDomain = PagBankCouponMapper.toContract(deserialized);

        assertNotNull(backToDomain);
        assertEquals("ref-roundtrip", backToDomain.getReferenceId());
        assertEquals("Round Trip Coupon", backToDomain.getName());
        assertEquals("Testing serialization roundtrip", backToDomain.getDescription());
        assertEquals(30, backToDomain.getDiscount().value());
        assertEquals("PERCENTAGE", backToDomain.getDiscount().type());
        assertEquals("TEMPORARY", backToDomain.getDuration().type());
        assertEquals(7, backToDomain.getDuration().occurrences());
        assertEquals(250, backToDomain.getRedemptionLimit());
    }

    @Test
    void testToPagBankContractCouponWithFixedDiscount() {
        var coupon = Coupon.builder()
            .referenceId("ref-fixed")
            .name("Fixed Discount")
            .description("$10 off")
            .discount("FIXED", 1000) // em centavos
            .duration("PERMANENT", 1)
            .redemptionLimit(999)
            .build();

        PagBankCoupon result = PagBankCouponMapper.toPagBankContract(coupon);

        assertEquals("FIXED", result.getDiscount().type());
        assertEquals(1000, result.getDiscount().value());
    }

    @Test
    void testToPagBankContractCouponWithPercentageDiscount() {
        var coupon = Coupon.builder()
            .referenceId("ref-percentage")
            .name("Percentage Discount")
            .description("50% off")
            .discount("PERCENTAGE", 50)
            .duration("TEMPORARY", 2)
            .redemptionLimit(300)
            .build();
        
        PagBankCoupon result = PagBankCouponMapper.toPagBankContract(coupon);

        assertEquals("PERCENTAGE", result.getDiscount().type());
        assertEquals(50, result.getDiscount().value());
    }

    @Test
    void testToPagBankContractCouponWithPermanentDuration() {
        
        var coupon = Coupon.builder()
            .referenceId("ref-permanent")
            .name("Permanent Coupon")
            .description("Forever valid")
            .discount("FIXED", 500)
            .duration("PERMANENT", 1)
            .redemptionLimit(10000)
            .build();

        PagBankCoupon result = PagBankCouponMapper.toPagBankContract(coupon);

        assertEquals("PERMANENT", result.getDuration().type());
        assertEquals(1, result.getDuration().occurrences());
    }

    @Test
    void testToPagBankContractCouponWithTemporaryDuration() {
        var coupon = Coupon.builder()
            .referenceId("ref-temporary")
            .name("Temporary Coupon")
            .description("Expires soon")
            .discount("PERCENTAGE", 25)
            .duration("TEMPORARY", 12)
            .redemptionLimit(500)
            .expiresAt(OffsetDateTime.parse("2026-06-30T23:59:59Z"))
            .build();

        PagBankCoupon result = PagBankCouponMapper.toPagBankContract(coupon);

        assertEquals("TEMPORARY", result.getDuration().type());
        assertEquals(12, result.getDuration().occurrences());
        assertNotNull(result.getExpiresAt());
    }

    @Test
    void testToContractCouponWithActiveStatus() {
        var discount = new PagBankCouponDiscount(10, "FIXED");
        var duration = new PagBankCouponDuration("PERMANENT", 1);

        var pagBankCoupon = PagBankCoupon.builder()
            .id("coupon-active")
            .referenceId("ref-active")
            .name("Active Coupon")
            .discount(discount)
            .status("ACTIVE")
            .duration(duration)
            .redemptionLimit(100)
            .build();

        Coupon result = PagBankCouponMapper.toContract(pagBankCoupon);

        assertEquals("ACTIVE", result.getStatus());
    }

    @Test
    void testToContractCouponWithInactiveStatus() {
        var discount = new PagBankCouponDiscount(20, "PERCENTAGE");
        var duration = new PagBankCouponDuration("TEMPORARY", 5);

        var pagBankCoupon = PagBankCoupon.builder()
            .id("coupon-inactive")
            .referenceId("ref-inactive")
            .name("Inactive Coupon")
            .discount(discount)
            .status("INACTIVE")
            .duration(duration)
            .redemptionLimit(50)
            .build();

        Coupon result = PagBankCouponMapper.toContract(pagBankCoupon);

        assertEquals("INACTIVE", result.getStatus());
    }

    @Test
    void testToPagBankContractCouponWithLargeRedemptionLimit() {
        
        var coupon = Coupon.builder()
            .referenceId("ref-large")
            .name("Large Limit")
            .discount("PERCENTAGE", 1)
            .duration("PERMANENT", 1)
            .redemptionLimit(999999)
            .build();

        PagBankCoupon result = PagBankCouponMapper.toPagBankContract(coupon);

        assertEquals(999999, result.getRedemptionLimit());
    }

    @Test
    void testToContractPagBankCouponWithInUseTrue() {
        var discount = new PagBankCouponDiscount(5, "FIXED");
        var duration = new PagBankCouponDuration("PERMANENT", 1);

        var pagBankCoupon = PagBankCoupon.builder()
            .id("coupon-used")
            .referenceId("ref-used")
            .name("In Use Coupon")
            .discount(discount)
            .duration(duration)
            .redemptionLimit(100)
            .inUse(true)
            .build();

        Coupon result = PagBankCouponMapper.toContract(pagBankCoupon);

        assertTrue(result.getInUse());
    }

    @Test
    void testToContractPagBankCouponWithInUseFalse() {
        var discount = new PagBankCouponDiscount(10, "PERCENTAGE");
        var duration = new PagBankCouponDuration("TEMPORARY", 3);

        var pagBankCoupon = PagBankCoupon.builder()
            .id("coupon-unused")
            .referenceId("ref-unused")
            .name("Not In Use")
            .discount(discount)
            .duration(duration)
            .redemptionLimit(50)
            .inUse(false)
            .build();

        Coupon result = PagBankCouponMapper.toContract(pagBankCoupon);

        assertFalse(result.getInUse());
    }

    private PagBankCoupon createPagBankCoupon(String id, String name, String referenceId) {
        return PagBankCoupon.builder()
            .id(id)
            .referenceId(referenceId)
            .name(name)
            .description("Test coupon")
            .discount(new PagBankCouponDiscount(10, "PERCENTAGE"))
            .duration(new PagBankCouponDuration("PERMANENT", 1))
            .redemptionLimit(100)
            .status("ACTIVE")
            .build();
    }
}

