package com.marinho.bankslips.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.marinho.bankslips.dto.BankSlipRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BankSlipsController.class)
public class BankSlipsControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private Faker faker = new Faker();

    @Test
    public void createBankSlip() throws Exception {
        BankSlipRequest request = BankSlipRequest.builder()
                .customer(faker.company().name())
                .dueDate("2018-09-12")
                .totalInCents(faker.numerify("#########"))
                .build();

        mvc.perform(
                post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.due_date", is(request.getDueDate())))
                .andExpect(jsonPath("$.total_in_cents", is(request.getTotalInCents())))
                .andExpect(jsonPath("$.customer", is(request.getCustomer())))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    public void listBankSlips() throws Exception {
        MvcResult result = mvc.perform(
                get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final String expected = "[{id:\"asdasdasd\",due_date:\"2018-09-12\",total_in_cents:\"1000000000\",customer:\"Fake Company\",status:\"PENDING\"}," +
                "{id:\"qweqweqwe\",due_date:\"2018-09-13\",total_in_cents:\"1000000001\",customer:\"Real Company\",status:\"PENDING\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }
}
