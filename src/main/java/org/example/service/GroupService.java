package org.example.service;

import org.example.entity.Group;
import org.example.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups(){
        List<Group> all = groupRepository.findAll();
        return all;
    }
}
