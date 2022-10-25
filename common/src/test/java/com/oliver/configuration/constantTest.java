package com.oliver.configuration;

import com.oliver.CommonApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.oliver.configuration.constant.JWT_EXPIRATION_TIME;
import static com.oliver.configuration.constant.JWT_KEY;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class constantTest {
    @Test
    public void jwtExpirationTimeTest() {
        Assertions.assertEquals("60000", JWT_EXPIRATION_TIME);
    }

    @Test
    public void jwtKeyTest() {
        Assertions.assertEquals("ad6fbd4b-659d-4cbe-8b2d-9376f5232872", JWT_KEY);
    }
}
