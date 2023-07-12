package com.pi.Centrale_Achat.serviceImpl;


import com.pi.Centrale_Achat.entities.ERole;
import com.pi.Centrale_Achat.entities.Role;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.RoleeRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.sun.mail.imap.protocol.BASE64MailboxDecoder.decode;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final    UserRepo userRepository;

    private final PasswordEncoder encoder;



    @Override
    public User updateUser(@AuthenticationPrincipal UserDetails userDetails, User updatedUser, String currentPassword) {
        String currentUsername = userDetails.getUsername();
        User user = userRepository.findUserByUsername(currentUsername);
        if (!encoder.matches(decode(currentPassword), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setNom(updatedUser.getNom());
        user.setPrenom(updatedUser.getPrenom());
        user.setEmail(updatedUser.getEmail());
        user.setAddress(updatedUser.getAddress());
        user.setNumTel(user.getNumTel());
        User savedUser = userRepository.save(user);
        return savedUser;
    }





    @Override
    public User getMyProfile(UserDetails userDetails) {
        String cuurentname = userDetails.getUsername();
        User user = userRepository.findUserByUsername(cuurentname);
        return user;
    }

    public Map<ERole, Integer> getstatistique() {
        Map<ERole, Integer> adminstats = new HashMap<>();
        adminstats.put(ERole.ROLE_CUSTOMER, 0);
        adminstats.put(ERole.ROLE_SUPPLIER, 0);
        adminstats.put(ERole.ROLE_OPERATOR, 0);
        adminstats.put(ERole.ROLE_DELIVERY, 0);


        List<User> users = userRepository.findAll();
        for (User user : users) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getName().equals(ERole.ROLE_OPERATOR)) {
                    adminstats.put(ERole.ROLE_OPERATOR, adminstats.get(ERole.ROLE_OPERATOR) + 1);
                } else if (role.getName().equals(ERole.ROLE_CUSTOMER)) {
                    adminstats.put(ERole.ROLE_CUSTOMER, adminstats.get(ERole.ROLE_CUSTOMER) + 1);
                }
                else if (role.getName().equals(ERole.ROLE_DELIVERY)) {
                    adminstats.put(ERole.ROLE_DELIVERY, adminstats.get(ERole.ROLE_DELIVERY) + 1);
                }
                else if (role.getName().equals(ERole.ROLE_SUPPLIER)) {
                    adminstats.put(ERole.ROLE_SUPPLIER, adminstats.get(ERole.ROLE_SUPPLIER) + 1);
                }
            }
        }

        return adminstats;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }





	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username); 
	}





	@Override
	public User ValidateAccount(String username) {
		 User u= this.findUserByUsername(username); 
			if(u!=null) {
				 u.setVerified(true);
			}
     return userRepository.save(u);
		
		 
	}

}
