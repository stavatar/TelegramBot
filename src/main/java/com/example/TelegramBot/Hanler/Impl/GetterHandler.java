package com.example.TelegramBot.Hanler.Impl;

import com.example.TelegramBot.Enums.StatusCounter;
import com.example.TelegramBot.ExcelWorker;
import com.example.TelegramBot.Hanler.Handler;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.CounterDTO;
import com.example.TelegramBot.Model.ReceiptDTO;
import com.example.TelegramBot.Model.ServiceDTO;
import com.example.TelegramBot.Repository.CounterRepository;
import com.example.TelegramBot.Service.AccountService;
import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Service.CounterService;
import com.example.TelegramBot.Service.ServiceService;
import com.example.TelegramBot.TelegramUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.stream.Collectors;

import static com.example.TelegramBot.TelegramUtil.*;

@Component
public class GetterHandler implements Handler {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CounterService counterService;
    @Autowired
    private ServiceService serviceService;
    @SneakyThrows
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(AccountsDTO account, String message,Long chat_id)  {
        SendMessage sendMessage=null;
        String text="";
        List<PartialBotApiMethod<? extends Serializable>> sendObj=new ArrayList<>();
        StringBuilder builder=new StringBuilder();
        switch (message){
            case GETALLACCOUNTS:
                List<AccountsDTO> accountsDTOS= accountService.getByUser(account.getUser());
                Integer number=0;
                for (AccountsDTO current: accountsDTOS) {
                    number++;
                    String t=number+") Type:"+current.getType()+
                              "\nLogin:" + current.getLogin()+"\n-------\n";
                    builder.append(t);
                }
                 text=builder.toString();
                break;
            case GETALLSERVICE:
                List<ServiceDTO> serviceDTOS = serviceService.getAll();
                StringBuilder stringBuilder = new StringBuilder();
                Integer count=0;
                for (ServiceDTO service : serviceDTOS) {
                    count++;
                    stringBuilder.append(count).append(") ").append(service.getName()).append("\n");
                    stringBuilder.append("Описание :").append(service.getDescription()).append("\n");
                    stringBuilder.append("Ед. Изм. :").append(service.getUnit().getName()).append("\n");
                    stringBuilder.append("Тариф : ").append(service.getRate()).append("\n");
                }
                text=stringBuilder.toString();
                break;
            case GETDATEPAY:
                text="Дата ближайшей оплаты"+ LocalDate.now().plusMonths(1).withDayOfMonth(10);
                break;
            case GETRECEIPTS:
                List<ReceiptDTO> receiptDTOList=account.getUser().getReceiptDTOList();
                for (ReceiptDTO receipt: receiptDTOList) {
                    ExcelWorker.CreateReceipt(receipt);
                    InputStream inputStream = Files.newInputStream(Paths.get("file.xls"));
                    LocalDate localDate= LocalDate.from(receipt.getDateCreate());
                    InputFile inputFile=new InputFile(inputStream,"Чек "+ localDate.toString()+".xls");
                    SendDocument sendDocument=new SendDocument();
                    sendDocument.setDocument(inputFile);
                    sendDocument.setChatId(String.valueOf(account.getId()));
                    sendObj.add(sendDocument);
                }
                if (receiptDTOList.size()==0){
                    text="Отсутстствуют чеки";
                }

                break;
            case GETRPAIDSUM:
                Float allsum=Float.parseFloat("0");
                List<ReceiptDTO> receipts=account.getUser().getReceiptDTOList();
                for (ReceiptDTO receipt: receipts) {
                    allsum+=receipt.getSum();
                }
                text= String.valueOf(allsum);
                break;
            case GETACTUALCOUNTER:
                List<CounterDTO> counters=counterService.getByUser(account.getUser()).stream().filter((counterDTO -> counterDTO.getStatus()== StatusCounter.WRITED)).collect(Collectors.toList());
                for (CounterDTO counter: counters) {
                     builder.append("№")
                            .append(counter.getNumber())
                            .append(" , ")
                            .append("Услуга:")
                            .append(counter.getServiceDTO().getName())
                            .append(" , Значение")
                            .append(counter.getData())
                            .append("\n");
                }
                if (counters.size()==0){
                    builder.append("Отсутстствуют заполненные счетчики");
                }
                text= builder.toString();
                break;
        }
        sendMessage=createMessageTemplateWithAllButton(account,text);
        sendObj.add(sendMessage);
        return sendObj;
    }

    @Override
    public List<State> operatedBotState() {
        return List.of(State.GETTER);
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(GETALLACCOUNTS, GETDATEPAY,GETRECEIPTS,GETRPAIDSUM,GETACTUALCOUNTER,GETALLSERVICE);
    }
}
