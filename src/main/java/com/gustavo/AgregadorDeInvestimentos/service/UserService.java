package com.gustavo.AgregadorDeInvestimentos.service;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.AccountResponseDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateAccountDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateUserDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.UpdateUserDto;
import com.gustavo.AgregadorDeInvestimentos.entity.Account;
import com.gustavo.AgregadorDeInvestimentos.entity.BillingAdress;
import com.gustavo.AgregadorDeInvestimentos.entity.User;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.BillingAdressRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAdressRepository billingAdressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAdressRepository billingAdressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAdressRepository = billingAdressRepository;
    }

    public UUID crateUser(CreateUserDto createUserDto){
        // DTO -> ENTITY
        var entity = new User();
        entity.setUsername(createUserDto.username());
        entity.setEmail(createUserDto.email());
        entity.setPassword(createUserDto.password());

        var userSaved = userRepository.save(entity);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId){
       return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto){
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()){
            var user = userEntity.get();
            if (updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }
            if (updateUserDto.password() != null){
                user.setPassword(updateUserDto.password());
            }
            userRepository.save(user);
        } else {

        }
    }

    public void deleteById(String userId){
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if (userExists){
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDto createAccountDto) {

        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Deixe o ID nulo — o @GeneratedValue cuida disso
        var account = new Account();
        account.setUser(user);
        account.setDescription(createAccountDto.description());

        var accountCreated = accountRepository.save(account);

        var billingAdress = new BillingAdress(
                accountCreated.getAccountId(),
                accountCreated,
                        createAccountDto.street(),
                        createAccountDto.number()
                );

        billingAdressRepository.save(billingAdress);
    }

    public List<AccountResponseDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
        return user.getAccounts()
                .stream().map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();
    }
}
