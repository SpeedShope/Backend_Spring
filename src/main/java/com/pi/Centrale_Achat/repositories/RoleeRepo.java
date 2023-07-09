package com.pi.Centrale_Achat.repositories;


import com.pi.Centrale_Achat.entities.ERole;
import com.pi.Centrale_Achat.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleeRepo extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(ERole name);

}
