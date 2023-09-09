package com.pi.Centrale_Achat.serviceImpl;

import com.pi.Centrale_Achat.dto.DiscountDto;
import com.pi.Centrale_Achat.entities.*;
import com.pi.Centrale_Achat.repositories.*;
import com.pi.Centrale_Achat.service.ProductService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Date;
import java.util.List;
;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final StockRepo mvStockRepo;
    private final OrderRepo orderRepo;
    private final CategoryRepo categoryRepo;

    private final SmsService smsService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @Autowired
//    private AlertService alertService;
//    @Autowired
//    private NotifierClient notifierClient;

    @Autowired
    private TenderRepo tenderRepo;
    @Override
    public List<Product> show_AllProducts() {

        return productRepo.findAll();
    }
    @Override
    public List<Product> show_AllProductstender(int idtender) {

        return productRepo.findByTender_Id(idtender);
    }
    @Override
    public Product showProduct(int idproduct) {

        return productRepo.findById(idproduct).orElse(null);
    }
    @Override
    public List<Product> show_ProductsOf_Category(int idCategory) {
        Category category = categoryRepo.findById(idCategory).get();
        return category.getProducts();
    }

    public List<Product> show_User_Products(User curentUser) {

        return curentUser.getProducts();
    }

    @Override
    public Product save(@AuthenticationPrincipal UserDetails userDetails, String name, float price, int qte, String description, int maxStock, int idCategory, MultipartFile file) throws IOException {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);

        Product p = new Product();
        p.setImage(file.getOriginalFilename());
        Files.copy(file.getInputStream(), Paths.get("src/main/resources/imageProduct/" + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        p.setDescription(description);
        p.setName(name);
        p.setPrice(price);
        p.setMaxStock(maxStock);
        p.setCategory(categoryRepo.findById(idCategory).orElse(null));
        p.setQte(qte);

        p.setUser(user1);

        productRepo.save(p);
        MvStock mvStk = new MvStock();
        mvStk.setProduct(p);
        mvStk.setDateMvt(new Date());
        mvStk.setTypeMvt(TypeMvStock.entree);
        mvStk.setQuantiteMvt(qte);
        mvStockRepo.save(mvStk);

        return p;      }
    
    @Override
    public Product addAvecImage(@AuthenticationPrincipal UserDetails userDetails,Product product, MultipartFile image) {
        String currecntUserName = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currecntUserName);

        try {

            product.setImage( "src\\main\\resources\\imageProduct\\" + product.getName() + product.getDescription() + ".png");

            InputStream inputStream = image.getInputStream();

            // Create a byte array to hold the content of the uploaded file
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

            // Create a new file using the destination path
            File destinationFile = new File(product.getImage());


            // Write the bytes to the new file
            FileUtils.writeByteArrayToFile(destinationFile, bytes);
            messagingTemplate.convertAndSend("/topic/tenders", product);
            product.setUser(user1);
            return productRepo.save(product);
        } catch (IOException ioe) {
            log.error("IO Problem : " + ioe.getMessage());
            return null;
        }
    }



    @Override
    public Product modifier(@AuthenticationPrincipal UserDetails userDetails,String name,
                            float price, String description, int maxStock, MultipartFile file, int idP) throws IOException {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Product p = productRepo.findById(idP).orElse(null);
        if (p.getUser().getId()==user1.getId()){
            Files.copy(file.getInputStream(), Paths.get("src/main/resources/imageProduct/" + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            p.setName(name);
            p.setDescription(description);
            p.setPrice(price);
            p.setMaxStock(maxStock);
            p.setImage(file.getOriginalFilename());
            productRepo.save(p);
        }



        return p;

    }
    @Override
    public Product updateQuantity(@AuthenticationPrincipal UserDetails userDetails,int qte, int idP) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Product p = productRepo.findById(idP).orElse(null);
        if (p.getUser().getId()==user1.getId()){
            p.setQte(qte);
            productRepo.save(p);
            MvStock mvStock = new MvStock();
            mvStock.setProduct(p);
            mvStock.setQuantiteMvt(qte);
            mvStock.setTypeMvt(TypeMvStock.entree);
            mvStock.setDateMvt(new Date());
            mvStockRepo.save(mvStock);
        }

        return p;
    }
    @Override
    public byte[] findByIdImage(int idP) throws IOException {

        Product p = productRepo.findById(idP).orElse(null);
        String photoName = p.getImage();


        // var imgFile = new ClassPathResource("imageProduct/" + photoName);

        ClassPathResource resource = new ClassPathResource("imageProduct/c1.PNG");
        InputStream inputStream = resource.getInputStream();


        // byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        byte[] bytes = StreamUtils.copyToByteArray(inputStream);
        return bytes;
    }
    @Override
    public void delete(@AuthenticationPrincipal UserDetails userDetails,int idP) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Product product = productRepo.findById(idP).orElse(null);
        if(product.getUser().getId()==user1.getId()){
            productRepo.deleteById(idP);
        }
        else {
            System.out.println("erreur");
        }


    }
    public Product apply_discount(@AuthenticationPrincipal UserDetails userDetails,int idProduct, DiscountDto discountDto) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Product product = productRepo.findById(idProduct).get();
        if(product.getUser().getId()==user1.getId()) {
            product.setStartDateDiscount(discountDto.getStartDateDiscount());
            product.setEndDateDiscount(discountDto.getEndDateDiscount());
            Float calculDiscount = (product.getPrice() * discountDto.getTauxDiscount() / 100);
            product.setDiscount(calculDiscount);
            productRepo.save(product);
        }
        return product;
    }

    @Scheduled(fixedDelay = 1000)//
    public void discount() {
        Date currentDate = new Date();
        List<Product> products = productRepo.findAll();
        for (Product p : products) {


            if (p.getStartDateDiscount() != null) {
               float prixInitial= p.getPrice();

                if (currentDate.after(p.getStartDateDiscount()) && currentDate.before(p.getEndDateDiscount())) {

                    Float nouveauprix=(prixInitial *p.getDiscount())/100;
                    p.setPrice(nouveauprix);
                    p.setDiscount(0);
                    productRepo.save(p);
                    System.out.println("product in remise" + p.getName());
                } else {

                    p.setStartDateDiscount(null);
                    p.setEndDateDiscount(null);
                    prixInitial = p.getPrice() +p.getDiscount();
                    p.setPrice(prixInitial);

                    productRepo.save(p);
                    System.out.println(" produit " + p.getName() + " end date remise ");
                }

            }


        }

    }

    //@Scheduled(fixedDelay = 1000)//
    public void notifier_Furnisseur(){
        List<Product> products = productRepo.findAll();
        for (Product p : products) {
            if (p.getQte() <p.getMaxStock()){
                String recipient_fournisseur=p.getUser().getNumTel();
                String message = String.format("Alert: Product %s has reached the minimum quantity of %d", p.getName(), p.getQte());
                smsService.sendSMS(recipient_fournisseur,message);
                System.out.println();

            }

        }

    }

    public void provide_Tender(@AuthenticationPrincipal UserDetails userDetails , String name, float price, int qte, String description, int maxStock, MultipartFile file, int idTender)throws IOException{
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        Tender tender= tenderRepo.findById(idTender).get();

        Product product=new Product();
        product.setImage(file.getOriginalFilename());
        Files.copy(file.getInputStream(), Paths.get("src/main/resources/imageProduct/" + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        product.setDescription(description);
        product.setName(name);
        product.setPrice(price);
        product.setMaxStock(maxStock);


        product.setQte(qte);
        product.setUser(currentUser);

        product.setTender(tender);
        productRepo.save(product);
        MvStock mvStk = new MvStock();
        mvStk.setProduct(product);
        mvStk.setDateMvt(new Date());
        mvStk.setTypeMvt(TypeMvStock.entree);
        mvStk.setQuantiteMvt(qte);
       mvStockRepo.save(mvStk);
        String responsableTenderName=tender.getUser().getUsername();
        String responsableTenderPhone=tender.getUser().getNumTel();
        String message = "Salut Mr  "+responsableTenderName+" votre demande tender "+ tender.getName()+" est reussi consulter l'application pour plus details ";
        smsService.sendSMS(responsableTenderPhone,message);
       System.out.println("produit ajouter avec success ");

    }
    @Override
    public void updateOperatorScore(int operatorId) {
        User user = userRepo.findById(operatorId).orElse(null);
        if (user != null) {
            List<Product> products = productRepo.findByUser_Id(user.getId());;
            Float totalScore = Float.valueOf(0);
            for (Product product : products) {
                totalScore += product.getRating();
            }
            Float averageScore = products.size() > 0 ? totalScore / products.size() : 0;
            user.setScore(averageScore);
            userRepo.save(user);
        }
    }








}
