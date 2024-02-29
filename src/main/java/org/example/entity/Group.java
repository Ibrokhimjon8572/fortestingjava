package org.example.entity;

import jakarta.persistence.*;

import java.util.Set;

import static org.example.utils.Constants.SCHEMA;
import static org.example.utils.Constants.TABLE_GROUP;

@Entity
@Table(name = TABLE_GROUP,schema = SCHEMA)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    private Long telegramGroupId;

    @ManyToMany
    @JoinTable(
            name = "group_text",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id")
    )
    private Set<Text> texts;

    public Group(String groupName, Long telegramGroupId, Set<Text> texts) {
        this.groupName = groupName;
        this.telegramGroupId = telegramGroupId;
        this.texts = texts;
    }

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getTelegramGroupId() {
        return telegramGroupId;
    }

    public void setTelegramGroupId(Long telegramGroupId) {
        this.telegramGroupId = telegramGroupId;
    }

    public Set<Text> getTexts() {
        return texts;
    }

    public void setTexts(Set<Text> texts) {
        this.texts = texts;
    }
}
