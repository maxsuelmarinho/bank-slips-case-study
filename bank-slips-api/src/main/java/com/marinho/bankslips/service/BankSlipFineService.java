package com.marinho.bankslips.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class BankSlipFineService implements IBankSlipFineService {

    @Autowired
    private Environment environment;

    @Override
    public BigDecimal calculate(final LocalDate dueDate, final LocalDate paymentDate, final BigDecimal totalInCents) {
        final LocalDate date = Optional.ofNullable(paymentDate)
                .orElse(LocalDate.now());

        long overdueDays = calculateOverdueDays(dueDate, date);

        final BigDecimal fee = findFee(overdueDays);

        return totalInCents.multiply(fee)
                .divide(BigDecimal.valueOf(100L))
                .setScale(0, BigDecimal.ROUND_HALF_UP);

    }

    private BigDecimal findFee(long overdueDays) {

        if (overdueDays > environment.getProperty("bankslipsapi.fine.overdue-in-days", Integer.class, 10)) {
            return new BigDecimal(environment.getProperty("bankslipsapi.fine.fee.max"));
        }

        return new BigDecimal(environment.getProperty("bankslipsapi.fine.fee.min"));
    }

    private long calculateOverdueDays(final LocalDate dueDate, final LocalDate date) {
        if (date.isAfter(dueDate)) {
            return DAYS.between(dueDate, date);
        }

        return 0L;
    }
}
