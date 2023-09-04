package com.pi.Centrale_Achat.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String nom;
    String prenom;
    String username;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateNaissance;
    String password;
    String email;
    String verificationCode;
    boolean verified=false;
    String address;
    String image;
    String numTel;
    Float score;
    private String resetpasswordcode;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST})
    List<Order>orders;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST})
    List<Product>products;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST})
    List<Category>categories;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST})
    List<Tender>tenders;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST})
    List<RequestClaim>requestClaims;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    List<OperatorScore>operatorScores;
    @OneToMany(mappedBy = "userComment",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JsonIgnore
    List<Comment>comments;
    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<Contract> contracts ; 
    
    public User(String nom,String prenom,String username,String address, String numTel,String email,String password,Date dateNaissance){
        this.nom = nom;
        this.prenom =prenom ;
        this.username = username;
        this.address=address;
        this.numTel=numTel;
        this.email = email;
        this.password = password;
        this.dateNaissance = dateNaissance ;
    }

    public User(String nom, String prenom, String username, Date dateNaissance, String password, String email, String address, String numTel) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.dateNaissance = dateNaissance;
        this.password = password;
        this.email = email;
        this.address = address;
        this.numTel = numTel;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String nom,String prenom,String username,String email,String password,Date dateNaissance){
        this.nom = nom;
        this.prenom =prenom ;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateNaissance = dateNaissance ;
    }



    @Override
    public String toString() {
        return "Client [id_user=" + id + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance="
                + dateNaissance + ", email=" + email + "]";
    }

    public String getResetpasswordcode() {
        return resetpasswordcode;
    }

    public void setResetpasswordcode(String resetpasswordcode) {
        this.resetpasswordcode = resetpasswordcode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }


    public int getId() {
        return id;
    }


}
