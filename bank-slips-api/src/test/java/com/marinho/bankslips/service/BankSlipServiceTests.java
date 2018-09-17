package com.marinho.bankslips.service;

import com.github.javafaker.Faker;
import com.marinho.bankslips.exception.BankSlipNotFoundException;
import com.marinho.bankslips.model.BankSlip;
import com.marinho.bankslips.model.BankSlipStatus;
import com.marinho.bankslips.repository.BankSlipRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankSlipServiceTests {

    @Autowired
    private IBankSlipService service;

    @MockBean
    private BankSlipRepository repository;

    private Faker faker = new Faker();

    @Test
    public void findAll() {
        final List<BankSlip> mockList = Arrays.asList(
                createBankSlip(),
                createBankSlip());

        when(repository.findAll()).thenReturn(mockList);

        final List<BankSlip> list = service.findAll();

        assertNotNull(list);
        assertEquals(mockList.size(), list.size());
    }

    @Test
    public void create() {

        final BankSlip bankSlipMock = createBankSlip();

        when(repository.save(Mockito.any(BankSlip.class))).then(it -> {
            bankSlipMock.setId(9L);
            return bankSlipMock;
        });

        final BankSlip bankSlip = service.create(bankSlipMock.getCustomer(), bankSlipMock.getDueDate(), bankSlipMock.getTotalInCents());

        assertNotNull(bankSlip);
        assertEquals(Long.valueOf(9L), bankSlip.getId());
        assertEquals(BankSlipStatus.PENDING, bankSlip.getStatus());
    }

    @Test
    public void findByUuid() {

        final BankSlip bankSlipMock = createBankSlip();
        final String bankSlipUuid = bankSlipMock.getUuid();

        when(repository.findByUuid(bankSlipUuid)).thenReturn(Optional.of(bankSlipMock));

        final BankSlip bankSlip = service.findByUuid(bankSlipUuid);

        assertNotNull(bankSlip);
        assertEquals(bankSlipUuid, bankSlip.getUuid());
    }

    @Test(expected = BankSlipNotFoundException.class)
    public void findByUuidShouldThrowsNotFoundException() {
        final String bankSlipId = "zxczxczxc";
        service.findByUuid(bankSlipId);
        fail("Should not arrived up here");
    }

    @Test
    public void pay() {

        final BankSlip bankSlipMock = createBankSlip();
        final String bankSlipUuid = bankSlipMock.getUuid();

        when(repository.findByUuid(bankSlipUuid)).thenReturn(Optional.of(bankSlipMock));

        service.pay(bankSlipMock.getUuid(), LocalDate.now());
        final BankSlip bankSlip = service.findByUuid(bankSlipUuid);
        assertEquals(BankSlipStatus.PAID, bankSlip.getStatus());
    }

    @Test(expected = BankSlipNotFoundException.class)
    public void payShouldThrowsNotFoundException() {
        final String bankSlipId = "zxczxczxc";
        service.pay(bankSlipId, LocalDate.now());
        fail("Should not arrived up here");
    }

    @Test
    public void cancel() {
        final BankSlip bankSlipMock = createBankSlip();
        final String bankSlipUuid = bankSlipMock.getUuid();

        when(repository.findByUuid(bankSlipUuid)).thenReturn(Optional.of(bankSlipMock));

        service.cancel(bankSlipUuid);
        final BankSlip bankSlip = service.findByUuid(bankSlipUuid);

        assertEquals(BankSlipStatus.CANCELED, bankSlip.getStatus());
    }

    @Test(expected = BankSlipNotFoundException.class)
    public void cancelShouldThrowsNotFoundException() {
        final String bankSlipId = "zxczxczxc";
        service.cancel(bankSlipId);
        fail("Should not arrived up here");
    }

    private BankSlip createBankSlip() {
        return BankSlip.builder()
                .uuid(UUID.randomUUID().toString())
                .totalInCents(new BigDecimal(faker.numerify("#########")))
                .dueDate(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)))
                .customer(faker.company().name())
                .status(BankSlipStatus.PENDING)
                .build();
    }

}
