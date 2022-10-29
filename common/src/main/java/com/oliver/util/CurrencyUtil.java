package com.oliver.util;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CurrencyUtil {
    private static Map<String, String> currencies = getAvailableCurrencies();

    /**
     * Returns the given country's current code.
     *
     * @param country {String} A country name. (e.g. China/Canada)
     *
     * @return {String} Returns the given country's current code.
     */
    public static String getCurrencyCodeByCountry(String country) {
        return currencies.get(country);
    }

    /**
     * Collects all available currencies code from all
     * available locales information.
     *
     * @return {Map<String, String>} Returns map of currencies code.
     *                               Key will be the country name.
     *                               Value will be the currency code of the country.
     */
    private static Map<String, String> getAvailableCurrencies() {
        Map<String, String> currencies = new HashMap<>();

        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                String country = locale.getDisplayCountry(new Locale("English", "US"));
                String currency =
                        Currency.getInstance(locale).getCurrencyCode();
                currencies.put(country, currency);
            } catch (Exception ignored) {}
        }

        return currencies;
    }
}
