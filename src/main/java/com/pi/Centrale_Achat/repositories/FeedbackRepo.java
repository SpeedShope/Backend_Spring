package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<Feedback,Integer> {
}
