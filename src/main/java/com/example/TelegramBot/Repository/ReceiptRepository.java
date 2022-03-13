package com.example.TelegramBot.Repository;

import com.example.TelegramBot.Model.ReceiptDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<ReceiptDTO, Long> {

}
