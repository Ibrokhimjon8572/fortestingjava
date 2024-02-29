package org.example.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

import static org.example.utils.Constants.SCHEMA;
import static org.example.utils.Constants.TABLE_TEXT;

@Entity
@Table(schema = SCHEMA,name = TABLE_TEXT)
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "texts")
    private Set<Group> groups;
    private String createdAt;
    private String content;
    private String time;
    private String weekDays;
    private Boolean every;
    private Boolean allGroups;
    @ManyToMany
    @JoinTable(
            name = "text_image_mapping",
            joinColumns = @JoinColumn(name = "text_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images;


    public Text(Set<Group> groups, String createdAt, String content, String time, String weekDays,
                Boolean every, List<Image> images, Boolean allGroups) {
        this.groups = groups;
        this.createdAt = createdAt;
        this.content = content;
        this.time = time;
        this.weekDays = weekDays;
        this.every = every;
        this.images = images;
        this.allGroups = allGroups;
    }

    public Text() {
    }

    public Boolean getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(Boolean allGroups) {
        this.allGroups = allGroups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public Boolean getEvery() {
        return every;
    }

    public void setEvery(Boolean every) {
        this.every = every;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
