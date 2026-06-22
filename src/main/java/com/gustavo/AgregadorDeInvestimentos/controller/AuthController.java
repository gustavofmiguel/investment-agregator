package com.gustavo.AgregadorDeInvestimentos.controller;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateUserDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.LoginRequestDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.LoginResponseDto;
import com.gustavo.AgregadorDeInvestimentos.entity.User;
import com.gustavo.AgregadorDeInvestimentos.service.TokenService;
import com.gustavo.AgregadorDeInvestimentos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private UserService userService;


    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid CreateUserDto createUserDto){
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users" + userId)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        var authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.email(), loginRequestDto.password()
        );
        var auth = authenticationManager.authenticate(authToken);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }






}
