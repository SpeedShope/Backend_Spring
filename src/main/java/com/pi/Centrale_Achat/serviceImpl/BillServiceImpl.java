package com.pi.Centrale_Achat.serviceImpl;
import com.pi.Centrale_Achat.entities.*;
import com.pi.Centrale_Achat.repositories.UserRepo;
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

    @Override
    public float calculeFacture(int idCmd) {
        Order o = orderRepo.findById(idCmd).orElse(null);
        float m = 0;
        List<Float> list = o.getProducts().stream().map(product -> product.getPrice()).collect(Collectors.toList());
        for (Float f : list) {
            m = f * o.getQte();
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




}
