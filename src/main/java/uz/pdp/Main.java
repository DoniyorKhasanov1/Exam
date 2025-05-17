package uz.pdp;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import uz.pdp.database.DB;
import uz.pdp.entity.User;
import uz.pdp.service.UpdateHandler;

import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("settings");

    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(bundle.getString("BOT_TOKEN"));
        adminAdder();
        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                CompletableFuture.runAsync(() -> {
                    UpdateHandler.handle(bot, update);
                });
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);

    }

    private static void adminAdder() {
        DB.users.add(User.builder().chatId(Long.valueOf(bundle.getString("ADMIN_ID"))).build());
    }
}
