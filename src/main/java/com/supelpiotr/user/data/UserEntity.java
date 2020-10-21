package com.supelpiotr.user.data;

import com.supelpiotr.account.data.BaseAccount;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 11, unique = true)
    //@PESEL
    private String pesel;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private BigDecimal initialPlnBalance = BigDecimal.valueOf(0);
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaseAccount> userAccount;

    private UserRole userRole = UserRole.ROLE_USER;
    private Boolean locked = false;
    private Boolean enabled = false;

    public UserEntity(String firstName, String lastName, String pesel, String password, BigDecimal initialPlnBalance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.password = password;
        this.initialPlnBalance = initialPlnBalance;
    }

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

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

}
