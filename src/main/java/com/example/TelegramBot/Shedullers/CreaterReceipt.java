package com.example.TelegramBot.Shedullers;
import com.example.TelegramBot.Bot;
import com.example.TelegramBot.Enums.StatusPaid;
import com.example.TelegramBot.Model.*;
import com.example.TelegramBot.Repository.ColumnReceiptRepository;
import com.example.TelegramBot.Repository.ProviderRepository;
import com.example.TelegramBot.Repository.ReceiptRepository;
import com.example.TelegramBot.Service.CounterService;
import com.example.TelegramBot.Service.ServiceService;
import com.example.TelegramBot.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static com.example.TelegramBot.TelegramUtil.*;
@EnableScheduling
@Component
public class CreaterReceipt {
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

    @Autowired
    private Bot bot;
    @Scheduled(cron = "0 0 0 * * *")
    public void createReceipt()
    {
        List<UserDTO> allUsers= userService.getAll();
        for (UserDTO user: allUsers) {
            ReceiptDTO receipt=new ReceiptDTO();
            double allSum=0.0;
            List<CounterDTO> counters=counterService.getByUser(user);
            for (CounterDTO counter:counters) {
                ColumnReceiptDTO columnReceipt=new ColumnReceiptDTO();
                columnReceipt.setReceiptDTO(receipt);
                columnReceipt.setRecalculation(0);
                columnReceipt.setServiceDTO(counter.getServiceDTO());
                columnReceipt.setVolume(counter.getData());
                columnReceipt.setProviderDTO(providerRepository.findAll().get(0));
                float sum=counter.getData()* Float.parseFloat(counter.getServiceDTO().getRate());
                allSum+=sum;
                columnReceipt.setSum(sum);
                columnReceiptRepository.save(columnReceipt);
            }
            receipt.setSum((float) allSum);
            receipt.setDateCreate(LocalDate.now());
            receiptRepository.save(receipt);

        }

    }
    @Scheduled(cron = "0 0 0 * * *")
    public void checkPaid() throws TelegramApiException {
        List<UserDTO> allUsers= userService.getAll();
        for (SendMessage msg: createNotificationForPaid(allUsers)) {
            bot.execute(msg);
        }
    }

}
