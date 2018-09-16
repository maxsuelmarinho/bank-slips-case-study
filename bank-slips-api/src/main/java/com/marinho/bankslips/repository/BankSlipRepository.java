package com.marinho.bankslips.repository;

import com.marinho.bankslips.model.BankSlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankSlipRepository extends JpaRepository<BankSlip, Long> {
    Optional<BankSlip> findByUuid(final String uuid);
}
