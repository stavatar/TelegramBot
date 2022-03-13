package com.example.TelegramBot.Repository;

import com.example.TelegramBot.Model.ColumnReceiptDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnReceiptRepository extends JpaRepository<ColumnReceiptDTO, Long> {
}
