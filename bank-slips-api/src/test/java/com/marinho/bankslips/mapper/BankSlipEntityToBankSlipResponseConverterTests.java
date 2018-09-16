package com.marinho.bankslips.mapper;

import com.github.javafaker.Faker;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.model.BankSlip;
import com.marinho.bankslips.model.BankSlipStatus;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BankSlipEntityToBankSlipResponseConverterTests {

    private final Faker faker = new Faker();

    private final ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        modelMapper.addConverter(new BankSlipEntityToBankSlipResponseConverter());
    }

    @Test
    public void convertEntityToDto() {
        final BankSlip entity = BankSlip.builder()
                .id(10L)
                .uuid(UUID.randomUUID().toString())
                .status(BankSlipStatus.PENDING)
                .customer(faker.company().name())
                .dueDate(LocalDate.now())
                .paymentDate(LocalDateTime.now())
                .totalInCents(new BigDecimal(faker.numerify("##########")))
                .build();

        final BankSlipResponse dto =
            modelMapper.map(entity, BankSlipResponse.class);

        assertEquals(entity.getCustomer(), dto.getCustomer());
        assertEquals(entity.getDueDate(), dto.getDueDate());
        assertEquals(entity.getTotalInCents(), dto.getTotalInCents());
        assertEquals(entity.getUuid(), dto.getId());
        assertEquals(entity.getStatus().name(), dto.getStatus());
    }


}
