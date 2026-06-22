package com.gustavo.AgregadorDeInvestimentos.controller;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.*;
import com.gustavo.AgregadorDeInvestimentos.entity.User;
import com.gustavo.AgregadorDeInvestimentos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;


/*
* createUser virou responsabilidade do /v1/auth/register
* vou deixar os dois por enquanto para não quebrar nada ainda
* Remover depois que tudo estiver testado
* */

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserDto createUserDto){
        var userId = userService.createUser(createUserDto);

        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID userId){
            return ResponseEntity.ok(userService.getUserById(userId));

    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> listUsers(
            @PageableDefault(size = 10, sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.listUsers(pageable));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable UUID userId,
                                               @RequestBody @Valid UpdateUserDto updateUserDto){
        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID userId ){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> createAccount(@PathVariable UUID userId,
                                           @RequestBody @Valid CreateAccountDto createAccountDto){
        userService.createAccount(userId, createAccountDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> listAccounts(@PathVariable UUID userId){

        var accounts = userService.listAccounts(userId);

        return ResponseEntity.ok(accounts);
    }
}
