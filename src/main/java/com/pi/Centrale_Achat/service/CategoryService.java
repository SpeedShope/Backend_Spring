package com.pi.Centrale_Achat.service;

import com.pi.Centrale_Achat.entities.Category;

import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;

public interface CategoryService {

    List<Category> show_All(UserDetails userDetails);
    Category saveCategorie(UserDetails userDetails,Category ca);
    Category modifierCategorie(UserDetails userDetails,Category ca,int id);
    void delete(UserDetails userDetails,int idc);

}
