package com.example.TelegramBot.Hanler;

import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Enums.State;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface Handler {
    // основной метод, который будет обрабатывать действия пользователя
    List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message,Long chat_id);


    // метод, который позволяет узнать, можем ли мы обработать текущий State у пользователя
    List<State> operatedBotState();
    // метод, который позволяет узнать, какие команды CallBackQuery мы можем обработать в этом классе
    List<String> operatedCallBackQuery();
}
