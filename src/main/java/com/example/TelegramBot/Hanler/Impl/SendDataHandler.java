package com.example.TelegramBot.Hanler.Impl;

import com.example.TelegramBot.Exception.ExistObjException;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.CounterDTO;
import com.example.TelegramBot.Model.ServiceDTO;
import com.example.TelegramBot.Service.AccountService;
import com.example.TelegramBot.Service.CounterService;
import com.example.TelegramBot.Service.ServiceService;
import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Enums.StatusCounter;
import com.example.TelegramBot.TelegramUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.TelegramBot.TelegramUtil.*;

@Component
public class SendDataHandler implements Handler {
    @Autowired
    private  CounterService counterService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private AccountService accountService;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message, Long chat_id) {
        CounterDTO counter=null;
        SendMessage sendMessage=null;
        StringBuilder stringBuilder=new StringBuilder();
        counter=counterService.getByUserAndStatus(account.getUser(),StatusCounter.WRITE);
        try {
            if (message.equals(SENDDATA)) {
                account.setState(State.SEND_DATA);
                accountService.save(account);
                if (counter!=null){
                    counterService.delete(counter);
                }
                counter = new CounterDTO();
                counter.setUser(account.getUser());
                counter.setStatus(StatusCounter.WRITE);
                List<ServiceDTO> serviceDTOS = serviceService.getAll();
                stringBuilder = new StringBuilder();
                for (ServiceDTO service : serviceDTOS) {
                    stringBuilder.append(service.getName()).append("\n");
                }
                sendMessage = TelegramUtil.createMessageTemplate(account, "Введите название услуги.\n Список возможных услуг:\n" + stringBuilder.toString());
            } else if (message.equals(EXIT)){
                counter = counterService.getEditingCounter(account.getUser());
                if (counter.getServiceDTO() != null) {
                    counterService.delete(counter);
                }
                account.setState(State.WAIT);
                accountService.save(account);
                sendMessage=createMessageTemplateWithAllButton(account,"Меню");
            }else {
                counter = counterService.getEditingCounter(account.getUser());
                if (counter.getServiceDTO() == null) {
                    sendMessage = TelegramUtil.createMessageTemplate(account, "Введите номер счетчика");
                    writeService(counter,message,account);
                }else  if (counter.getNumber()==null) {
                    counter.setNumber(Integer.valueOf(message));
                    sendMessage = TelegramUtil.createMessageTemplate(account, "Введите данные счетчика");
                } else if (counter.getData() == null) {
                    CounterDTO oldCounter = counterService.getByLastDateServiceUser(counter.getUser(), counter.getServiceDTO());
                    if (oldCounter != null && (oldCounter.getId() != counter.getId()))
                        counter.setData(Integer.parseInt(message) - oldCounter.getData());
                    else counter.setData(Integer.parseInt(message));
                    counter.setStatus(StatusCounter.WRITED);
                    counter.setDate(LocalDateTime.now());
                    account.setState(State.WAIT);
                    accountService.save(account);
                    sendMessage = TelegramUtil.createMessageTemplateWithAllButton(account, "Счетчик заполнен");
                }
            }
            counterService.save(counter);
        }catch (NumberFormatException| NoSuchElementException e){
            sendMessage = TelegramUtil.createMessageTemplate(account,"Введены не верные данные, повторите ввод");
        }catch (ExistObjException e){
            sendMessage = TelegramUtil.createMessageTemplateWithAllButton(account,"Счетчик для данной услуги уже был заполнен. Возврат в меню");
        }
        return  List.of(sendMessage);

    }
    public  void writeService(CounterDTO counter,String message,AccountsDTO account) throws ExistObjException {
        ServiceDTO serviceDTO=serviceService.getByName(message);
        if (serviceDTO==null)
            throw  new  NoSuchElementException();
        CounterDTO counterDTO=counterService.getByServiceAndServiceUserAndStatus(serviceDTO,counter.getUser(),StatusCounter.WRITE);
        if (counterDTO!=null&&counter.getId()!=counterDTO.getId()) {
            counterService.delete(counter);
            account.setState(State.WAIT);
            throw new ExistObjException();
        }
        counter.setServiceDTO(serviceDTO);

    }
    @Override
    public List<State> operatedBotState() {
        return List.of(State.SEND_DATA);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(SENDDATA,EXIT);
    }
}
