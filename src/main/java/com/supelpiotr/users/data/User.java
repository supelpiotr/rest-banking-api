package com.supelpiotr.users.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 5313493413859894403L;

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 120, unique = true)
    private String pesel;
    @Column(nullable = false)
    private String encryptedPassword;

}
