package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Integer> {
}
