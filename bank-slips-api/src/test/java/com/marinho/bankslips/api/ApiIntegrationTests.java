package com.marinho.bankslips.api;

import com.github.javafaker.Faker;
import com.marinho.bankslips.dto.BankSlipPaymentRequest;
import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.model.BankSlip;
import com.marinho.bankslips.model.BankSlipStatus;
import com.marinho.bankslips.repository.BankSlipRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private Faker faker = new Faker();

    @Autowired
    private BankSlipRepository repository;

    private BankSlip bankSlip;

    @Before
    public void setUp() {
        bankSlip = BankSlip.builder()
                .uuid(UUID.randomUUID().toString())
                .totalInCents(new BigDecimal(faker.numerify("#########")))
                .dueDate(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)))
                .customer(faker.company().name())
                .status(BankSlipStatus.PENDING)
                .build();

        repository.save(bankSlip);
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void createBankSlip() {

        BankSlipRequest request = createBankSlipRequest();

        final ResponseEntity<BankSlipResponse> responseEntity = restTemplate.postForEntity(
                "/bankslips", request, BankSlipResponse.class);

        final BankSlipResponse response = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(request.getCustomer(), response.getCustomer());
        assertEquals(request.getTotalInCents(), response.getTotalInCents());
        assertEquals(request.getDueDate(), response.getDueDate());
        assertEquals(BankSlipStatus.PENDING.name(), response.getStatus());
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenCustomerIsNotPresent() {

        BankSlipRequest request = createBankSlipRequest();
        request.setCustomer(null);

        final ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                "/bankslips", request, Object.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenDueDateIsNotPresent() {

        BankSlipRequest request = createBankSlipRequest();
        request.setDueDate(null);

        final ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                "/bankslips", request, Object.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenTotalInCentsIsNotPresent() {

        BankSlipRequest request = createBankSlipRequest();
        request.setTotalInCents(null);

        final ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                "/bankslips", request, Object.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenTotalInCentsIsNotPositiveValue() {

        BankSlipRequest request = createBankSlipRequest();
        request.setTotalInCents(new BigDecimal(-1));

        final ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                "/bankslips", request, Object.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    public void createShouldReturnBadRequestWhenNoBodyIsPresent() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<BankSlipRequest> entity = new HttpEntity<>(null, headers);

        final ResponseEntity<Object> responseEntity = restTemplate.exchange(
                "/bankslips", HttpMethod.POST, entity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void listBankSlips() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity(null, headers);

        final ResponseEntity<List<BankSlipResponse>> responseEntity = restTemplate.exchange(
                "/bankslips", HttpMethod.GET, entity, new ParameterizedTypeReference<List<BankSlipResponse>>() {
                });

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        long count = repository.count();
        assertEquals(count, responseEntity.getBody().size());

    }

    @Test
    public void retrieveBankSlip() throws Exception {
        final String bankSlipUuid = bankSlip.getUuid();
        Map<String, String> params = new HashMap<>();
        params.put("uuid", bankSlipUuid);

        final ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("/bankslips/{uuid}", String.class, params);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final String expected = "{id:\"" + bankSlip.getUuid() + "\"," +
                "due_date:\"" + bankSlip.getDueDate().toString() + "\"," +
                "total_in_cents:" + bankSlip.getTotalInCents() + "," +
                "customer:\"" + bankSlip.getCustomer() + "\"," +
                "status:\"PENDING\"}";

        JSONAssert.assertEquals(expected, responseEntity.getBody(), JSONCompareMode.STRICT);
    }

    @Test
    public void retrieveBankSlipShouldReturnNotFound() {
        final String bankSlipUuid = "werwerwer";
        Map<String, String> params = new HashMap<>();
        params.put("uuid", bankSlipUuid);

        final ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("/bankslips/{uuid}", String.class, params);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void payBankSlip() {
        BankSlipRequest request = createBankSlipRequest();
        final ResponseEntity<BankSlipResponse> responseEntity = restTemplate.postForEntity(
                "/bankslips", request, BankSlipResponse.class);
        final BankSlipResponse bankSlip = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        Map<String, String> params = new HashMap<>();
        params.put("uuid", bankSlip.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        BankSlipPaymentRequest paymentRequest = BankSlipPaymentRequest.builder().paymentDate(LocalDate.now()).build();
        HttpEntity entity = new HttpEntity(paymentRequest, headers);

        final ResponseEntity responseEntityPut = restTemplate.exchange(
                "/bankslips/{uuid}/payments", HttpMethod.PUT, entity, BankSlipPaymentRequest.class, params);

        assertEquals(HttpStatus.NO_CONTENT, responseEntityPut.getStatusCode());
        assertEquals(BankSlipStatus.PAID, repository.findByUuid(bankSlip.getId()).get().getStatus());

    }

    @Test
    public void payBankSlipShouldReturnNotFound() {
        final String bankSlipUuid = "werwerwer";
        Map<String, String> params = new HashMap<>();
        params.put("uuid", bankSlipUuid);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        BankSlipPaymentRequest request = BankSlipPaymentRequest.builder().paymentDate(LocalDate.now()).build();
        HttpEntity entity = new HttpEntity(request, headers);

        final ResponseEntity responseEntity = restTemplate.exchange(
                "/bankslips/{uuid}/payments", HttpMethod.PUT, entity, BankSlipPaymentRequest.class, params);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void cancelBankSlip() {
        BankSlipRequest request = createBankSlipRequest();
        final ResponseEntity<BankSlipResponse> responseEntity = restTemplate.postForEntity(
                "/bankslips", request, BankSlipResponse.class);

        final BankSlipResponse bankSlip = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        Map<String, String> params = new HashMap<>();
        params.put("uuid", bankSlip.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity(null, headers);

        final ResponseEntity responseEntityPut = restTemplate.exchange(
                "/bankslips/{uuid}", HttpMethod.DELETE, entity, Object.class, params);

        assertEquals(HttpStatus.NO_CONTENT, responseEntityPut.getStatusCode());
        assertEquals(BankSlipStatus.CANCELED, repository.findByUuid(bankSlip.getId()).get().getStatus());
    }

    @Test
    public void cancelBankSlipShouldReturnNotFound() throws Exception {
        final String bankSlipUuid = "werwerwer";
        Map<String, String> params = new HashMap<>();
        params.put("uuid", bankSlipUuid);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity(null, headers);

        final ResponseEntity responseEntity = restTemplate.exchange(
                "/bankslips/{uuid}", HttpMethod.DELETE, entity, Object.class, params);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private BankSlipRequest createBankSlipRequest() {
        final LocalDate tomorrow = LocalDate.now().plusDays(1);

        return BankSlipRequest.builder()
                .customer(faker.company().name())
                .dueDate(tomorrow)
                .totalInCents(new BigDecimal(faker.numerify("#########")))
                .build();
    }
}
