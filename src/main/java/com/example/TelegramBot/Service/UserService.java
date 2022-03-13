package com.example.TelegramBot.Service;

import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.UserDTO;
import com.example.TelegramBot.Repository.UserRepository;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private   UserRepository userRepository;

    public List<UserDTO> getAll(){
        return userRepository.findAll();
    }
    public void  delete(UserDTO user){
        userRepository.deleteById(user.getId());
    }
    public UserDTO save(UserDTO user){
       return   userRepository.save(user);
    }
    public UserDTO getUserById(long id){
        Optional<UserDTO> user=userRepository.findById(id);
        return user.orElse(null);
    }
}
