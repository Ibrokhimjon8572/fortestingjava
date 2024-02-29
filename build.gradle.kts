plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.5")
    implementation("org.telegram:telegrambots:6.8.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.5")
    implementation("com.github.pengrad:java-telegram-bot-api:6.9.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.5")
    implementation("org.postgresql:postgresql:42.6.0")
    compileOnly("org.projectlombok:lombok:1.18.30")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.1.5")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.1.0")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
