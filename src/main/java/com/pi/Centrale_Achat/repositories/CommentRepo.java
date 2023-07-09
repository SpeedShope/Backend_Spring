package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.Comment;
import com.pi.Centrale_Achat.entities.Tender;
import com.pi.Centrale_Achat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment,Integer> {
    List<Comment> findBySignale(Boolean signale);
    List<Comment> findByTender_Id(int idtender);
    List<Comment> findByUserComment(User user);
}
