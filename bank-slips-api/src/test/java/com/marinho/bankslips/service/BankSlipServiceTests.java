package com.marinho.bankslips.service;

import com.github.javafaker.Faker;
import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.exception.BankSlipNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankSlipServiceTests {

    @Autowired
    private BankSlipService service;

    private Faker faker = new Faker();

    @Before
    public void setUp() {

    }

    @Test
    public void findAllBankSlips() {
        Collection<BankSlipResponse> list = service.findAll();

        assertNotNull(list);
    }

    @Test
    public void createBankSlip() {
        BankSlipRequest request = BankSlipRequest.builder()
                .customer(faker.company().name())
                .dueDate("2018-09-12")
                .totalInCents(faker.numerify("#########"))
                .build();

        BankSlipResponse response = service.create(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("PENDING", response.getStatus());
        assertEquals(3, service.findAll().size());
    }

    @Test
    public void findBankSlipById() {

        final String bankSlipId = "asdasdasd";
        final BankSlipResponse response = service.findById(bankSlipId);
        assertNotNull(response);
        assertEquals(bankSlipId, response.getId());
    }

    @Test(expected = BankSlipNotFoundException.class)
    public void findBankSlipByIdShouldThrowsNotFoundException() {
        final String bankSlipId = "zxczxczxc";
        service.findById(bankSlipId);
        fail("Should not arrived up here");
    }

    @Test
    public void payBankSlip() {
        final String bankSlipId = "asdasdasd";
        service.pay(bankSlipId);
        final BankSlipResponse response = service.findById(bankSlipId);
        assertEquals("PAID", response.getStatus());
    }

    @Test(expected = BankSlipNotFoundException.class)
    public void payShouldThrowsNotFoundException() {
        final String bankSlipId = "zxczxczxc";
        service.pay(bankSlipId);
        fail("Should not arrived up here");
    }

    @Test
    public void cancelBankSlip() {
        final String bankSlipId = "qweqweqwe";
        service.cancel(bankSlipId);
        final BankSlipResponse response = service.findById(bankSlipId);
        assertEquals("CANCELED", response.getStatus());
    }

    @Test(expected = BankSlipNotFoundException.class)
    public void cancelShouldThrowsNotFoundException() {
        final String bankSlipId = "zxczxczxc";
        service.cancel(bankSlipId);
        fail("Should not arrived up here");
    }

}
