package com.example.TelegramBot;

import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Repository.UserRepository;
import com.example.TelegramBot.Service.AccountService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
@Component
@Log
public class UpdateReceiver {
    // Storing available handlers in a list (stolen from Miroha)
    @Autowired
    private  List<Handler> handlers;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;
    // Achieving access to user storage

    public UpdateReceiver(List<Handler> handlers) {
        this.handlers = handlers;
    }
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        // try-catch in order to return empty list on unsupported command
        try {
            // Checking if Update is a message with text
            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                final long chatId = message.getFrom().getId();
                AccountsDTO accountsDTO=accountService.getById(chatId);

                return getHandlerByState(accountsDTO==null||message.getText().equals("/start") ? State.START : accountsDTO.getState()).handle(accountsDTO,message.getText(),chatId);
            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final long chatId = callbackQuery.getFrom().getId();
                AccountsDTO accountsDTO=accountService.getById(chatId);
                return getHandlerByCallBackQuery(callbackQuery.getData()).handle(accountsDTO, callbackQuery.getData(),null);
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }


    private Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().stream().anyMatch(st-> st==state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }

}
