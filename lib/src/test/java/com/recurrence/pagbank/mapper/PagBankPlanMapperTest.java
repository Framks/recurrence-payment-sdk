package com.recurrence.pagbank.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recurrence.core.Converter;
import org.junit.jupiter.api.BeforeAll;

public class PagBankPlanMapperTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        objectMapper = Converter.mapper();
    }


}
