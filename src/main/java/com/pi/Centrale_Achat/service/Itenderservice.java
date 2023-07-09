package com.pi.Centrale_Achat.service;




import com.pi.Centrale_Achat.entities.Product;
import com.pi.Centrale_Achat.entities.Tender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import weka.core.Instances;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface Itenderservice {
    Tender ajoutertender(Tender tender);
    List<Tender> retrieveAllTenders();

    List<Tender> getTenderforUsers(UserDetails userDetails);


    Tender retrieveTender (int idTender);

    void removeTender(UserDetails userDetails,int idTender);
    void removeTender(int idTender);
//    public Tender createTender(Tender tender);
    List<Tender> getTendersWithOffers();
    List<Tender> filterTenders(String nom, String Description, String marque);



    Tender addAvecImage(UserDetails userDetails,Tender tender, MultipartFile image);
     Instances createDataInstance(List<Tender> tenders) throws ParseException;
      void getTenderAnalyse() throws Exception;
    public  List<Tender> getTendersWithcomments();
    public double getProductAcceptanceRate(int productId) throws Exception;
    public List<Tender> findSimilarProducts(String productUrl) throws IOException;
    public Tender updateTender( UserDetails userDetails, Tender ta,int idtender);
}
