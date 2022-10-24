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
        String username = faker.name().name();
        String expectedResourceString = "/user/" + username + "/account";

        String resourceString =
                SystemMenuResourceStringCreator
                        .getAccountResourceString(username);
        Assertions.assertEquals(expectedResourceString,  resourceString);
    }

    @Test
    public void getAccountResourceStringTest() {
        String username = faker.name().name();
        String accountNumber = faker.finance().iban();

        String expectedResourceString = "/user/" + username +
                "/account/" + accountNumber;

        System.out.println(expectedResourceString);

        String resourceString =
                SystemMenuResourceStringCreator.
                        getAccountInformationResourceString(username, accountNumber);
        Assertions.assertEquals(expectedResourceString, resourceString);
    }
}
