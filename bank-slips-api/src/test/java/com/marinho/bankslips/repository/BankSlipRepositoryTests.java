package com.marinho.bankslips.repository;

import com.marinho.bankslips.model.BankSlip;
import com.marinho.bankslips.model.BankSlipStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class BankSlipRepositoryTests {

    @Autowired
    private BankSlipRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void checkCount() {
        assertEquals(2, repository.count());
    }

    @Test
    public void findOne() {
        Optional<BankSlip> bankSlip = repository.findById(1L);
        assertEquals("Customer A", bankSlip.get().getCustomer());
    }

    @Test
    public void exists() {
        assertTrue(repository.existsById(1L));
        assertFalse(repository.existsById(9L));
    }

    @Test
    public void delete() {
        repository.deleteById(1L);
        assertEquals(1, repository.count());
    }

    @Test
    public void deleteAll() {
        repository.deleteAll();
        assertEquals(0, repository.count());
    }

    @Test
    public void save() {
        BankSlip bankSlip = repository.findById(1L).get();
        bankSlip.setStatus(BankSlipStatus.PAID);
        repository.save(bankSlip);

        entityManager.flush();

        BankSlip bankSlipUpdated = repository.findById(1L).get();
        assertEquals(BankSlipStatus.PAID, bankSlipUpdated.getStatus());

    }

    @Test
    public void findByUuid() {
        BankSlip bankSlip = repository.findByUuid("asdasdasd").get();
        assertEquals("Customer A", bankSlip.getCustomer());
    }
}
