package com.pi.Centrale_Achat.controller;

import com.pi.Centrale_Achat.entities.ERole;
import com.pi.Centrale_Achat.entities.Role;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.payload.request.ForgotPassRequest;
import com.pi.Centrale_Achat.payload.request.LoginRequest;
import com.pi.Centrale_Achat.payload.request.ResetPassRequest;
import com.pi.Centrale_Achat.payload.request.SignupRequest;
import com.pi.Centrale_Achat.payload.response.JwtResponse;
import com.pi.Centrale_Achat.payload.response.MessageResponse;
import com.pi.Centrale_Achat.repositories.RoleeRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.security.jwt.JwtUtils;
import com.pi.Centrale_Achat.serviceImpl.UserDetailsImpl;
import com.pi.Centrale_Achat.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200" , "http://localhost:4000"}, maxAge = 3600, allowCredentials="true")
public class AuthController {

    private final    AuthenticationManager authenticationManager;

    private final    UserRepo userRepository;

    private final    RoleeRepo roleRepository;

    private final    PasswordEncoder encoder;

    private final    JwtUtils jwtUtils;

    private final     JavaMailSender javaMailSender;



    private final UserDetailsService userDetailsService;

    private final Map<String, Integer> loginAttempts = new HashMap<>();


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = null;
        try {
            user = new User(signUpRequest.getNom(), signUpRequest.getPrenom(), signUpRequest.getUsername(),
                    new SimpleDateFormat("yyyy-MM-dd").parse(signUpRequest.getDateNaissance()),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getEmail(), signUpRequest.getAddress(),signUpRequest.getNumTel()
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String verificationCode = UUID.randomUUID().toString();

        List<String> strRoles = signUpRequest.getRole();
        System.out.println("Roles received "+strRoles);
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role clientrole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_CUSTOMER)));
            roles.add(clientrole);
        } else {
            for (String roleName : strRoles) {
                if (ERole.ROLE_OPERATOR.name().equals(roleName)) {

                    Role operateurrole = roleRepository.findByName(ERole.ROLE_OPERATOR)
                            .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_OPERATOR)));
                    roles.add(operateurrole);

                } if (ERole.ROLE_ADMIN.name().equals(roleName)) {
                    Role admin = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_ADMIN)));
                    roles.add(admin);}
                else if (ERole.ROLE_CUSTOMER.name().equals(roleName)) {
                    Role clientrole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                            .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_CUSTOMER)));
                    roles.add(clientrole);
                } else if (ERole.ROLE_SUPPLIER.name().equals(roleName)) {
                    Role fournisseurrole = roleRepository.findByName(ERole.ROLE_SUPPLIER)
                            .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_SUPPLIER)));
                    roles.add(fournisseurrole);
                }
                else if (ERole.ROLE_DELIVERY.name().equals(roleName)) {
                    Role livreurrole = roleRepository.findByName(ERole.ROLE_DELIVERY)
                            .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_DELIVERY)));
                    roles.add(livreurrole);
                }
            }
        }

        user.setRoles(roles);

        user.setVerificationCode(verificationCode);

        userRepository.save(user);
        String appUrl = "http://localhost:9090/api/auth/SignUp";
        String message = "<html><body><p>Bonjour " + signUpRequest.getNom() + ",</p>" +
                "<p>Votre inscription sur notre site a été effectuée avec succès.</p>" +
                "<p>Veuillez cliquer sur le lien suivant pour vérifier votre compte :</p>" +
                "<table border='1'><tr><td>URL</td><td>" + appUrl + "/verify?code=" + verificationCode + "</td></tr>" +
                "<tr><td>Code de vérification</td><td>" + verificationCode + "</td></tr></table>" +
                "<p>Merci de votre confiance.</p></body></html>";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(signUpRequest.getEmail());
        mailMessage.setSubject("Inscription réussie");
        mailMessage.setText("Un email de confirmation vous a été envoyé à l'adresse " + signUpRequest.getEmail() + ". Veuillez suivre les instructions pour vérifier votre compte.");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(mailMessage.getTo());
        helper.setSubject(mailMessage.getSubject());
        helper.setText(message, true);

        javaMailSender.send(mimeMessage);



        String responseMessage = "Un email de confirmation a été envoyé à l'adresse " + signUpRequest.getEmail() + ". " +
                "Veuillez suivre les instructions pour vérifier votre compte.";

        return ResponseEntity.ok(new MessageResponse(responseMessage));

    }









    @GetMapping("/SignUp/verify")
    public ResponseEntity<?> verifySignUp(@RequestParam String code) {
        User user = userRepository.findByVerificationCode(code);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Code de vérification invalide."));
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Votre compte a été vérifié avec succès."));
    }





    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody LoginRequest loginRequest) throws MessagingException {
        String username = loginRequest.getUsername();
        Integer attempts = loginAttempts.getOrDefault(username, 0);

        if (attempts >= 3) {
            User user = userRepository.findUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Too many failed login attempts!"));
            }
            String email = user.getEmail();
            if (StringUtils.isEmpty(email)) {
                System.out.println("Alert: Too many failed login attempts for user " + username + ", but no email address available to send alert to.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Too many failed login attempts!"));
            }
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Tentatives de connexion infructueuses");
            mailMessage.setText("Nous avons détecté plusieurs tentatives de connexion infructueuses sur votre compte. Veuillez vérifier que votre mot de passe est sécurisé et que personne d'autre n'a accès à votre compte.");
            javaMailSender.send(mailMessage);
            System.out.println("Alert: Too many failed login attempts for user " + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Too many failed login attempts! Please check your email for further instructions."));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            loginAttempts.put(username, attempts + 1);
            if (attempts + 1 >= 3) {
                User user = userRepository.findUserByUsername(username);
                String verificationCode = UUID.randomUUID().toString();

                user.setVerificationCode(verificationCode);
                user.setVerified(false);
                userRepository.save(user);

                if (user == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password!"));
                }
                String email = user.getEmail();
                if (StringUtils.isEmpty(email)) {
                    System.out.println("Alert: Too many failed login attempts for user " + username + ", but no email address available to send alert to.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Too many failed login attempts!"));
                }
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("Tentatives de connexion infructueuses");
                mailMessage.setText("Nous avons détecté plusieurs tentatives de connexion infructueuses sur votre compte.Veuillez vérifier Mr/Mm "+ username  +" que votre mot de passe est sécurisé et que personne d'autre n'a accès à votre compte.");
                javaMailSender.send(mailMessage);
                System.out.println("Alert: Too many failed login attempts for user " + username);
                loginAttempts.put(username,0);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Too many failed login attempts! Please check your email for further instructions."));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password!"));
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        if (!userDetails.isVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Error: Account not verified!"));
        }
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        ResponseCookie jwt = jwtUtils.generateJwtCookie(userDetails);
        if (!encoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid password!"));
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwt.toString())
                .body(new JwtResponse(jwt.toString(),
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getProfile(),
                        roles));
    }



    @PostMapping("/signout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassRequest forgotPassRequest) {
        if (!userRepository.existsByEmail(forgotPassRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("il n'existe aucun utilisateur avec cet email, vérifiez vos données"));
        } else {
            User user = userRepository.findUserByEmail(forgotPassRequest.getEmail());

            String code = UUID.randomUUID().toString();

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(forgotPassRequest.getEmail());
            mailMessage.setText(" Bonjour vous trouvez ci-joint un code de vérification pour réinitialiser votre mot de passe : "+ code);
            javaMailSender.send(mailMessage);

            user.setResetpasswordcode(code);
            user.setVerified(false);

            userRepository.save(user);


            return ResponseEntity.ok().body(new MessageResponse("Verification code sent successfully!"));
        }}



    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassRequest resetPassRequest) {
        if (!userRepository.existsByEmail(resetPassRequest.getEmail()) ){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("il n'existe aucun utilisateur avec cet email, vérifiez vos données"));
        }
        User user = userRepository.findUserByEmail(resetPassRequest.getEmail());


        if (!user.getResetpasswordcode().equals(resetPassRequest.getResetpasswordcode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid verification code!"));
        }

        user.setPassword(encoder.encode(resetPassRequest.getPassword()));
        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok().body(new MessageResponse("Password reset successful!"));
    }






}

