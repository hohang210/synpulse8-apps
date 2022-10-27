package com.oliver.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CurrencyUtilTest {
    @Test
    public void getCurrencyCodeByCountryTest() {
        String country = "China";
        String cnyCurrency =
                CurrencyUtil.getCurrencyCodeByCountry(country);
        Assertions.assertEquals("CNY", cnyCurrency);

        String invalidCurrency =
                CurrencyUtil.getCurrencyCodeByCountry("invalid-country");
        Assertions.assertNull(invalidCurrency);
    }
}
