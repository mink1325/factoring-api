package com.mkcode.factoring.service;

import com.mkcode.factoring.persistence.model.Contract;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.mkcode.factoring.service.ContractService.contractOverlaps;
import static java.time.LocalDate.ofYearDay;
import static org.assertj.core.api.Assertions.assertThat;

class ContractServiceTest {

    @Test
    void contractOverlapsTest() {
        var contract1to5 = createContract(ofYearDay(2020, 1), ofYearDay(2020, 5));
        var contract5to10 = createContract(ofYearDay(2020, 5), ofYearDay(2020, 10));
        var contract10to20 = createContract(ofYearDay(2020, 10), ofYearDay(2020, 20));
        var contract11 = createContract(ofYearDay(2020, 11), null);
        var contract12to20 = createContract(ofYearDay(2020, 12), ofYearDay(2020, 20));

        assertThat(contractOverlaps(contract5to10, contract1to5)).isTrue();
        assertThat(contractOverlaps(contract5to10, contract5to10)).isTrue();
        assertThat(contractOverlaps(contract5to10, contract10to20)).isTrue();
        assertThat(contractOverlaps(contract5to10, contract11)).isFalse();
        assertThat(contractOverlaps(contract11, contract12to20)).isTrue();
    }

    private Contract createContract(LocalDate startDate, LocalDate endDate) {
        var contract = new Contract();
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        return contract;
    }
}