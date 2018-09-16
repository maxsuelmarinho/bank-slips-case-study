package com.marinho.bankslips.repository;

import com.marinho.bankslips.model.BankSlip;
import org.springframework.data.repository.CrudRepository;

public interface BankSlipRepository extends CrudRepository<BankSlip, Long> {
}
