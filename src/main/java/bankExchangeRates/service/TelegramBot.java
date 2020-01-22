package bankExchangeRates.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    public TelegramBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        sendMsg(message, "Hello! I will show the addresses of the lowest USD currency from bnb.by!");
        sendMsg(message, "Please, wait one minute :) Otherwise retry again.");
        Service service = new Service();
        service.startParse();
        if (text.equals("/start")) {
            sendMsg(message, "The lowest currency 1USD is " + service.getMinCurrency() + "BYN");
            sendMsg(message, "The addresses: " + service.getAddressMinCurrency());
        }
    }

    private void sendMsg(Message msg, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "SmallestCurrency_bot";
    }

    @Override
    public String getBotToken() {
        return "1073603307:AAGRLn5LuYsNwYRkn4Fg2gWujpZB1fV5DtM";
    }
}