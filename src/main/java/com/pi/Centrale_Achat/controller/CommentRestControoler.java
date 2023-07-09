package com.pi.Centrale_Achat.controller;


import com.pi.Centrale_Achat.entities.Comment;
import com.pi.Centrale_Achat.entities.Tender;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.CommentRepo;
import com.pi.Centrale_Achat.repositories.TenderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comment")
public class CommentRestControoler {

    @Autowired
    private CommentRepo commentRepo;

        @Autowired
        ICommentService iCommentService;

    @Autowired
    UserRepo userRepo;


    @PutMapping("/add/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Comment addComment(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Comment comment, @PathVariable("id") Integer idtender) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        return iCommentService.addCommentaire(userDetails,comment,idtender);
    }
//    @PostMapping("/add")
//    @PreAuthorize("hasRole('CUSTOMER')")
//    public Comment addComment(@RequestBody Comment comment){
//       return iCommentService.ajouterComment(comment);
//    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Comment updateComment(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Comment comment,@PathVariable("id") Integer idtender) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        return iCommentService.modifierComment(userDetails,comment,idtender);
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getAllComments(@AuthenticationPrincipal UserDetails userDetails) {


            String currentUserName = userDetails.getUsername();
            User currentUser = userRepo.findUserByUsername(currentUserName);
            if (currentUser == null) {
                return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND) ;
            } else
                return ResponseEntity.ok(iCommentService.retrieveAllComments(userDetails));
        }

    @GetMapping("get/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Comment getComment(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("id") int idComment)
    {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            Comment comment = commentRepo.findById(idComment).orElse(null);
            if ( comment != null && (currentUsername.equals(comment.getUserComment().getUsername()))) {
                return iCommentService.retrieveComment(userDetails,idComment);
            } else {

                throw new AccessDeniedException("erreur");
            }

    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public void deleteComment(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("id") int idComment)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Comment comment = commentRepo.findById(idComment).orElse(null);
        if ( comment != null && (currentUsername.equals(comment.getUserComment().getUsername()))) {
            iCommentService.removeComment(userDetails,idComment);
        } else {

            throw new AccessDeniedException("erreur");
        }
    }


}
