package com.pi.Centrale_Achat.serviceImpl;
import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.Order;
import com.pi.Centrale_Achat.entities.Product;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.BillRepo;
import com.pi.Centrale_Achat.repositories.OrderRepo;
import com.pi.Centrale_Achat.repositories.ProductRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orederRepo;

    private final BillRepo billRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;


    public Order ajouter(@AuthenticationPrincipal UserDetails userDetails, Order order ) {
        System.out.println("==================>" + order);
        String currentUser = userDetails.getUsername();
        User user1 = userRepo.findUserByUsername(currentUser);


        order.setCode(UUID.randomUUID().toString());
        order.setUser(user1);
        Bill bill = order.getBill();
        order = orederRepo.save(order);
        bill.setOrder(order);
        bill.setDateFacture(LocalDate.now());
        bill = billRepo.save(bill);
        order.setBill(bill);
        return order;
    }

    @Override
    public void delete(UserDetails userDetails, int id) {

    }


    public void delete(int id) {

        Order order = orederRepo.findById(id).orElse(null);

            orederRepo.deleteById(id);

            System.out.println("erreur");
        }




    @Override
    public List<Order> getOrdersForUser(User user) {
        return null;
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
    public List<Order> getOrdersForUser() {
        return orederRepo.findAll();
    }
    public User getUser(int id ){
        Order o = orederRepo.findById(id).get();
        return o.getUser();
    }
    public List<Product> get(int id ){
        Order o = orederRepo.findById(id).get();
        return o.getProducts();
    }









}