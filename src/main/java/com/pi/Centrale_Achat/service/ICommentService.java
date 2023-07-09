package com.pi.Centrale_Achat.service;



import com.pi.Centrale_Achat.entities.Comment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

public interface ICommentService {
    Comment ajouterComment(Comment comment);
    List<Comment> retrieveAllComments(UserDetails userDetails);


    Comment retrieveComment(UserDetails userDetails, int idComment);

    void removeComment(UserDetails userDetails,int idComment);
    void ajouterCommentTender(Comment comment,int idTender);

     void signalerCommentaire(int id);
     List<Comment> getCommentairesSignales();
    public Comment addCommentaire(UserDetails userDetails,Comment commentaire, int idTender) throws IOException;
    public Comment modifierComment(UserDetails userDetails,Comment comment,int idComment);
}
