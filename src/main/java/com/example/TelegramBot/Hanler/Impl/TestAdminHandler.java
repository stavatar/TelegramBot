package com.example.TelegramBot.Hanler.Impl;

import com.example.TelegramBot.Bot;
import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Enums.StatusCounter;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.*;
import com.example.TelegramBot.Repository.ColumnReceiptRepository;
import com.example.TelegramBot.Repository.ProviderRepository;
import com.example.TelegramBot.Repository.ReceiptRepository;
import com.example.TelegramBot.Service.CounterService;
import com.example.TelegramBot.Service.UserService;
import com.example.TelegramBot.Shedullers.CreaterReceipt;
import com.example.TelegramBot.TelegramUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.TelegramBot.TelegramUtil.*;

@Component
public class TestAdminHandler implements Handler {

    @Autowired
    private UserService userService;
    @Autowired
    private CounterService counterService;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private ColumnReceiptRepository columnReceiptRepository;
    @Autowired
    private ReceiptRepository receiptRepository;


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message, Long chat_id) {
        String text="";
        StringBuilder builder=new StringBuilder();
        List<SendMessage> sendMessages=new ArrayList<>();
        List<UserDTO> allUsers= userService.getAll();
        switch (message){
            case ADMINCREATERECEIPTS:
                for (UserDTO user: allUsers) {

                    double allSum=0.0;
                    List<CounterDTO> counters=counterService.getByUser(user).stream().filter(counterDTO -> counterDTO.getStatus()==StatusCounter.WRITED).collect(Collectors.toList());
                    List<ColumnReceiptDTO> columnReceiptDTOS=new ArrayList<>();
                    for (CounterDTO counter:counters) {
                        ColumnReceiptDTO columnReceipt=new ColumnReceiptDTO();
                        columnReceipt.setRecalculation(0);
                        columnReceipt.setServiceDTO(counter.getServiceDTO());
                        columnReceipt.setVolume(counter.getData());
                        columnReceipt.setProviderDTO(providerRepository.findAll().get(0));
                        float sum=counter.getData()* Float.parseFloat(counter.getServiceDTO().getRate());
                        allSum+=sum;
                        columnReceipt.setSum(sum);
                        columnReceiptDTOS.add(columnReceipt);
                        counter.setStatus(StatusCounter.OLD);
                        counterService.save(counter);
                    }
                    ReceiptDTO receipt=new ReceiptDTO();
                    receipt.setSum((float) allSum);
                    receipt.setDateCreate(LocalDate.now());

                    ReceiptDTO receiptDTO=receiptRepository.save(receipt);
                    user.getReceiptDTOList().add(receipt);
                    userService.save(user);

                    for (ColumnReceiptDTO columnReceiptDTO:columnReceiptDTOS) {
                        columnReceiptDTO.setReceiptDTO(receiptDTO);
                        columnReceiptRepository.save(columnReceiptDTO);
                    }
                }

                break;
            case ADMINCHECKPAID:
                sendMessages=TelegramUtil.createNotificationForPaid(allUsers);
                break;

        }
        return List.copyOf(sendMessages);
    }

    @Override
    public List<State> operatedBotState() {
         return List.of(State.ADMIN);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(ADMINCREATERECEIPTS,ADMINCHECKPAID);
    }
}
