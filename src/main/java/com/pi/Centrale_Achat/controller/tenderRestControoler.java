package com.pi.Centrale_Achat.controller;


import com.pi.Centrale_Achat.entities.Category;
import com.pi.Centrale_Achat.entities.Product;
import com.pi.Centrale_Achat.entities.Tender;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.TenderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.Itenderservice;
import com.pi.Centrale_Achat.service.ProductService;
import com.pi.Centrale_Achat.serviceImpl.EmailService;
import com.pi.Centrale_Achat.serviceImpl.TenderserviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weka.core.Instances;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tender")
public class tenderRestControoler {

    @Autowired
     ProductService productService;

        @Autowired
        Itenderservice itenderservice;

        @Autowired
    TenderserviceImpl tenderservice;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TenderRepo tenderRepo;

//    @PostMapping("/mail")
//    public ResponseEntity<Tender> createTender(@RequestBody Tender tender) throws MessagingException, javax.mail.MessagingException {
//        Tender createdTender = itenderservice.createTender(tender);
//
//       // send email to all users
//       emailService.sendEmailToAllUsers(createdTender);
//
//        return ResponseEntity.ok().body(createdTender);
//   }

    // other controller methods

    /*@PostMapping("/add")
    public ResponseEntity<Tender> createTender(@RequestBody Tender tender) {
        Tender createdTender = itenderservice.createTender(tender);
        return new ResponseEntity<>(createdTender, HttpStatus.CREATED);}*/


    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/update/{idtender}")
    public ResponseEntity<?> updateTender(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Tender tender,@PathVariable int idtender) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        List<Tender>tenders = tenderRepo.findAll();

        for (Tender tender1 : tenders){
            if (!(currentUser.getId()==tender1.getUser().getId())){
               return new ResponseEntity<>("erreur", HttpStatus.FORBIDDEN);
          }
            else {
                tenderservice.updateTender(userDetails,tender,idtender);
                return new ResponseEntity<>("tender modifier avec success", HttpStatus.OK);
            }

        }

        return new ResponseEntity<>("Not found ", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public List<Tender> getAllTenders() {
        return itenderservice.retrieveAllTenders();
    }


    @GetMapping("/tenderuser")
    @PreAuthorize("hasRole('CUSTOMER')")

    public ResponseEntity<?> getTenderforUsers(@AuthenticationPrincipal UserDetails userDetails) {
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser == null) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND) ;
        } else
            return ResponseEntity.ok(itenderservice.getTenderforUsers(userDetails));
    }


    @GetMapping("get/{id}")
    public Tender getTender(@PathVariable("id") Integer idTender)
    {
        return itenderservice.retrieveTender(idTender);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public void deleteTender(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("id") int idTender)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Tender tender = tenderRepo.findById(idTender).orElse(null);
        if ( tender != null && (currentUsername.equals(tender.getUser().getUsername()))) {
            tenderservice.removeTender(userDetails,idTender);
        } else {

                     throw new AccessDeniedException("erreur");
        }    }

    @PostMapping("/data")
    public ResponseEntity<String> createDataInstance(@RequestBody List<Tender> tenders) {
        try {
            Instances data = itenderservice.createDataInstance(tenders);
            return ResponseEntity.ok(data.toString());
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Error creating data instance: " + e.getMessage());
        }
    }

    @PostMapping("add_image")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Tender> addAvecImage(@AuthenticationPrincipal UserDetails userDetails, @RequestPart Tender tender, @RequestPart MultipartFile image) throws MessagingException,  javax.mail.MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currecntUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currecntUserName);
        if (currentUser==null){
            System.out.println("erreur");
        }
         itenderservice.addAvecImage(userDetails,tender,image);
        // send email to all users
        emailService.sendEmailToAllUsers(tender);

        return ResponseEntity.ok().body(tender);
    }
    @GetMapping("/tender/analyse")
    public void getTenderAnalyse() throws Exception {
        itenderservice.getTenderAnalyse();
    }

    @GetMapping("/{productId}/acceptance-rate")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<Map<String, Double>> getProductAcceptanceRate(@PathVariable int productId) throws Exception {


        double acceptanceRate = itenderservice.getProductAcceptanceRate(productId);
        Map<String, Double> response = new HashMap<>();
        response.put("acceptance_rate", acceptanceRate);
        return ResponseEntity.ok(response);
    }




    @GetMapping("filter")

    public ResponseEntity<List<Tender>> filterTenders(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String marque) {





        List<Tender> tenders = itenderservice.filterTenders(nom, description, marque);
        return ResponseEntity.ok(tenders);
    }

//
//    @GetMapping("/{productId}")
//    public ResponseEntity<Product> getProductById(@PathVariable int productId) {
//        Product product = productService.getProductById(productId);
//        if (product != null) {
//            return ResponseEntity.ok(product);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


    @GetMapping("/similar")
    public ResponseEntity<List<Tender>> getSimilarProducts(@RequestParam("url") String productUrl) {
        try {
            List<Tender> similarProducts = itenderservice.findSimilarProducts(productUrl);
            return ResponseEntity.ok(similarProducts);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/deleteadmin/{id}")

    public void deleteTenderadmin(@PathVariable("id") int idTender) {


        Tender tender = tenderRepo.findById(idTender).orElse(null);

        tenderservice.removeTender(idTender);
    }

}





