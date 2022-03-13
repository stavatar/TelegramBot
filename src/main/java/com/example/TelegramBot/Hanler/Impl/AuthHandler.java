package com.example.TelegramBot.Hanler.Impl;

import com.example.TelegramBot.Enums.StateAuth;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.UserDTO;
import com.example.TelegramBot.Service.AccountService;
import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.example.TelegramBot.TelegramUtil.AUTHORIZATION;
import static com.example .TelegramBot.TelegramUtil.*;
@Component
public class AuthHandler implements Handler {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message, Long chat_id) {
        SendMessage sendMessage=null;
        if (message.equals(EXIT)){
            account.setState(State.WAIT);
            account.setUser(null);
            account.setStateAuth(StateAuth.NOT_AUTH);
            accountService.save(account);
            sendMessage=createMessageTemplateWithStartButttons(account);
        }
        if (message.equals(AUTHORIZATION)){
           account.setState(State.AUTHORIZATION);
        }
        switch (account.getStateAuth()){
            case AUTH:
                sendMessage=createMessageTemplate(account,"Вы уже вошли в аккаунт");
                break;
            case NOT_AUTH:
                sendMessage=createMessageTemplate(account,"Введите логин");
                account.setStateAuth(StateAuth.ENTER_LOGIN);
                accountService.save(account);
                break;
            case ENTER_LOGIN:
                List<UserDTO> users=userService.getAll();
                Optional<UserDTO> userOpt= users.stream().filter(user-> user.getLogin().equals(message)).findAny();
                if(userOpt.isPresent()){
                    account.setStateAuth(StateAuth.ENTER_PASSWORD);
                    account.setUser(userOpt.get());
                    accountService.save(account);
                    sendMessage=createMessageTemplate(account,"Введите пароль");
                }else
                    sendMessage=createMessageTemplate(account,"Не найден пользователь с таким логином");
                break;
            case ENTER_PASSWORD:
                if(account.getUser().getPassword().equals(message)){
                account.setStateAuth(StateAuth.AUTH);
                accountService.save(account);
                sendMessage=createMessageTemplateWithAllButton(account,"Вы вошли!");
            }else {
                    sendMessage = createMessageTemplate(account, "Не верный пароль.Повторите ввод.");
                }
            break;
        }
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.AUTHORIZATION);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(AUTHORIZATION);
    }
}
