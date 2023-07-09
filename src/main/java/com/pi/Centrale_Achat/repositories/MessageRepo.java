package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message,Integer> {
}
