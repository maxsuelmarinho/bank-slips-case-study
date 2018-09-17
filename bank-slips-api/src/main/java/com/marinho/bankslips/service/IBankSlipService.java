package com.marinho.bankslips.service;


import com.marinho.bankslips.model.BankSlip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IBankSlipService {

    List<BankSlip> findAll();

    BankSlip findByUuid(final String id);

    void pay(final String id, LocalDate paymentDate);

    void cancel(String id);

    BankSlip create(String customer, LocalDate dueDate, BigDecimal totalInCents);
}
