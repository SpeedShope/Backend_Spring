package com.pi.Centrale_Achat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pi.Centrale_Achat.entities.Contract;
@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer>{

}
