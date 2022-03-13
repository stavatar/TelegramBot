package com.example.TelegramBot.Repository;

import com.example.TelegramBot.Model.ProviderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderDTO, Long> {
}
