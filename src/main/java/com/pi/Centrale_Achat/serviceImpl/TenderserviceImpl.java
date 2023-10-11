package com.pi.Centrale_Achat.serviceImpl;

import com.pi.Centrale_Achat.entities.*;
import com.pi.Centrale_Achat.repositories.CommentRepo;
import com.pi.Centrale_Achat.repositories.ProductRepo;
import com.pi.Centrale_Achat.repositories.TenderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.Itenderservice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import weka.classifiers.trees.J48;
import weka.core.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class TenderserviceImpl implements Itenderservice {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    CommentRepo commentRepository;
    @Autowired
    TenderRepo tenderRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ProductRepo productRepo;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tender ajoutertender(Tender tender) {
       return tenderRepository.save(tender);

    }

    @Override
    public List<Tender> retrieveAllTenders() {

       List<Tender> tenderList = new ArrayList<>();
        tenderRepository.findAll().forEach(tenderList::add);
        return tenderList;
    }

    @Override
    public List<Tender> getTenderforUsers(@AuthenticationPrincipal UserDetails userDetails) {
        String currecntUserName = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currecntUserName);
        return tenderRepository.findByUser(user1);
    }



    @Override
    public Tender retrieveTender(int idTender) {
        return tenderRepository.findById(idTender).orElse(null);
    }

    @Override
    public void removeTender(@AuthenticationPrincipal UserDetails userDetails,int idTender) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Tender tender = tenderRepository.findById(idTender).orElse(null);
            if (tender.getUser().getId()==user1.getId()){
                tenderRepository.deleteById(idTender);
            }
        }

    @Override
    public void removeTender(int idTender) {


        Tender tender = tenderRepository.findById(idTender).orElse(null);

            tenderRepository.deleteById(idTender);


    }


    //
//    @Override
//    public Tender createTender(@AuthenticationPrincipal UserDetails userDetails,Tender tender) {
//
//        messagingTemplate.convertAndSend("/topic/tenders", savedTender);
//        return savedTender;
//    }
@Override
public Tender updateTender(@AuthenticationPrincipal UserDetails userDetails,Tender ta,int idtender) {
    String currentUser = userDetails.getUsername();
    User user1 = userRepo.findUserByUsername(currentUser);
    List<Tender>tenders = tenderRepository.findAll();
    Tender tender = new Tender();
    for (Tender c : tenders){
        if (c.getUser().getId()==user1.getId()){
            if(c.getId()==idtender){
            c.setName(ta.getName());
            c.setDescription(ta.getDescription());
                c.setBrand(ta.getBrand());
                c.setQuantity(ta.getQuantity());
                c.setDateTender(ta.getDateTender());


            tender = c;
            tenderRepository.save(c);

        }}
    }

    return tender;
}





    @Override
    public List<Tender> filterTenders(String name, String description, String brand) {
/*

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tender> query = cb.createQuery(Tender.class);
        Root<Tender> root = query.from(Tender.class);

        // Ajouter les critères de filtrage
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            predicates.add((Predicate) cb.or(
                    cb.like(root.get("name"),   name),
                    cb.like(root.get("description"), "%" + name + "%")
            ));
        }
        if (StringUtils.isNotBlank(description)) {
            predicates.add((Predicate) cb.greaterThanOrEqualTo(root.get("startDate"), LocalDate.parse(description)));
        }
        if (StringUtils.isNotBlank(brand)) {
            predicates.add((Predicate) cb.lessThanOrEqualTo(root.get("endDate"), LocalDate.parse(brand)));
        }

        // Appliquer les critères de filtrage à la requête
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Tender> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();*/
       // log.info(name);
        List<Tender> tenders = new ArrayList<>();
        tenderRepository.findAll().forEach(tender -> {
            if(tender.getName().trim().equalsIgnoreCase(name) || tender.getDescription().trim().equalsIgnoreCase(description) || tender.getBrand().trim().equalsIgnoreCase(brand))
                tenders.add(tender);
        });
        return tenders;
    }

    @Override
    public  List<Tender> getTendersWithOffers() {
        List<Tender> tenders = tenderRepository.findAll();
        for (Tender tender : tenders) {
            List<Product> offers = productRepo.findByTender(tender);
            tender.setProducts(offers);
        }
        return tenders;
    }




    @Override
    public Tender addAvecImage(@AuthenticationPrincipal UserDetails userDetails,Tender tender, MultipartFile image) {
        String currecntUserName = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currecntUserName);

        try {

            tender.setImage( "src\\main\\resources\\images\\"+tender.getDescription()+".png");

            InputStream inputStream = image.getInputStream();

            // Create a byte array to hold the content of the uploaded file
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

            // Create a new file using the destination path
            File destinationFile = new File(tender.getImage());


            // Write the bytes to the new file
            FileUtils.writeByteArrayToFile(destinationFile, bytes);
            messagingTemplate.convertAndSend("/topic/tenders", tender);
            tender.setUser(user1);
            return tenderRepository.save(tender);
        } catch (IOException ioe) {
            log.error("IO Problem : " + ioe.getMessage());
            return null;
        }
    }


    @Override
    public Instances createDataInstance(List<Tender> tenders) throws ParseException {
        FastVector attributes = new FastVector();
     //   attributes.addElement(new Attribute("date", "yyyy-MM-dd"));
        attributes.addElement(new Attribute("price"));
        attributes.addElement(new Attribute("quantity"));
        attributes.addElement(new Attribute("offer_accepted", Arrays.asList("true", "false")));
     //   attributes.addElement(new Attribute("product_id"));


        Instances data = new Instances("tenders", attributes, tenders.size());

        for (Tender tender : tenders) {
            for (Product offer : tender.getProducts()) {
                double[] values = new double[data.numAttributes()];

            //  values[0] = new SimpleDateFormat("yyyy-MM-dd").parse(tender.getDateTender()).getTime();
                values[1] = offer.getPrice();
                values[2] = offer.getQte();
                values[3] = offer.isAccepted() ? 1 : 0;
            //   values[4] = offer.getId(); // product id


                data.add(new DenseInstance(1.0, values));
            }
        }




        return data;
    }

    @Override
    public void getTenderAnalyse() throws Exception {
        List<Tender> tenders = getTendersWithOffers();
        Instances data = createDataInstance(tenders);


        data.setClassIndex(data.numAttributes() - 1);

// build classifier
        J48 tree = new J48();
        tree.buildClassifier(data);
        // Print the summary of the classifier and the tree.
        System.out.println(tree);
        System.out.println(tree.graph());
    }
    @Override
    public  List<Tender> getTendersWithcomments() {
        List<Tender> tenders = tenderRepository.findAll();
        for (Tender tender : tenders) {
            List<Comment> comments = commentRepository.findByTender_Id(tender.getId());
            tender.setComments(comments);
        }
        return tenders;
    }
    @Override
    public double getProductAcceptanceRate(int productId) throws Exception {
        List<Tender> tenders = getTendersWithOffers();
        Instances data = createDataInstance(tenders);
        data.setClassIndex(data.numAttributes() - 1);

// build classifier
        J48 tree = new J48();
        tree.buildClassifier(data);

        // get number of instances classified as true and false for the given product id
        int acceptedCount = 0;
        int rejectedCount = 0;
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            if (instance.value(3) == productId) {
                if (tree.classifyInstance(instance) == 1) {
                    acceptedCount++;
                } else {
                    rejectedCount++;
                }
            }
        }

        // calculate acceptance and rejection rates
        int totalCount = acceptedCount + rejectedCount;
        double acceptanceRate = (double) acceptedCount / totalCount * 100;
        double rejectionRate = (double) rejectedCount / totalCount * 100;

        System.out.println("Product " + productId + " has an acceptance rate of " + acceptanceRate + "% and a rejection rate of " + rejectionRate + "%.");
        return acceptanceRate;
    }
    @Override
    public List<Tender> findSimilarProducts(String productUrl) throws IOException {
   /*     // Rechercher le produit dans la base de données
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for ID " + productId);
        }

        // Extraire les informations du produit à partir de la base de données
        String productName = product.getName();
        String productDescription = product.getDescription();

        // Extraire les informations sur les produits similaires à partir de l'autre site
        String htmlContent = Jsoup.connect(productUrl).get().html();
        Document doc = Jsoup.parse(htmlContent);
        Elements similarProductElements = doc.select("div.similar-products div.product");
        List<Product> similarProducts = new ArrayList<>();
        for (Element productElement : similarProductElements) {
            String similarProductName = productElement.select(".product-name").text();
            String similarProductDescription = productElement.select(".product-description").text();
            String similarProductImageUrl = productElement.select(".product-image").attr("src");
            log.info(productElement.select(".product-name").text() + " " + productElement.select(".product-description").text() + " " + productElement.select(".product-image").attr("src"));
            // Vérifier si le produit similaire correspond au produit recherché en comparant les attributs
            if (similarProductName.equalsIgnoreCase(productName)
                    && similarProductDescription.equalsIgnoreCase(productDescription)) {
                Product similarProduct = new Product(similarProductName, similarProductDescription, similarProductImageUrl);
                similarProducts.add(similarProduct);
            }
        }
        return similarProducts;*/
        List<Tender> similarProducts = new ArrayList<>();
        String data = getUrls(productUrl);
        for(Tender product : tenderRepository.findAll())
        {
            if(data.toLowerCase().contains(product.getName().toLowerCase()))
                similarProducts.add(product);
        }
return similarProducts;
    }

    private String getUrls(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String content = restTemplate.getForObject(url, String.class);
            return content;
        } catch (IllegalArgumentException illegalArgumentException) {
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}





