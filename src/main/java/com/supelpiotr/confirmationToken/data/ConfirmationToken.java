package com.supelpiotr.confirmationToken.data;

import com.supelpiotr.user.data.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdDate;

    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    public ConfirmationToken(UserEntity user) {
        this.user = user;
        this.createdDate = LocalDate.now();
        this.token = UUID.randomUUID().toString();
    }
}