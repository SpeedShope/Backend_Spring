package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Order;
import com.pi.Centrale_Achat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Integer> {
    int countOrdersByDatCmdBetween(Date d1, Date d2);
    Order findOrderByDatCmd(Date d);
    List<Order>findByUser(User user);
    List<Order> findOrdersByUserId(int id);
}