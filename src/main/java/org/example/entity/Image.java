package org.example.entity;

import jakarta.persistence.*;
import org.example.utils.Constants;

import static org.example.utils.Constants.SCHEMA;
import static org.example.utils.Constants.TABLE_IMAGE;


@Entity
@Table(name = TABLE_IMAGE,schema = SCHEMA)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageName;
    private String createdDate;
    private String imagePath;

    public Image() {

    }

    public Image(String imageName, String imagePath, String createdDate, String type) {
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
