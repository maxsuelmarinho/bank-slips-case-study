package com.marinho.bankslips.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.exception.BankSlipNotFoundException;
import com.marinho.bankslips.service.BankSlipService;
import com.marinho.bankslips.service.IBankSlipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = Api.class)
public class ApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IBankSlipService service;

    private Faker faker = new Faker();

    @Test
    public void createBankSlip() throws Exception {

        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        BankSlipRequest request = BankSlipRequest.builder()
                .customer(faker.company().name())
                .dueDate(tomorrow)
                .totalInCents(new BigDecimal(faker.numerify("#########")))
                .build();

        final BankSlipResponse mockResponse = BankSlipResponse.builder()
                .id(UUID.randomUUID().toString())
                .dueDate(request.getDueDate())
                .totalInCents(request.getTotalInCents())
                .customer(request.getCustomer())
                .status("PENDING")
                .build();

        when(service.create(request)).thenReturn(mockResponse);

        mvc.perform(
                post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.due_date", is(request.getDueDate().toString())))
                .andExpect(jsonPath("$.total_in_cents").value(request.getTotalInCents()))
                .andExpect(jsonPath("$.customer", is(request.getCustomer())))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenCustomerIsNotPresent() throws Exception {

        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        BankSlipRequest request = BankSlipRequest.builder()
                .dueDate(tomorrow)
                .totalInCents(new BigDecimal(faker.numerify("#########")))
                .build();

        mvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field", is("customer")));
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenDueDateIsNotPresent() throws Exception {

        BankSlipRequest request = BankSlipRequest.builder()
                .customer(faker.company().name())
                .totalInCents(new BigDecimal(faker.numerify("#########")))
                .build();

        mvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field", is("dueDate")));
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenTotalInCentsIsNotPresent() throws Exception {

        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        BankSlipRequest request = BankSlipRequest.builder()
                .dueDate(tomorrow)
                .customer(faker.company().name())
                .build();

        mvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field", is("totalInCents")));
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenTotalInCentsIsNotPositiveValue() throws Exception {

        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        BankSlipRequest request = BankSlipRequest.builder()
                .dueDate(tomorrow)
                .customer(faker.company().name())
                .totalInCents(new BigDecimal(-1))
                .build();

        mvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field", is("totalInCents")));
    }

    @Test
    public void createShouldReturnBadRequestWhenNoBodyIsPresent() throws Exception {
        mvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void listBankSlips() throws Exception {
        final List<BankSlipResponse> mockList = Arrays.asList(
                BankSlipResponse.builder()
                        .id("asdasdasd")
                        .dueDate(LocalDate.of(2018, 9, 12))
                        .totalInCents(new BigDecimal("1000000000"))
                        .customer("Fake Company")
                        .status("PENDING")
                        .build(),
                BankSlipResponse.builder()
                        .id("qweqweqwe")
                        .dueDate(LocalDate.of(2018, 9, 13))
                        .totalInCents(new BigDecimal("1000000001"))
                        .customer("Real Company")
                        .status("PENDING")
                        .build());

        when(service.findAll()).thenReturn(mockList);

        MvcResult result = mvc.perform(
                get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final String expected = "[{id:\"asdasdasd\",due_date:\"2018-09-12\",total_in_cents:1000000000,customer:\"Fake Company\",status:\"PENDING\"}," +
                "{id:\"qweqweqwe\",due_date:\"2018-09-13\",total_in_cents:1000000001,customer:\"Real Company\",status:\"PENDING\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void retrieveBankSlip() throws Exception {
        final String bankSlipId = "qweqweqwe";

        final BankSlipResponse mockResponse = BankSlipResponse.builder()
                .id(bankSlipId)
                .dueDate(LocalDate.of(2018, 9, 13))
                .totalInCents(new BigDecimal("1000000001"))
                .customer("Real Company")
                .status("PENDING")
                .build();

        when(service.findById(bankSlipId)).thenReturn(mockResponse);

        MvcResult result = mvc.perform(get("/" + bankSlipId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final String expected = "{id:\"qweqweqwe\",due_date:\"2018-09-13\",total_in_cents:1000000001,customer:\"Real Company\",status:\"PENDING\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void retrieveBankSlipShouldReturnNotFound() throws Exception {
        final String bankSlipId = "zxczxczxc";

        doThrow(BankSlipNotFoundException.class).when(service).findById(bankSlipId);

        mvc.perform(get("/" + bankSlipId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void payBankSlip() throws Exception {
        final String bankSlipId = "asdasdasd";

        final BankSlipResponse mockResponse = BankSlipResponse.builder()
                .id(bankSlipId)
                .dueDate(LocalDate.of(2018, 9, 12))
                .totalInCents(new BigDecimal("1000000000"))
                .customer("Fake Company")
                .status("PENDING")
                .build();

        doAnswer(it -> {
            mockResponse.setStatus("PAID");
            return null;
        }).when(service).pay(bankSlipId);

        mvc.perform(put("/" + bankSlipId + "/payments"))
                .andExpect(status().isNoContent());

        assertEquals("PAID", mockResponse.getStatus());
    }

    @Test
    public void payBankSlipShouldReturnNotFound() throws Exception {
        final String bankSlipId = "zxczxczxc";

        doThrow(BankSlipNotFoundException.class).when(service).pay(bankSlipId);

        mvc.perform(put("/" + bankSlipId + "/payments"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void cancelBankSlip() throws Exception {
        final String bankSlipId = "asdasdasd";

        final BankSlipResponse mockResponse = BankSlipResponse.builder()
                .id(bankSlipId)
                .dueDate(LocalDate.of(2018, 9, 12))
                .totalInCents(new BigDecimal("1000000000"))
                .customer("Fake Company")
                .status("PENDING")
                .build();

        doAnswer(it -> {
            mockResponse.setStatus("CANCELED");
            return null;
        }).when(service).cancel(bankSlipId);

        mvc.perform(delete("/" + bankSlipId))
                .andExpect(status().isNoContent());

        assertEquals("CANCELED", mockResponse.getStatus());
    }

    @Test
    public void cancelBankSlipShouldReturnNotFound() throws Exception {
        final String bankSlipId = "zxczxczxc";

        doThrow(BankSlipNotFoundException.class).when(service).cancel(bankSlipId);

        mvc.perform(delete("/" + bankSlipId))
                .andExpect(status().isNotFound());
    }
}
