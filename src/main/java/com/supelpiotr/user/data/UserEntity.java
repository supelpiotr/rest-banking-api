package com.supelpiotr.user.data;

import lombok.*;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 11, unique = true)
    @PESEL
    private String pesel;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private BigDecimal initialPlnBalance = BigDecimal.valueOf(0.0);
    @Column(nullable = false)
    private BigDecimal plnBalance = BigDecimal.valueOf(0.0);
    @Column(nullable = false)
    private BigDecimal eurBalance = BigDecimal.valueOf(0.0);

    private UserRole userRole = UserRole.ROLE_USER;
    private Boolean locked = false;
    private Boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(simpleGrantedAuthority);

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return pesel;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
