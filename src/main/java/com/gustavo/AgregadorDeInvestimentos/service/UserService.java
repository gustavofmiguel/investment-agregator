package com.gustavo.AgregadorDeInvestimentos.service;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.*;
import com.gustavo.AgregadorDeInvestimentos.entity.Account;
import com.gustavo.AgregadorDeInvestimentos.entity.BillingAddress;
import com.gustavo.AgregadorDeInvestimentos.entity.User;
import com.gustavo.AgregadorDeInvestimentos.exception.ResourceNotFoundException;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.BillingAddressRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UUID createUser(CreateUserDto createUserDto){
        // DTO -> ENTITY
        var entity = new User();
        entity.setUsername(createUserDto.username());
        entity.setEmail(createUserDto.email());
        entity.setPassword(passwordEncoder.encode(createUserDto.password()));

        var userSaved = userRepository.save(entity);
        return userSaved.getUserId();
    }

    public UserResponseDto getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(UserResponseDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userId));
    }

    public Page<UserResponseDto> listUsers(Pageable pageable){
        return userRepository.findAll(pageable)
                .map(UserResponseDto::from);
    }

    @Transactional
    public void updateUserById(UUID userId, UpdateUserDto updateUserDto){
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userId));

        if (updateUserDto.username() != null){
            user.setUsername(updateUserDto.username());
        }
        if (updateUserDto.password() != null){
            user.setPassword(updateUserDto.password());
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(UUID userId){
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("Usuário não encontrado: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void createAccount(UUID userId, CreateAccountDto createAccountDto) {

        var user = userRepository.findById((userId))
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userId));

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
                        new ResourceNotFoundException("Usuário não encontrado: " + userId));
        return user.getAccounts()
                .stream().map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();
    }
}
