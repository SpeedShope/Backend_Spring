package com.pi.Centrale_Achat.serviceImpl;


import com.pi.Centrale_Achat.entities.Comment;
import com.pi.Centrale_Achat.entities.Tender;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.CommentRepo;
import com.pi.Centrale_Achat.repositories.TenderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentService implements ICommentService {

    @Autowired
    CommentRepo commentRepository;
    @Autowired
    TenderRepo tenderRepository;


    //private static final List<String> MOTS_OFFENSANTS = Arrays.asList("insulte", "raciste", "sexiste", "discrimination");
    private static final String REGEX_TELF = ".*\\b[0-9]{8}\\b.*";
    private final UserRepo userRepo;

    public CommentService(CommentRepo commentaireRepository,
                          UserRepo userRepo) {
        this.commentRepository = commentaireRepository;
        this.userRepo = userRepo;
    }
    @Override
    public Comment addCommentaire(@AuthenticationPrincipal UserDetails userDetails, Comment commentaire, int idTender) throws IOException {
        Resource resource = new FileSystemResource("C:/Users/User/Desktop/spring-update/Centrale_AchatPi/src/main/resources/imageProduct/Bad.txt");
        InputStream inputStream = resource.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line);
            stringBuilder.append("  ");
        }
        String[] MOTS_OFFENSANTS = stringBuilder.toString().split("\\s+");

        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        if (commentaire.getMessage().matches(REGEX_TELF)) {
            throw new IllegalArgumentException("Le commentaire contient un numéro de téléphone.");
        }
        String message = commentaire.getMessage().toLowerCase();
        for (String mot : MOTS_OFFENSANTS) {
            if (message.contains(mot)) {
                throw new IllegalArgumentException("Le commentaire contient des mots offensants.");
            }
        }

        Tender tender = tenderRepository.findById(idTender).orElse(null);
        commentaire.setUserComment(user1);
        commentaire.setTender(tender);
        Comment e =commentRepository.save(commentaire);

        return e;

    }
    @Override
    public void signalerCommentaire(int id) {
        Comment commentaire = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commentaire introuvable avec l'id " + id));

        commentaire.setSignale(true);

        commentRepository.save(commentaire);
    }
    @Override
    public List<Comment> getCommentairesSignales() {
        return commentRepository.findBySignale(true);
    }


    @Override
    public Comment ajouterComment(Comment comment) {
        return null;
    }
    @Override
    public Comment modifierComment(@AuthenticationPrincipal UserDetails userDetails,Comment comment,int idComment) {

        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        List<Comment>comments = commentRepository.findAll();

        for (Comment c : comments){
            if (c.getUserComment().getId()==user1.getId()){
                if(c.getId()==idComment){
                    c.setMessage(comment.getMessage());
                    comment = c;
                    commentRepository.save(c);

                }}
        }

        return comment;


    }

    @Override
    public List<Comment> retrieveAllComments(@AuthenticationPrincipal UserDetails userDetails) {
        String currecntUserName = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currecntUserName);
        return commentRepository.findByUserComment(user1);
    }

    @Override
    public Comment retrieveComment(@AuthenticationPrincipal UserDetails userDetails,int idComment) {

        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (comment.getUserComment().getId()==user1.getId())
            return comment;


        return comment;
    }

    @Override
    public void removeComment(@AuthenticationPrincipal UserDetails userDetails,int idComment) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (comment.getUserComment().getId()==user1.getId()){
            commentRepository.deleteById(idComment);
        }

    }

    @Override
    public void ajouterCommentTender(Comment comment , int idTender) {

        Comment e=commentRepository.save(comment);
        Tender tender = tenderRepository.findById(idTender).orElse(null);
        e.setTender(tender);}




}

