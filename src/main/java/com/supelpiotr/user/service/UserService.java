package com.supelpiotr.user.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.account.repository.AccountRepository;
import com.supelpiotr.confirmationToken.data.ConfirmationToken;
import com.supelpiotr.confirmationToken.service.ConfirmationTokenService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.repository.UserRepository;
import com.supelpiotr.utils.PeselHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String pesel) throws UsernameNotFoundException {

        final Optional<UserEntity> optionalUser = userRepository.findByPesel(pesel);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else {
            throw new UsernameNotFoundException(MessageFormat.format("User with pesel {0} not found.", pesel));
        }
    }

    public void registerUser(UserEntity user) throws Exception {

        Long birthYear = PeselHelper.getBirthYear(user.getPesel());
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (!PeselHelper.isPeselValid(user.getPesel())){
            throw new Exception("PESEL number invalid");
        } else if (year - birthYear < 18){
            throw new Exception("User is underaged");
        }
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        final UserEntity createdUser = userRepository.save(user);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

    }

    public UserEntity mapToEntity(UserDTO userDTO) {

        userDTO.setEnabled(true);
        return new ModelMapper().map(userDTO, UserEntity.class);

    }

    public void save(UserEntity user) {
        userRepository.save(user);
    }

    public boolean subAccountActive(UserEntity user, AccountType accountType) {
        return this.getUserSubAccount(user, accountType) != null;
    }

    public BigDecimal getCurrencyBalance(UserEntity user, AccountType accountType){
        BaseAccount usdUserAccount = user.getUserAccount().stream()
                .filter(i -> i.getType().equals(accountType))
                .collect(toSingleton());
        return usdUserAccount.getBalance();
    }

    public BigDecimal getUserPln(UserEntity user){
        BaseAccount plnAccount = user.getUserAccount().stream()
                .filter(i -> i.getType().equals(AccountType.PLN))
                .collect(toSingleton());
        return plnAccount.getBalance();
    }

    public BaseAccount getUserSubAccount(UserEntity user, AccountType accountType){
        return user.getUserAccount().stream()
                .filter(i -> i.getType().equals(accountType))
                .collect(toSingleton());
    }

    public BaseAccount getUserPlnAccount(UserEntity user){
        return user.getUserAccount().stream()
                .filter(i -> i.getType().equals(AccountType.PLN))
                .collect(toSingleton());
    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if(list.isEmpty()){
                        return null;
                    } else if(list.size() != 1){
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

}


