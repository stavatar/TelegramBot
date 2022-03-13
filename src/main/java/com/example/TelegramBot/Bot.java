package com.example.TelegramBot;

import com.example.TelegramBot.Model.ColumnReceiptDTO;
import com.example.TelegramBot.Model.CounterDTO;
import com.example.TelegramBot.Model.ReceiptDTO;
import com.example.TelegramBot.Model.UserDTO;
import com.example.TelegramBot.Repository.ColumnReceiptRepository;
import com.example.TelegramBot.Repository.ProviderRepository;
import com.example.TelegramBot.Repository.ReceiptRepository;
import com.example.TelegramBot.Service.CounterService;
import com.example.TelegramBot.Service.ServiceService;
import com.example.TelegramBot.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private  UpdateReceiver updateReceiver;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ColumnReceiptRepository columnReceiptRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private ProviderRepository providerRepository;

    // Main bot method that is inherited from TelegramLongPollingBot.class
    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);
        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    try {
                        execute((SendMessage) response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else if(response instanceof SendDocument){
                    try {
                        execute((SendDocument) response);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    // Simple checking for Telegram API Exceptions
    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }


    @Override
    public String getBotUsername() {
        return "Lab";
    }

    @Override
    public String getBotToken() {
        return "5157370533:AAH0n23MOclU16EHpwS9S7-01UYQYxWwvyM";
    }



}
