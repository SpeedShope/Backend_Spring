package com.pi.Centrale_Achat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pi.Centrale_Achat.entities.Mission;

@Repository
public interface MissionRepository  extends JpaRepository<Mission , Integer>{

}
