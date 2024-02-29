package org.example.controller;

import org.example.entity.Group;
import org.example.entity.Text;
import org.example.service.TextService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping("/image")
    public ResponseEntity<String> sendMessageToGroupWithImage(@RequestBody Text text){
       return ResponseEntity.ok(textService.sendTextWithImagesToGroups(text));
    }


    @PostMapping("/message")
    public ResponseEntity<String> sendMessagetoGroups(@RequestBody Text text){
        return ResponseEntity.ok(textService.sendMessageToGroups(text));
    }

    @GetMapping("/message")
    public String getAllGroups(Model model){
        List<Group> allGroups = textService.geAllGroups();
        model.addAttribute("allGroups",allGroups);
        return "send-message";
    }


    @GetMapping("/image_message")
    public String getImageAndGroup(Model model){
        Map<String, List<Object>> imageAndGroups = textService.getImageAndGroups();
        model.addAttribute("imageAndGroups",imageAndGroups);
        return "send-image-text";
    }

    @GetMapping("/history")
    public String getHistoryText(Model model){
        Map<String, List<Object>> textHistory = textService.getTextHistory();
        model.addAttribute("textHistory",textHistory);
        return "history-text";
    }
}
