package com.wiktorkielar.crud.repository;

import com.wiktorkielar.crud.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findEmployeeEntityByUuid(String uuid);

    @Transactional
    void deleteByUuid(String uuid);
}
