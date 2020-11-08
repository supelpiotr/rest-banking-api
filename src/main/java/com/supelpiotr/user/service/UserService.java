package com.supelpiotr.user.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.account.service.AccountService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.repository.UserRepository;
import com.supelpiotr.utils.PeselHelper;
import com.supelpiotr.utils.exceptions.RegistrationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String pesel) {

        final Optional<UserEntity> optionalUser = userRepository.findByPesel(pesel);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else {
            throw new UsernameNotFoundException(MessageFormat.format("User with pesel {0} not found.", pesel));
        }
    }

    public void registerUser(UserDTO userDTO) throws RegistrationException {

        if (userRepository.findByPesel(userDTO.getPesel()).isPresent()) {
            throw new RegistrationException("User account is already created. Please login");
        } else {
            UserEntity user = mapToEntity(userDTO);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            Long birthYear = PeselHelper.getBirthYear(user.getPesel());
            if (!PeselHelper.isPeselValid(user.getPesel())){
                throw new RegistrationException("PESEL number invalid");
            } else if (birthYear != null && year - birthYear < 18){
                throw new RegistrationException("User is underaged");
            }
            final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

            accountService.prepareDefaultAccount(user);
            user.setPassword(encryptedPassword);
            userRepository.save(user);
        }
        
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

    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
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

    public void saveOnSturtup(UserEntity userEntity) {
        Optional<UserEntity> initialUser = userRepository.findByPesel("87061612345");
        accountService.prepareDefaultAccount(userEntity);

        if (!initialUser.isPresent()) {
            userRepository.save(userEntity);
        } else {
            userRepository.delete(initialUser.get());
            userRepository.save(userEntity);
        }
    }

    public UserDTO mapToDTO(String name) {
        UserDetails user = loadUserByUsername(name);
        UserDTO userDTO = new UserDTO();
        modelMapper.map(user,userDTO);
        return userDTO;
    }
}


