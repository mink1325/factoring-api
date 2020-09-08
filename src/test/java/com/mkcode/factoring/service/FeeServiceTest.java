package com.mkcode.factoring.service;

import com.mkcode.factoring.model.InvoiceDto;
import com.mkcode.factoring.persistence.ContractRepository;
import com.mkcode.factoring.persistence.model.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeeServiceTest {

    @Mock
    private ContractRepository repository;

    private FeeService service;

    @BeforeEach
    void setup() {
        when(repository.findActualContract(any(), any())).thenReturn(createContract());
        service = new FeeService(repository);
    }

    @Test
    void givenInvoice_whenCalculateFee_thenFeeIsReturned() {
        var invoice = getInvoice("A01", "2020-10-17", "2020-09-01", "2020-09-01");

        assertThat(service.calculate(invoice).intValue()).isEqualTo(53);
    }

    @Test
    void givenInvoiceWithoutPaidDate_whenCalculateFee_thenFeeIsReturned() {
        var invoice = getInvoice("A02", null, "2020-08-01", "2020-08-01");
        long additionalDays = DAYS.between(LocalDate.parse("2020-08-14"), LocalDate.now());

        assertThat(service.calculate(invoice).intValue()).isEqualTo(20 + additionalDays);
    }

    @Test
    void givenInvoiceWithPaidDateBeforePurchaseDate_whenCalculateFee_thenFeeIsReturned() {
        var invoice = getInvoice("A03", "2020-07-01", "2020-08-01", "2020-07-01");
        long additionalDays = DAYS.between(LocalDate.parse("2020-08-14"), LocalDate.now());

        assertThat(service.calculate(invoice).intValue()).isEqualTo(20);
    }

    @Test
    void givenInvoiceForNonExistingContract_whenCalculateFee_thenExceptionIsReturned() {
        when(repository.findActualContract(eq("A11"), any())).thenReturn(Optional.empty());
        var invoice = getInvoice("A11", "2020-12-01", "2020-09-12", "2020-09-12");

        assertThatThrownBy(() -> service.calculate(invoice))
                .isExactlyInstanceOf(ContractNotFoundException.class)
                .hasMessage("No active contract A11 for date 2020-09-12 was found.");
    }

    private InvoiceDto getInvoice(String contractNo, String paidDate, String purchaseDate, String issueDate) {
        return new InvoiceDto(contractNo, LocalDate.parse(issueDate), null,
                paidDate == null ? null : LocalDate.parse(paidDate),
                LocalDate.parse(purchaseDate), BigDecimal.valueOf(1000));
    }

    private Optional<Contract> createContract() {
        var contract = new Contract("A1", true, LocalDate.MIN, LocalDate.MAX,
                BigDecimal.valueOf(2), 14, BigDecimal.valueOf(0.1));
        return Optional.of(contract);
    }
}