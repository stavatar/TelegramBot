package com.example.TelegramBot.Hanler.Impl;

import com.example.TelegramBot.Enums.StateAuth;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Service.AccountService;
import com.example.TelegramBot.Enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

import static com.example.TelegramBot.TelegramUtil.*;
@Component
public class StartHandler implements Handler {

    @Autowired
    private AccountService accountService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message,Long chat_id) {
            //List<PartialBotApiMethod<? extends Serializable>> result = new ArrayList<>();
        //Если стартовал впервые
            if (account==null) {
                account=new AccountsDTO();
                account.setState(State.START);
                account.setStateAuth(StateAuth.NOT_AUTH);
                account.setId(chat_id);
                accountService.save(account);
                SendMessage sendMessage=createMessageTemplateWithStartButttons(account);
                return List.of(sendMessage);
            }else {
                SendMessage sendMessage=createMessageTemplateWithAllButton(account,"Здравствуйте");
                return List.of(sendMessage);
            }
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.START);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of();
    }
}
