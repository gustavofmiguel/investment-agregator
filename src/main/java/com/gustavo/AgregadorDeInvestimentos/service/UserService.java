package com.gustavo.AgregadorDeInvestimentos.service;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.AccountResponseDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateAccountDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateUserDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.UpdateUserDto;
import com.gustavo.AgregadorDeInvestimentos.entity.Account;
import com.gustavo.AgregadorDeInvestimentos.entity.BillingAddress;
import com.gustavo.AgregadorDeInvestimentos.entity.User;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.BillingAddressRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDto createUserDto){
        // DTO -> ENTITY
        var entity = new User();
        entity.setUsername(createUserDto.username());
        entity.setEmail(createUserDto.email());
        entity.setPassword(createUserDto.password());

        var userSaved = userRepository.save(entity);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(UUID userId){
       return userRepository.findById(userId);
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }

    public void updateUserById(UUID userId, UpdateUserDto updateUserDto){
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (updateUserDto.username() != null){
            user.setUsername(updateUserDto.username());
        }
        if (updateUserDto.password() != null){
            user.setPassword(updateUserDto.password());
        }
        userRepository.save(user);
    }

    public void deleteById(UUID userId){
        if (!userRepository.existsById(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }

    public void createAccount(UUID userId, CreateAccountDto createAccountDto) {

        var user = userRepository.findById((userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Deixe o ID nulo — o @GeneratedValue cuida disso
        var account = new Account();
        account.setUser(user);
        account.setDescription(createAccountDto.description());

        var accountCreated = accountRepository.save(account);

        var billingAdress = new BillingAddress(
                accountCreated.getAccountId(),
                accountCreated,
                        createAccountDto.street(),
                        createAccountDto.number()
                );

        billingAddressRepository.save(billingAdress);
    }

    public List<AccountResponseDto> listAccounts(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
        return user.getAccounts()
                .stream().map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();
    }
}
