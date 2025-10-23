package com.srijan.expence_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srijan.expence_service.Model.ExpenceDTO;

public interface ExpenceDTORepository extends JpaRepository<ExpenceDTO, Integer> {

}
