package com.mkcode.factoring;

import com.mkcode.factoring.persistence.ContractRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class FactoringApplicationTests {
    private static final String CONTRACT_NO = "A10";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    @Order(10)
    void testSaveContract() throws Exception {
        assertThat(contractRepository.findByContractNo(CONTRACT_NO)).isEmpty();

        mockMvc.perform(post("/contract")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"contractNo\": \"" + CONTRACT_NO + "\",\n" +
                        "  \"active\": true,\n" +
                        "  \"startDate\": \"2020-01-01\",\n" +
                        "  \"fixedFee\": 2,\n" +
                        "  \"daysIncluded\": 14,\n" +
                        "  \"additionalFee\": 0.1\n" +
                        "}"))
                .andExpect(status().isCreated());

        assertThat(contractRepository.findByContractNo(CONTRACT_NO))
                .hasSize(1)
                .extracting("contractNo").containsOnlyOnce(CONTRACT_NO);
    }

    @Test
    @Order(20)
    void testSaveNewContractAndEndPreviousContract() throws Exception {
        assertThat(contractRepository.findByContractNo(CONTRACT_NO)).hasSize(1);

        mockMvc.perform(post("/contract")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"contractNo\": \"" + CONTRACT_NO + "\",\n" +
                        "  \"active\": true,\n" +
                        "  \"startDate\": \"2020-09-01\",\n" +
                        "  \"fixedFee\": 1.75,\n" +
                        "  \"daysIncluded\": 14,\n" +
                        "  \"additionalFee\": 0.1\n" +
                        "}"))
                .andExpect(status().isCreated());

        assertThat(contractRepository.findByContractNo(CONTRACT_NO))
                .hasSize(2)
                .extracting("contractNo", "startDate", "endDate")
                .containsExactlyInAnyOrder(
                        tuple(CONTRACT_NO, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-08-31")),
                        tuple(CONTRACT_NO, LocalDate.parse("2020-09-01"), null));
    }

    @Order(30)
    @Test
    void testFeeCalculation() throws Exception {
        assertThat(contractRepository.findActualContract(CONTRACT_NO, LocalDate.parse("2020-08-31"))).isPresent();
        mockMvc.perform(post("/fee/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"contractNo\": \"" + CONTRACT_NO + "\",\n" +
                        "  \"issueDate\": \"2020-08-31\",\n" +
                        "  \"dueDate\": \"2020-10-15\",\n" +
                        "  \"paidDate\": \"2020-10-17\",\n" +
                        "  \"purchaseDate\": \"2020-09-01\",\n" +
                        "  \"amount\": 1000\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("53.00"));
    }
}
