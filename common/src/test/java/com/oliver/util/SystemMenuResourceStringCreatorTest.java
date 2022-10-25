package com.oliver.util;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SystemMenuResourceStringCreatorTest {
    private static Faker faker;

    @BeforeAll
    public static void setUp() {
        faker = new Faker();
    }

    @Test
    public void getCreateAccountResourceStringTest() {
        String expectedResourceString = "/account";

        String resourceString =
                SystemMenuResourceStringCreator
                        .getAccountResourceString();
        Assertions.assertEquals(expectedResourceString,  resourceString);
    }

    @Test
    public void getAccountResourceStringTest() {
        String accountNumber = faker.finance().iban();
        String expectedResourceString = "/account/" + accountNumber;

        String resourceString =
                SystemMenuResourceStringCreator.
                        getAccountInformationResourceString(accountNumber);
        Assertions.assertEquals(expectedResourceString, resourceString);
    }
}
