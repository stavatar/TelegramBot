package com.example.TelegramBot.Repository;

import com.example.TelegramBot.Enums.StatusCounter;
import com.example.TelegramBot.Model.CounterDTO;
import com.example.TelegramBot.Model.ServiceDTO;
import com.example.TelegramBot.Model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounterRepository extends JpaRepository<CounterDTO, Long> {
    Optional<List<CounterDTO>> findByUser(UserDTO user);
    Optional<List<CounterDTO>> findByServiceDTOAndUser(ServiceDTO service,UserDTO user);
    Optional<List<CounterDTO>> findByUserAndServiceDTO(UserDTO user, ServiceDTO service);
    Optional<List<CounterDTO>> findByUserAndServiceDTOAndStatus(UserDTO user, ServiceDTO service,StatusCounter statusCounter);
    Optional<List<CounterDTO>> findByUserAndStatus(UserDTO user, StatusCounter statusCounter);
}
