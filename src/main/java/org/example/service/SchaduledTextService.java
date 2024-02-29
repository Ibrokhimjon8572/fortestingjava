package org.example.service;

import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.entity.Group;
import org.example.entity.Image;
import org.example.entity.ScheduledText;
import org.example.entity.Text;
import org.example.repository.GroupRepository;
import org.example.repository.ImageRepository;
import org.example.repository.SchaduledTextRepository;
import org.example.repository.TextRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
@Configuration
@EnableScheduling
@Service
public class SchaduledTextService {

    private final SchaduledTextRepository schaduledTextRepository;
    private final TextRepository textRepository;
    private final GroupRepository groupRepository;
    private final ImageRepository imageRepository;
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot6652215835:AAFxbdrPlJzptbNfsKXwssBITupueVxoKAk/";

    public SchaduledTextService(SchaduledTextRepository schaduledTextRepository, TextRepository textRepository, GroupRepository groupRepository, ImageRepository imageRepository) {
        this.schaduledTextRepository = schaduledTextRepository;
        this.textRepository = textRepository;
        this.groupRepository = groupRepository;
        this.imageRepository = imageRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkHourlyText(){
        LocalTime currentTime = LocalTime.now();
        LocalTime currentHourAndMinute = LocalTime.of(currentTime.getHour(), currentTime.getMinute());


        List<ScheduledText> scheduledTexts = schaduledTextRepository.findAllByTime(String.valueOf(currentHourAndMinute));

        for (ScheduledText text : scheduledTexts){
            sendText(text.getTextId());
        }
    }
    private void sendText(Long textId){
        Optional<Text> byId = textRepository.findByIdWithGroupsAndImages(textId);
        if (byId.isPresent()){
            Text text = byId.get();
            if (text.getAllGroups().equals(true)){
                if (text.getImages().isEmpty()){
                    List<Group> all = groupRepository.findAll();
                    for (Group group:all){
                        sendMessageWithTextSchedule(group.getTelegramGroupId(), text.getContent());
                    }
                }else {
                    sendTextWithImagesToGroupsSchedule(text);
                }
            }else {
                if (text.getImages().isEmpty()){
                    for (Group group: text.getGroups()){
                        Optional<Group> groupId = groupRepository.findById(group.getId());
                        sendMessageWithTextSchedule(groupId.get().getTelegramGroupId(), text.getContent());
                    }
                }else {
                    sendTextWithImagesToGroupsSchedule(text);
                }
            }
        }
    }
    private static void sendMessageWithTextSchedule(Long chatId,String message) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("chat_id", String.valueOf(chatId))
                .add("text", message)
                .build();

        Request request = new Request.Builder()
                .url(TELEGRAM_API_URL + "sendMessage")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("Message sent successfully to chat ID: " + chatId);
            } else {
                System.err.println("Failed to send message. Response: " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /////////////////////////////////////////////////////////////
    public String sendTextWithImagesToGroupsSchedule(Text textMessageRequest) {
        if (textMessageRequest.getImages().size() > 1) {
            for (Group groupRequest : textMessageRequest.getGroups()) {
                Optional<Group> groupOptional = groupRepository.findById(groupRequest.getId());
                if (groupOptional.isPresent()) {
                    Group group = groupOptional.get();
                    long chatId = group.getTelegramGroupId();
                    WebClient webClient = WebClient.create();
                    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
                    List<Resource> imageResources = getResourcesSchedule(textMessageRequest.getImages());

                    List<String> mediaParts = new ArrayList<>();
                    for (int i = 0; i < imageResources.size(); i++) {
                        textMessageRequest.getImages().get(i);
                        Resource imageResource = imageResources.get(i);

                        bodyBuilder.part("photo" + i, imageResource);
                        bodyBuilder.part("caption" + i, textMessageRequest.getContent());

                        mediaParts.add("{\"type\":\"photo\",\"media\":\"attach://photo" + i + "\"}");
                    }
                    String mediaJson = "[" + String.join(",", mediaParts) + "]";
                    bodyBuilder.part("media", mediaJson);
                    bodyBuilder.part("chat_id", chatId);
                    try {
                        webClient.method(HttpMethod.POST)
                                .uri(TELEGRAM_API_URL + "/sendMediaGroup")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();
                        sendMessageWithTextSchedule(chatId,textMessageRequest.getContent());
                    } catch (WebClientResponseException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            System.err.println("Endpoint not found");
                        } else {
                            System.err.println("Error occurred: " + e.getRawStatusCode() + " - " + e.getResponseBodyAsString());
                        }
                    }
                }
            }
        }else {
            sendPhotoWithTextSchedule(textMessageRequest, textMessageRequest.getImages().get(0).getId());
        }
        return "Saqlandi";
    }
    private List<Resource> getResourcesSchedule(List<Image> images) {
        List<Resource> resources = new ArrayList<>();
        for (Image image : images) {
            Optional<Image> imageOptional = imageRepository.findById(image.getId());
            if (imageOptional.isPresent()) {
                try {
                    Resource resource = new ClassPathResource("image/" + imageOptional.get().getImageName());
                    if (resource.exists()) {
                        resources.add(resource);
                    } else {
                        System.err.println("Resource not found: " + imageOptional.get().getImageName());
                    }
                } catch (Exception e) {
                    System.err.println("Error loading resource: " + e.getMessage());
                }
            }
        }
        return resources;
    }
    private void sendPhotoWithTextSchedule(Text textMessageRequest, Long imageId) {
        for (Group group : textMessageRequest.getGroups()) {
            Optional<Group> byId = groupRepository.findById(group.getId());
            Optional<Image> byId1 = imageRepository.findById(imageId);
            File photoFile = new File(byId1.get().getImagePath() + byId1.get().getImageName());
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(TELEGRAM_API_URL + "sendPhoto");

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("chat_id", new StringBody(String.valueOf(byId), ContentType.TEXT_PLAIN));
            builder.addPart("photo", new FileBody(photoFile, ContentType.DEFAULT_BINARY));
            builder.addPart("caption", new StringBody(textMessageRequest.getContent(), ContentType.TEXT_PLAIN));

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            try {
                httpClient.execute(httpPost);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
