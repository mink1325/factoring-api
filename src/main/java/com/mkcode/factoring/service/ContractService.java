package com.mkcode.factoring.service;

import com.mkcode.factoring.model.ContractDto;
import com.mkcode.factoring.persistence.ContractRepository;
import com.mkcode.factoring.persistence.model.Contract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository repository;

    public void save(ContractDto contractDto) {
        save(convertToContract(contractDto));
    }

    private void save(Contract contract) {
        var existingContracts = repository.findByContractNo(contract.getContractNo());

        validateIfExistingNewerContract(existingContracts, contract);

        findOverlappingContract(existingContracts, contract)
                .ifPresent(ec -> finalizeExistingContract(ec, contract.getStartDate()));

        repository.save(contract);
    }

    private void validateIfExistingNewerContract(List<Contract> existingContracts, Contract contract) {
        existingContracts.stream()
                .map(Contract::getStartDate)
                .filter(startDate -> contract.getStartDate().isBefore(startDate))
                .findAny()
                .ifPresent(startDate -> {
                    throw new ContractValidationException(format("There is newer contract starting at %tF", startDate));
                });
    }

    private void finalizeExistingContract(Contract contract, LocalDate startDate) {
        if (contract.getEndDate() == null) {
            contract.setEndDate(startDate.minusDays(1));
            repository.save(contract);
        } else {
            throw new ContractValidationException(
                    format("There is overlapping contract [%tF - %tF].", contract.getStartDate(), contract.getEndDate()));
        }
    }

    private Optional<Contract> findOverlappingContract(List<Contract> existingContracts, Contract contract) {
        return existingContracts.stream()
                .filter(existingContract -> contractOverlaps(existingContract, contract))
                .findFirst();
    }

    static boolean contractOverlaps(Contract first, Contract second) {
        return (second.getEndDate() == null || !first.getStartDate().isAfter(second.getEndDate())) &&
                (first.getEndDate() == null || !second.getStartDate().isAfter(first.getEndDate()));
    }

    private Contract convertToContract(ContractDto contractDto) {
        return new Contract(contractDto.getContractNo(),
                contractDto.isActive(),
                contractDto.getStartDate(),
                contractDto.getEndDate(),
                contractDto.getFixedFee(),
                contractDto.getDaysIncluded(),
                contractDto.getAdditionalFee());
    }
}