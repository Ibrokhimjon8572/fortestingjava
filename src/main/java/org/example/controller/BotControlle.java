package org.example.controller;

import org.example.entity.Group;
import org.example.entity.Image;
import org.example.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;

import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class BotControlle extends TelegramLongPollingBot {

    private final GroupRepository groupRepository;

    public BotControlle(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null) {

            Chat chat = message.getChat();

            if (chat != null && chat.isGroupChat()) {
                Long chatId = chat.getId();
                String title = chat.getTitle();

                if (!groupRepository.isGroupExist(chatId)) {
                    Group group = new Group();
                    group.setTelegramGroupId(chatId);
                    group.setGroupName(title);
                    groupRepository.save(group);
                }
            }
        }
    }

    public void sendAlbumImages(Long chatId, List<Optional<Image>> imagePaths, String caption) {
        List<InputMedia> inputMediaList = new ArrayList<>();

        for (Optional<Image> imagePath : imagePaths) {
//            ClassLoader classLoader = getClass().getClassLoader();
            File photoFile = imagePath.isPresent() ? new File(this.getClass().getResource("/image/"+imagePath.get().getImageName()).getFile()) : null;

//            String mediaName = imagePath.get().getImageName();
//
            InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
            inputMediaPhoto.setMedia("");
//            inputMediaPhoto.setCaption(caption);
//            inputMediaPhoto.setMediaName(mediaName);

            inputMediaList.add(inputMediaPhoto);
        }

        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setMedias(inputMediaList);
        sendMediaGroup.setChatId(chatId.toString());

        try {
            execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            System.out.println(e);
        }
    }



    @Override
    public String getBotUsername() {
        return "mbf_sendtxt_bot";
    }
    @Override
    public String getBotToken() {
        return "6652215835:AAFxbdrPlJzptbNfsKXwssBITupueVxoKAk";
    }

}

