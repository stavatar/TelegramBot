package com.example.TelegramBot.Repository;

import com.example.TelegramBot.Model.UserDTO;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<UserDTO, Long> {
}