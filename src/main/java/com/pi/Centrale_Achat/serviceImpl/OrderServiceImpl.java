package com.pi.Centrale_Achat.serviceImpl;
import com.pi.Centrale_Achat.entities.Order;
import com.pi.Centrale_Achat.entities.Product;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.OrderRepo;
import com.pi.Centrale_Achat.repositories.ProductRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orederRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;


    public Order ajouter(@AuthenticationPrincipal UserDetails userDetails, Order order , int idP) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Product p = productRepo.findById(idP).orElse(null);
        if (order.getProducts() == null) {
            if (p.getQte() > order.getQte()) {
                p.setQte(p.getQte() - order.getQte());
                productRepo.save(p);
                List<Product> products = new ArrayList<>();
                products.add(p);
                order.setProducts(products);
                order.setUser(user1);
                orederRepo.save(order);
            } else {
                System.out.println("invalid qte");
            }
        } else {
            if (p.getQte() > order.getQte()) {
                p.setQte(p.getQte() - order.getQte());
                order.getProducts().add(p);
                order.setUser(user1);
                orederRepo.save(order);
            } else {
                System.out.println("invalid qte");
            }
        }
        return order;

    }

    @Override
    public void delete(@AuthenticationPrincipal UserDetails userDetails,int id) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        Order order = orederRepo.findById(id).orElse(null);
        if(order.getUser().getId()==user1.getId()){
            orederRepo.deleteById(id);
        }
        else {
            System.out.println("erreur");
        }


    }

        @Override
    public int countCmdBetweenToDate(@AuthenticationPrincipal UserDetails userDetails,Date date1, Date date2) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);
        int nbrCmd=0;
        List<Order>orders = orederRepo.findAll();
        for (Order o : orders){
            if (o.getUser().getId()==user1.getId()){
                nbrCmd= orederRepo.countOrdersByDatCmdBetween(date1,date2);
            }
        }
        return nbrCmd;
    }
    @Override
    public Order findOrderByDate(Date d) {
        return orederRepo.findOrderByDatCmd(d);
    }

    @Override
    public List<Order> getOrdersForUser(User user) {
        return orederRepo.findByUser(user);
    }









}




