package com.marinho.bankslips.service;

import com.marinho.bankslips.exception.BankSlipNotFoundException;
import com.marinho.bankslips.model.BankSlip;
import com.marinho.bankslips.model.BankSlipStatus;
import com.marinho.bankslips.repository.BankSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BankSlipService implements IBankSlipService {

    @Autowired
    private BankSlipRepository repository;

    @Autowired
    private IBankSlipFineService fineService;

    public List<BankSlip> findAll() {
        return this.repository.findAll();
    }

    public BankSlip findByUuid(final String uuid) {
        final BankSlip bankSlip = this.repository.findByUuid(uuid)
                .orElseThrow(BankSlipNotFoundException::new);

        final BigDecimal fine = fineService.calculate(
                bankSlip.getDueDate(),
                bankSlip.getPaymentDate(),
                bankSlip.getTotalInCents());

        bankSlip.setFine(fine);

        return bankSlip;
    }

    public void pay(final String id, LocalDate paymentDate) {
        final BankSlip bankSlip = findByUuid(id);

        bankSlip.setStatus(BankSlipStatus.PAID);
        bankSlip.setPaymentDate(paymentDate);
        repository.save(bankSlip);
    }

    public void cancel(String id) {
        final BankSlip bankSlip = findByUuid(id);

        bankSlip.setStatus(BankSlipStatus.CANCELED);
        repository.save(bankSlip);
    }

    @Override
    public BankSlip create(String customer, LocalDate dueDate, BigDecimal totalInCents) {
        final BankSlip bankSlip = BankSlip.builder()
                .uuid(UUID.randomUUID().toString())
                .dueDate(dueDate)
                .totalInCents(totalInCents)
                .customer(customer)
                .status(BankSlipStatus.PENDING)
                .build();

        return this.repository.save(bankSlip);
    }
}
