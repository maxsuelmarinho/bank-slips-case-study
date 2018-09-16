package com.marinho.bankslips.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bank_slip")
public class BankSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String customer;

    @NotNull
    @Column(name = "total_in_cents")
    private BigDecimal totalInCents;

    @NotNull
    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BankSlipStatus status;
}
