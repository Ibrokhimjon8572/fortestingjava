package org.example.service;


import org.example.entity.Image;
import org.example.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;

    public ImageService(ImageRepository imageRepository, ResourceLoader resourceLoader) {
        this.imageRepository = imageRepository;
        this.resourceLoader = resourceLoader;
    }
    @Value("${file.upload.directory}")
    private String imageDirectory;

    public void saveImage(MultipartFile file) throws IOException {

        Path directoryPath = Paths.get(imageDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        Path imagePath = Paths.get(imageDirectory, fileName);
        Files.write(imagePath, bytes);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        String formattedDateTime = now.format(dateTimeFormatter);

        Image image = new Image();
        image.setCreatedDate(formattedDateTime);
        image.setImageName(fileName);
        image.setImagePath(imagePath.toString());
        imageRepository.save(image);
    }

    public String deleteById(Long id) {
        Optional<Image> byId = imageRepository.findById(id);
        if (byId.isPresent()){
            Long image = byId.get().getId();
            imageRepository.deleteById(image);
            return "O'chirildi";
        }return"Xatolik yuz berdi";
    }

    public Page<Image> getAllImages(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }
}
