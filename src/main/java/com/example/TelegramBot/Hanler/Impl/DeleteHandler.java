package com.example.TelegramBot.Hanler.Impl;

import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Enums.StatusCounter;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.CounterDTO;
import com.example.TelegramBot.Service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;
import static com.example.TelegramBot.TelegramUtil.*;
import static com.example.TelegramBot.TelegramUtil.*;
import static com.example.TelegramBot.TelegramUtil.GETALLSERVICE;
@Component
public class DeleteHandler implements Handler {
    @Autowired
    private CounterService counterService;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message, Long chat_id) {
        switch (message){
            case DELETEALLCOUNTER:
                List<CounterDTO> counters=counterService.getAllByUserAndStatus(account.getUser(), StatusCounter.WRITED);
                for (CounterDTO counter: counters)
                    counterService.delete(counter);
                break;
        }
        return List.of(createMessageTemplateWithAllButton(account,"Все счетчики удалены"));
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.DELETE);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(DELETEALLCOUNTER);
    }
}
