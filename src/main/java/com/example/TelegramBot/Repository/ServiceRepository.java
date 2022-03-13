package com.example.TelegramBot.Repository;

import com.example.TelegramBot.Model.ServiceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceDTO, Long> {
    Optional<ServiceDTO> findByName(String name);

}