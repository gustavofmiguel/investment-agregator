package com.gustavo.AgregadorDeInvestimentos.service;

import com.gustavo.AgregadorDeInvestimentos.controller.CreateUserDto;
import com.gustavo.AgregadorDeInvestimentos.controller.UpdateUserDto;
import com.gustavo.AgregadorDeInvestimentos.entity.User;
import com.gustavo.AgregadorDeInvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
