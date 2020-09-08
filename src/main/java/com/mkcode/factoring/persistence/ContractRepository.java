package com.mkcode.factoring.persistence;

import com.mkcode.factoring.persistence.model.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends CrudRepository<Contract, Long> {

    List<Contract> findByContractNo(String contractNo);

    @Query("select c from Contract c where contractNo = :contractNo and active = true " +
            "and startDate <= :date and (endDate >= :date or endDate is null)")
    Optional<Contract> findActualContract(@Param("contractNo") String contractNo, @Param("date") LocalDate date);
}

