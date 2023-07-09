package com.pi.Centrale_Achat.serviceImpl;

import com.pi.Centrale_Achat.entities.Category;

import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.CategoryRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {
    private final CategoryRepo categoryRepo ;


    private  final UserRepo userRepo;



    @Override
    public List<Category> show_All(@AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        return categoryRepo.findByUser(user1);
    }

    @Override
    public Category saveCategorie(@AuthenticationPrincipal UserDetails userDetails, Category ca) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        ca.setUser(user1);

        return categoryRepo.save(ca);
    }

    @Override
    public Category modifierCategorie(UserDetails userDetails, Category ca, int id) {
        return null;
    }



    public Category modifierCategorie(@AuthenticationPrincipal UserDetails userDetails, Category ca) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        List<Category>categories = categoryRepo.findAll();
        Category category = new Category();
for (Category c : categories){
    if (c.getUser().getId()==user1.getId()){
        c.setNameCategory(ca.getNameCategory());
        category = c;
        categoryRepo.save(c);

    }
}
//        int iduser = ca.getUser().getId();
//        if (!(user1.getId()==iduser)) {
//            System.out.println("erreur");
//        }
//        ca.setNameCategory(ca.getNameCategory());
//        categoryRepo.save(ca);
//
//        return ca ;
        return category;
    }

    @Override
    public void delete(@AuthenticationPrincipal UserDetails userDetails,int idc) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        List<Category>categories = categoryRepo.findAll();
        for (Category category : categories){
            if (category.getUser().getId()==user1.getId()){
                categoryRepo.deleteById(idc);
            }
        }

    }
}
