package com.example.TelegramBot.Repository;

import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsDTO, Long> {
        Optional<List<AccountsDTO>> getAccountsDTOSByUser(UserDTO user);
}
