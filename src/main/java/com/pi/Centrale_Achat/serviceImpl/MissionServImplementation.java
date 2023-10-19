package com.pi.Centrale_Achat.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi.Centrale_Achat.entities.Mission;
import com.pi.Centrale_Achat.entities.Order;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.MissionRepository;
import com.pi.Centrale_Achat.repositories.OrderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.missionService;

@Service
public class MissionServImplementation implements missionService {

	 @Autowired 
	 UserRepo userrep; 
	  @Autowired
	  OrderRepo orderrep;
	  @Autowired 
	  MissionRepository msrep; 
	@Override
	public Mission AffectOrder(int userId, int orderID) {
		 Mission m = new Mission(); 	  

		 User u = userrep.findById(userId).get();
		 if (u!=null ) {
			 Order o = orderrep.findById(orderID).get(); 
			 
			 if(o!=null) {
				 m.setOrder(o);
				 m.setUser(u);
				 m.setStatus(false);
				 return msrep.save(m);

			 }
		 }
		 return msrep.save(m);

	}
	@Override
	public List<Mission> getAllMissions() {
		return msrep.findAll();
	}
	@Override
	public Mission getMissionByID(int id) {
		return msrep.findById(id).get();
	}

	
}
