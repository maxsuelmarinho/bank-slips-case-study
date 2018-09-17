package com.marinho.bankslips.service;

import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankSlipFineServiceTests {

    @Autowired
    private IBankSlipFineService service;

    private Faker faker = new Faker();

    @Test
    public void calculateUntilTenDays() {

        final BigDecimal fine = service.calculate(
                LocalDate.now().minusDays(faker.number().numberBetween(1, 10)),
                null,
                BigDecimal.valueOf(1000L));

        assertEquals(BigDecimal.valueOf(5L), fine);
    }

    @Test
    public void calculateMoreThanTenDays() {

        final BigDecimal fine = service.calculate(LocalDate.now().minusDays(faker.number().numberBetween(11, 30)),
                null,
                BigDecimal.valueOf(1000L));

        assertEquals(BigDecimal.valueOf(10L), fine);
    }
}