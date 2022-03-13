package com.example.TelegramBot;

import com.example.TelegramBot.Enums.StatusPaid;
import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.UserDTO;
import com.example.TelegramBot.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramUtil {

    public static final String REGISTRATION = "/registration";
    public static final String AUTHORIZATION = "/authorisation";
    public static final String CHECK = "/checkStatus";
    public static final String SENDDATA = "/sendData";
    public static final String GETRECEIPTS = "/getReceipts";
    public static final String GETDATEPAY = "/getDatePay";
    public static final String GETALLSERVICE= "/getAllService";
    public static final String GETALLACCOUNTS = "/getAllAccounts";
    public static final String GETRPAIDSUM = "/getPaidSum";
    public static final String GETACTUALCOUNTER= "/getCounters";
    public static final String ADMINCREATERECEIPTS = "/adminCreateReceipt";
    public static final String ADMINCHECKPAID = "/adminCheckPaid";
    public static final String EXIT = "/exit";
    public static final String DELETEALLCOUNTER = "/deleteAllActualCounter";

    public static SendMessage createMessageTemplate(AccountsDTO account,String text) {
        return createMessageTemplate(String.valueOf(account.getId()),text);
    }

    public static List<SendMessage> createNotificationForPaid(List<UserDTO> allUsers){
        List<SendMessage> msgs=new ArrayList<>();
        for (UserDTO user: allUsers) {
            if (user.getStatus()== StatusPaid.NOT_PAID) {
                if (user.getStatus()== StatusPaid.NOT_PAID) {
                    List<SendMessage> msgs_user=createMessagesTemplateToTG(user.getAccount(),"У вас не оплачен 1 или более чеков. Заплатите!");
                    msgs.addAll(msgs_user);
                }

            }
        }
        return msgs;
    }
    // Создаем шаблон SendMessage с включенным Markdown
    public static SendMessage createMessageTemplate(String chatId,String text) {
        SendMessage sendMessage=new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
    public static List<SendMessage> createMessagesTemplateToTG(List<AccountsDTO> accounts,String text) {
        List<SendMessage> msgs=new ArrayList<>();
        for (AccountsDTO account: accounts) {
            SendMessage sendMessage=createMessageTemplate(account,text);
            msgs.add(sendMessage);
        }
        return msgs;
    }
    public static SendMessage createMessageTemplateWithStartButttons(AccountsDTO account) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne =  new ArrayList<>();
        inlineKeyboardButtonsRowOne.add(createInlineKeyboardButton("Registration", REGISTRATION));
        inlineKeyboardButtonsRowOne.add(createInlineKeyboardButton("Authorisation", AUTHORIZATION));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));
        SendMessage sendMessage=TelegramUtil.createMessageTemplate(account,"Здравствуйте. Вас приветствует бот");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    public static SendMessage createMessageTemplateWithExitButton(AccountsDTO account,String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne =  new ArrayList<>();
        inlineKeyboardButtonsRowOne.add(createInlineKeyboardButton("Выход в меню", EXIT));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));
        SendMessage sendMessage=TelegramUtil.createMessageTemplate(account,text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    public static SendMessage createMessageTemplateWithAllButton(AccountsDTO account,String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List< List<InlineKeyboardButton>> listRows=new ArrayList<>();
        List<InlineKeyboardButton> ButtonsRowOne =  new ArrayList<>();
        List<InlineKeyboardButton> ButtonsRowTwo =  new ArrayList<>();
        List<InlineKeyboardButton> ButtonsRowThree=  new ArrayList<>();
        List<InlineKeyboardButton> ButtonsRowFour =  new ArrayList<>();
        List<InlineKeyboardButton> ButtonsRowFive =  new ArrayList<>();
        List<InlineKeyboardButton> ButtonsRowSix =  new ArrayList<>();
        ButtonsRowOne.add(createInlineKeyboardButton("Заполнить данные счетчика",SENDDATA));
        ButtonsRowTwo.add(createInlineKeyboardButton("Проверить статус оплаты",CHECK));
        ButtonsRowThree.add(createInlineKeyboardButton("Чеки",GETRECEIPTS));
        ButtonsRowThree.add(createInlineKeyboardButton("Заполненные счетчики",GETACTUALCOUNTER));
        ButtonsRowThree.add(createInlineKeyboardButton("Виды услуг",GETALLSERVICE));
        ButtonsRowFour.add(createInlineKeyboardButton("Дата ближ. оплаты",GETDATEPAY));
        ButtonsRowFive.add(createInlineKeyboardButton("Подключенные аккаунты",GETALLACCOUNTS));
        ButtonsRowFive.add(createInlineKeyboardButton("Cумма",GETRPAIDSUM));
        ButtonsRowSix.add(createInlineKeyboardButton("Генерация чека",ADMINCREATERECEIPTS));
        ButtonsRowSix.add(createInlineKeyboardButton("Уведомления о просрочке",ADMINCHECKPAID));
        ButtonsRowSix.add(createInlineKeyboardButton("Удалить текущ. счетчики",DELETEALLCOUNTER));
        inlineKeyboardMarkup.setKeyboard(List.of(ButtonsRowOne,ButtonsRowTwo,ButtonsRowThree,ButtonsRowFour,ButtonsRowFive,ButtonsRowSix));
        SendMessage sendMessage=TelegramUtil.createMessageTemplate(account,text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setProtectContent(true);
        return sendMessage;
    }
    // Создаем кнопку
    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        InlineKeyboardButton inlineKeyboardButton=new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(command);
        return inlineKeyboardButton;
    }

}
