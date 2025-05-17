package uz.pdp.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import uz.pdp.database.DB;
import uz.pdp.entity.Homework;
import uz.pdp.entity.User;
import uz.pdp.entity.UserStates;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateHandler {
    private static final TelegramBot bot = new TelegramBot(ResourceBundle.getBundle("settings").getString("BOT_TOKEN"));
    private static final ConcurrentHashMap<Long, UserStates> states = new ConcurrentHashMap<>();
    private static final Stack<User> users = new Stack<>();
    private static final Stack<Homework> homeworks = new Stack<>();

    public static void handle(TelegramBot bot, Update update) {
        if (update.message() != null) {
            Long chatId = update.message().chat().id();
            String text = update.message().text();
            UserStates state = states.getOrDefault(chatId, UserStates.START);
            switch (state) {
                case START:
                    start(chatId, update);
                    break;
                case USER_MENU:
                    userMenu(chatId, text);
                    break;
                case SENDING_HOMEWORK_DESCRIPTION:
                    getHomeworkDescription(chatId, text);
                    break;
                case SEND_HOMEWORK_ZIP_FILE:
                    sendHomeworkZipFile(chatId, update);
                    break;
            }
        }else if(update.callbackQuery() != null){

        }
    }

    private static void sendHomeworkZipFile(Long chatId, Update update) {
        String text = update.message().text();
        System.out.println(text);
        if (text != null) {
            if (text.equals("Cancel")) {
                states.put(chatId, UserStates.USER_MENU);
                homeworks.pop();
                SendMessage sendMessage = new SendMessage(chatId, "Cancelled");
                userMenuKeyboard(sendMessage);
                bot.execute(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(chatId, "Please send your homework as a .zip file");
                sendMessage.replyMarkup(new ReplyKeyboardMarkup("Cancel").resizeKeyboard(true));
                bot.execute(sendMessage);
            }
        } else if (update.message().document() != null) {
            System.out.println("Homework zip file received");
            if (!update.message().document().fileName().endsWith(".zip")) {
                SendMessage sendMessage = new SendMessage(chatId, "Please send your homework as a .zip file");
                sendMessage.replyMarkup(new ReplyKeyboardMarkup("Cancel").resizeKeyboard(true));
                bot.execute(sendMessage);
                return;
            }
            Homework homework = homeworks.pop();
            homework.setZipFileId(update.message().document().fileId());
            homework.setSendTime(LocalDateTime.now());
            DB.homeworks.add(homework);
            states.put(chatId, UserStates.USER_MENU);

            Long adminChatId = Long.valueOf(getAdminChatId());
            states.put(adminChatId, UserStates.ADMIN_MENU);
            SendMessage sendMessage1 = new SendMessage(adminChatId, """
                    New homework sent from student: %s %s
                    Please click Check Homework button to check it!
                    """.formatted(update.message().chat().firstName(), update.message().chat().lastName()));
            adminMenuKeyboard(sendMessage1);
            bot.execute(sendMessage1);

            SendMessage sendMessage = new SendMessage(chatId, "Homework sent successfully to Teacher");
            userMenuKeyboard(sendMessage);
            bot.execute(sendMessage);
        }
    }
    private static void start(Long chatId, Update update) {
        if (adminChecker(chatId)) {
            states.put(chatId, UserStates.ADMIN_MENU);
            SendMessage message = new SendMessage(chatId, "Welcome Admin👋");
            adminMenuKeyboard(message);
            bot.execute(message);
        } else {
            states.put(chatId, UserStates.USER_MENU);
            User user1 = DB.users.stream().filter(user -> user.getChatId().equals(chatId)).findFirst().orElse(null);
            if (user1 == null) {
                User user = User.builder().chatId(chatId).
                        firstname(update.message().from().firstName()).
                        lastname(update.message().from().lastName()).
                        username(update.message().from().username()).
                        build();
                DB.users.add(user);
            }
            SendMessage message = new SendMessage(chatId, "Welcome User👋");
            userMenuKeyboard(message);
            bot.execute(message);
        }
    }
    private static void getHomeworkDescription(Long chatId, String text) {
        if (text.equals("Cancel")) {
            states.put(chatId, UserStates.USER_MENU);
            SendMessage sendMessage = new SendMessage(chatId, "Cancelled");
            userMenuKeyboard(sendMessage);
            bot.execute(sendMessage);
        } else {
            Homework homework = new Homework();
            homework.setDescription(text);
            homework.setChatId(chatId);
            homework.setId(UUID.randomUUID());
            homeworks.push(homework);
            states.put(chatId, UserStates.SEND_HOMEWORK_ZIP_FILE);
            SendMessage sendMessage = new SendMessage(chatId, "Please click 📎 button and send homework as a .zip file");
            sendMessage.replyMarkup(new ReplyKeyboardMarkup("Cancel").resizeKeyboard(true));
            bot.execute(sendMessage);
        }
    }

    private static void userMenuKeyboard(@NotNull SendMessage message) {
        message.replyMarkup(new ReplyKeyboardMarkup("Send homework", "Show old homework").resizeKeyboard(true));
    }

    private static void userMenu(Long chatId, @NotNull String text) {
        switch (text) {
            case "Send homework":
                sendHomeworkDescription(chatId);
                break;
            case "Show old homework":
                showOldHomeworkForUser(chatId);
                break;
            default:
                SendMessage message = new SendMessage(chatId, "Unknown command☹️");
                bot.execute(message);
        }
    }

    private static void sendHomeworkDescription(Long chatId) {
        states.put(chatId, UserStates.SENDING_HOMEWORK_DESCRIPTION);
        SendMessage message = new SendMessage(chatId, "Please send description for  homework");
        message.replyMarkup(new ReplyKeyboardMarkup("Cancel").resizeKeyboard(true));
        bot.execute(message);
    }

    private static void showOldHomeworkForUser(Long chatId) {
        StringBuilder sb = new StringBuilder();
        AtomicInteger count = new AtomicInteger();
        List<UUID> homeworkIds = new ArrayList<>();
        DB.homeworks.stream().filter(homework -> homework.getChatId().equals(chatId)).
                filter(homework -> homework.getCheckedAt() != null).forEach(homework -> {
                    sb.append(count.getAndIncrement()).append(". ").append(homework.getDescription()).append("\n");
                    homeworkIds.add(homework.getId());
                });
        if (homeworkIds.isEmpty()) {
            states.put(chatId, UserStates.USER_MENU);
            SendMessage message = new SendMessage(chatId, "No homework found");
            userMenuKeyboard(message);
            bot.execute(message);
        }else{
            states.put(chatId, UserStates.SHOW_OLD_HOMEWORK_USER);
            SendMessage message = new SendMessage(chatId, "Old homework list:\n\n" + sb);
            message.replyMarkup(homeworkButtons(homeworkIds));
            bot.execute(message);
        }
    }
    @NotNull
    private static InlineKeyboardMarkup homeworkButtons(@NotNull List<UUID> homeworksIds) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList<>();
        StringBuilder lastHomeworkId = new StringBuilder();
        StringBuilder firstHomeworkId = new StringBuilder();
        for (int i = 1; i <= homeworksIds.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(i))
                    .callbackData("h_" + homeworksIds.get(i - 1));
            row.add(button);
            if (i % 5 == 0) {
                markup.addRow(row.toArray(new InlineKeyboardButton[0]));
                row.clear();
            }
            if (i == homeworksIds.size()) {
                UUID uuid = homeworksIds.get(i - 1);
                lastHomeworkId.append(uuid);
            }
            if (i == 1) {
                UUID uuid = homeworksIds.get(0);
                firstHomeworkId.append(uuid);
            }
        }
        if (!row.isEmpty()) {
            markup.addRow(row.toArray(new InlineKeyboardButton[0]));
        }
        if (!row.isEmpty()) {
            row.clear();
            InlineKeyboardButton prev = new InlineKeyboardButton("⏮️")
                    .callbackData("prev_" + firstHomeworkId);
            InlineKeyboardButton reject = new InlineKeyboardButton("❌")
                    .callbackData("reject");
            InlineKeyboardButton next = new InlineKeyboardButton("⏭️")
                    .callbackData("next_" + lastHomeworkId);
            row.add(prev);
            row.add(reject);
            row.add(next);
            markup.addRow(row.toArray(new InlineKeyboardButton[0]));
        }

        return markup;
    }

    private static void adminMenuKeyboard(@NotNull SendMessage message) {
        message.replyMarkup(new ReplyKeyboardMarkup("Check homework", "Show old homework").resizeKeyboard(true));


    }

    private static boolean adminChecker(Long chatId) {
        return DB.users.stream().anyMatch(user -> user.getChatId().equals(chatId));
    }
    private static String getAdminChatId() {
        return ResourceBundle.getBundle("settings").getString("ADMIN_ID");
    }
}
