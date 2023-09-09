package com.pi.Centrale_Achat.service;

import com.pi.Centrale_Achat.entities.Order;
import com.pi.Centrale_Achat.entities.Product;
import com.pi.Centrale_Achat.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

public interface OrderService {
    Order ajouter(UserDetails userDetails,Order order);
    void delete(UserDetails userDetails,int id);
    List<Order> getOrdersForUser(User user);

    int countCmdBetweenToDate(UserDetails userDetails,Date date1,Date date2);
    Order findOrderByDate(Date d);
}