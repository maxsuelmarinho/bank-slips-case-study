package com.marinho.bankslips.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IBankSlipFineService {
    BigDecimal calculate(final LocalDate dueDate, final LocalDate paymentDate, final BigDecimal totalInCents);
}
