package com.pi.Centrale_Achat.serviceImpl;
import com.pi.Centrale_Achat.dto.Coupon;
import com.pi.Centrale_Achat.entities.*;
import com.pi.Centrale_Achat.repositories.ProductRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import lombok.var;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.pi.Centrale_Achat.repositories.BillRepo;
import com.pi.Centrale_Achat.repositories.OrderRepo;
import com.pi.Centrale_Achat.service.BillService;
import lombok.RequiredArgsConstructor;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BillServiceImpl implements BillService {
    private final BillRepo billRepo;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;


    @Override
    public Bill addFacture(@AuthenticationPrincipal UserDetails userDetails, Bill b, int idCmd) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Order o = orderRepo.findById(idCmd).orElse(null);
        String name= o.getUser().getUsername();
        if(!(user1.getUsername()==name)){
            System.out.println("erreur");
        }
        else {
            b.setCode(UUID.randomUUID().toString());
            b.setDateFacture(LocalDate.now());
            b.setOrder(o);
            b.setPrice(calculeFacture(idCmd));
            return billRepo.save(b);
        }
        return b;

    }
    public List<Bill>getBill(){
        return billRepo.findAll();
    }
    public Bill addFactureWithCodePromo(@AuthenticationPrincipal UserDetails userDetails,Bill b, int idCmd, String codeCoupon) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        String c = generateCode();
        Order o = orderRepo.findById(idCmd).orElse(null);
        String name= o.getUser().getUsername();
        if(!(user1.getUsername()==name)){
            System.out.println("erreur");
        }
        else {
            b.setCode(UUID.randomUUID().toString());
            b.setDateFacture(LocalDate.now());
            b.setOrder(o);
            if (codeCoupon.equals(c)) {
                b.setPrice(calculFactureAvecCodePromo(idCmd));
                billRepo.save(b);
            }
            else {
                System.out.println("code incorrect !!");
            }


        }
        return b;
    }

    @Override
    public float calculeFacture(int idCmd) {
        Order o = orderRepo.findById(idCmd).orElse(null);
        float m = 0;
        List<Float> list = o.getProducts().stream().map(product -> product.getPrice()).collect(Collectors.toList());
        for (Float f : list) {
            m = f +m;
        }
        return m;
    }

    @Override
    public List<Bill> getBillsForUser(@AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        return billRepo.findByOrderUser(user1);
    }



    @Override
    public Bill topFacture() {
        Bill bb = new Bill();
        List<Bill> bills = billRepo.findAll();
        double m = bills.stream().map(x -> x.getPrice()).mapToDouble(x -> x).max().orElseThrow(NoSuchElementException::new);
        for (Bill b : bills) {
            if (b.getPrice() == m) {
                bb=b;
            }
        }
        return bb;
    }
    private Coupon getCoupon()  {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date newDate = calendar.getTime();
        Coupon coupon = new Coupon();
        coupon.setCode(generateCode());
        coupon.setDiscountPercentage((float) 0.2);
        coupon.setExpirationDate(newDate) ;
        return coupon;
    }
    @Scheduled(fixedRate = 10000)
    public String generateCode() {
        final Random rnd = new Random(12345);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = rnd.nextInt(chars.length());
            char randomChar = chars.charAt(index);
            sb.append(randomChar);
        }
        String code = sb.toString();
        // System.out.println(code);
        return code;
    }
    public float calculFactureAvecCodePromo(int id)  {
        float m =  calculeFacture(id);
        return applyDiscount(m);
    }

    public float applyDiscount(float amount)  {
        Coupon coupon = getCoupon();
        if (coupon.getExpirationDate().after(new Date())&& coupon.getCode().contains(generateCode())) {
            amount=amount * (1 - coupon.getDiscountPercentage());
        }
        return amount ;
    }
    public User getOrderBill() {
        List<Order> orders = orderRepo.findAll();
        List<Bill> bills = billRepo.findAll();
        Map<Integer, Float> mapOfUserBil = new HashMap<>();
        Map<Integer, List<Order>> mapOfOrder = orders.stream().collect(Collectors.groupingBy(order -> order.getUser().getId()));
        mapOfOrder.entrySet().forEach(entry -> {
            float sum = 0;
            List<Bill> billTest = new ArrayList<>();
            for (var order : entry.getValue()) {
                billTest.add(bills.stream().filter(bill -> bill.getOrder().getId() == order.getId()).findAny().get());
                sum += bills.stream().filter(bill -> bill.getOrder().getId() == order.getId()).findFirst().get().getPrice();
            }
            mapOfUserBil.put(entry.getKey(), sum);
        });
        return getMaxAchat(mapOfUserBil);
    }
    public User getMaxAchat(Map<Integer,Float>map){
        User user = new User();
        if(map != null){
            Integer userId =  map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
            user = userRepo.findById(userId).get();
        }
        return user ;
    }
    public Product offre(){
        int qte=0;
        List<Product>productss=new ArrayList<>();
        Product prod = new Product();
        List<Product>products= produitRec();
        for (Product p :products){
            if (p.getQte()>=qte){
                qte=p.getQte();
            }
        }
        for (Product product : products) {
            if (product.getQte() == qte) {
                productss.add(product);
            }
        }
        float value =  productss.stream().map(x->x.getPrice()).min(Comparator.<Float>naturalOrder()).get();
        for (Product pr : productss){
            if (pr.getPrice()==value){
                prod =pr;
            }
        }
        return prod;
    }
    public List<Product> produitRec(){
        List<Order>orders= orderRepo.findOrdersByUserId(getOrderBill().getId());
        List<Product>products=productRepo.findAll();
        List<Product>products2= new ArrayList<>();
        List<Product>products3= new ArrayList<>();
        for(Order o : orders){
            for (Product pr : o.getProducts()){
                products2.add(pr);
            }
        }
        for(Product p :products){
            for(Product pp : products2){
                if (p.getCategory().equals(pp.getCategory())&&!(products2.contains(p))){
                    products3.add(p);
                }
            }
        }
        List<Product>productList=products3.stream().distinct().collect(Collectors.toList());
        return productList;
    }



}