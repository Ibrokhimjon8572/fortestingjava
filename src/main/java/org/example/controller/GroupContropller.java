package org.example.controller;

import org.example.entity.Group;
import org.example.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/group")
public class GroupContropller {
    private final GroupService groupService;

    public GroupContropller(GroupService groupService) {
        this.groupService = groupService;
    }
    @GetMapping
    public String getAllGroups(Model model){
        List<Group> allGroups = groupService.getAllGroups();
        model.addAttribute("allGroups",allGroups);
        return "group";
    }
}
