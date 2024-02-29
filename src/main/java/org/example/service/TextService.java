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
import org.apache.http.util.EntityUtils;
import org.example.controller.BotControlle;
import org.example.entity.*;
import org.example.repository.GroupRepository;
import org.example.repository.ImageRepository;
import org.example.repository.TextRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.*;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TextService {
    private final TextRepository textRepository;
    private final GroupRepository groupRepository;
    private final ImageRepository imageRepository;


    public TextService(TextRepository textRepository,
                       GroupRepository groupRepository,
                       ImageRepository imageRepository
    ) {
        this.textRepository = textRepository;
        this.groupRepository = groupRepository;
        this.imageRepository = imageRepository;
    }

    public List<Group> geAllGroups(){
       return groupRepository.findAll();
    }
    public void saveToDatabase(Text sendMesageDto) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        String formattedDateTime = now.format(dateTimeFormatter);

        Text text = new Text();
        text.setContent(sendMesageDto.getContent());
        text.setCreatedAt(formattedDateTime);
        text.setTime(sendMesageDto.getTime());
        text.setEvery(sendMesageDto.getEvery());
        text.setWeekDays(sendMesageDto.getWeekDays());
        text.setAllGroups(sendMesageDto.getAllGroups());

        Set<Group> existingGroups = sendMesageDto.getGroups().stream()
                .map(group -> groupRepository.findById(group.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        text.setGroups(existingGroups);

        if (sendMesageDto.getImages() != null) {
            Set<Image> existingImage = sendMesageDto.getImages().stream()
                    .map(image -> imageRepository.findById(image.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            text.setImages((List<Image>) existingImage);
        }
        Text savedText = textRepository.save(text);
        for (Group group : existingGroups) {
            group.getTexts().add(savedText); // Assuming getTexts() returns a Set
            groupRepository.save(group);
        }
    }

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot6652215835:AAFxbdrPlJzptbNfsKXwssBITupueVxoKAk/";


    public String sendMessageToGroups(Text sendMesage) {

        if (!sendMesage.getEvery()) {
            if (sendMesage.getAllGroups()){
                List<Group> all = groupRepository.findAll();
                for (Group chatId : all) {
                    Optional<Group> byId = groupRepository.findById(chatId.getId());
                    sendMessageWithText(byId.get().getTelegramGroupId(), sendMesage.getContent());
                }
            }else {
                for (Group chatId : sendMesage.getGroups()) {
                    Optional<Group> byId = groupRepository.findById(chatId.getId());
                    sendMessageWithText(byId.get().getTelegramGroupId(), sendMesage.getContent());
                }
            }
        }saveToDatabase(sendMesage);
        return "Saqlandi";
    }

    private static void sendMessageWithText(Long chatId, String message) {
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
    public String sendTextWithImagesToGroups(Text textMessageRequest) {
        if (textMessageRequest.getImages().size() > 1) {
            for (Group groupRequest : textMessageRequest.getGroups()) {
                Optional<Group> groupOptional = groupRepository.findById(groupRequest.getId());
                if (groupOptional.isPresent()) {
                    Group group = groupOptional.get();
                    long chatId = group.getTelegramGroupId();
                    WebClient webClient = WebClient.create();
                    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
                    List<Resource> imageResources = getResources(textMessageRequest.getImages());

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

                        sendMessageWithText(chatId,textMessageRequest.getContent());

                    } catch (WebClientResponseException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            System.err.println("Endpoint not found");
                        } else {
                            System.err.println("Error occurred: " + e.getRawStatusCode() + " - " + e.getResponseBodyAsString());
                        }
                    }
                }
            }
        } else {
            for (Group groupRequest : textMessageRequest.getGroups()) {
                Optional<Group> groupOptional = groupRepository.findById(groupRequest.getId());
                if (groupOptional.isPresent()) {
                    Group group = groupOptional.get();
                    long chatId = group.getTelegramGroupId();
                    sendPhotoWithText(chatId, textMessageRequest);
                }
            }
        }
        return "Saqlandi";
    }




    private List<Resource> getResources(List<Image> images) {
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


    private void sendPhotoWithText(Long chatId, Text textMessageRequest) {
        Optional<Image> byId1 = imageRepository.findById(textMessageRequest.getImages().get(0).getId());
        File photoFile = new File(byId1.get().getImagePath());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(TELEGRAM_API_URL + "sendPhoto");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("chat_id", new StringBody(String.valueOf(chatId), ContentType.TEXT_PLAIN));
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


    public Map<String ,List<Object>> getImageAndGroups(){
        Map<String, List<Object>> result = new HashMap<>();

        List<Image> images = imageRepository.findAll();
        result.put("image", Collections.unmodifiableList(images));

        List<Group> groups = groupRepository.findAll();
        result.put("group", Collections.unmodifiableList(groups));

        return result;
    }

    public Map<String,List<Object>> getTextHistory(){
        Map<String , List<Object>> result = new HashMap<>();

        List<Text> texts1 = textRepository.findAllByEveryIsTrue();
        result.put("everyTrue", Collections.unmodifiableList(texts1));

        List<Text> texts2 = textRepository.findAllByEveryIsFalse();
        result.put("everyFalse", Collections.unmodifiableList(texts2));

        return result;
    }
}
