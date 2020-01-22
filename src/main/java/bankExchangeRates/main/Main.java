package bankExchangeRates.main;

import bankExchangeRates.service.TelegramBot;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Main{
    public static void main(String[] args) {
        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegram = new TelegramBotsApi();
            DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
            telegram.registerBot(new TelegramBot(options));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

//        CurrencyService service = new CurrencyService();
//        service.startParse();
//        service.checkDBTable();
//        System.out.println("---" + service.getAddressMinCurrency());
    }
}
