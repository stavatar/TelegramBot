package com.example.TelegramBot.Hanler.Impl;

import com.example.TelegramBot.Enums.StateAuth;
import com.example.TelegramBot.Enums.StatusCounter;
import com.example.TelegramBot.Enums.StatusPaid;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.UserDTO;
import com.example.TelegramBot.Service.AccountService;
import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Service.UserService;
import com.example.TelegramBot.TelegramUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.TelegramBot.TelegramUtil.*;

@Component
public class RegistrationHandler implements Handler {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message,Long chat_id) {
        SendMessage sendMessage=null;
        try{
            if(!message.equals(REGISTRATION)){
                if (message.equals(EXIT)){
                    UserDTO user=account.getUser();
                    if (user!=null)
                        userService.delete(user);

                    account.setState(State.WAIT);
                    sendMessage=createMessageTemplateWithStartButttons(account);
                }else if (account.getUser()==null){
                   UserDTO newUser=new UserDTO();
                   newUser.setLogin(message);
                   account.setUser(newUser);
                   sendMessage=TelegramUtil.createMessageTemplateWithExitButton(account,"Введите пароль");
               }else if(account.getUser().getPassword()==null){
                   account.getUser().setPassword(message);
                   sendMessage=TelegramUtil.createMessageTemplateWithExitButton(account,"Введите размер квартиры");
               }else if(account.getUser().getSizeRoom()==null){
                    account.getUser().setSizeRoom(Integer.valueOf(message));
                    sendMessage=TelegramUtil.createMessageTemplateWithExitButton(account,"Введите имя пользователя");
                }else if(account.getUser().getName()==null){
                   account.getUser().setName(message);
                   account.getUser().setStatus(StatusPaid.NOT_PAID);
                   account.setStateAuth(StateAuth.AUTH);
                   String text="Данные введены" +
                           "\nЛогин="+ account.getUser().getLogin()+
                           "\nИмя пользователя"+ account.getUser().getName()+
                           "\nПароль"+ account.getUser().getPassword()+
                            "\nРазмерКвартиры"+ account.getUser().getSizeRoom();
                   sendMessage=createMessageTemplateWithAllButton(account,text);
                   account.setState(State.WAIT);
               }else {
                    sendMessage=createMessageTemplateWithAllButton(account,"ТЕСТ");
                    account.setState(State.WAIT);
               }

        }else {
            account.setState(State.REGISTRATION);
            sendMessage=TelegramUtil.createMessageTemplateWithExitButton(account,"Начата регистрация. Введите логин");
        }
        accountService.save(account);
    }catch (NumberFormatException| NoSuchElementException e){
        sendMessage = TelegramUtil.createMessageTemplate(account,"Введены не верные данные, повторите ввод");
    }
        return List.of(sendMessage);
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.REGISTRATION);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(REGISTRATION,EXIT);
    }
}
