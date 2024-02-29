package org.example.utils;

import org.example.entity.ScheduledText;
import org.example.repository.TextRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class DailyTextScheduler {
    private final TextRepository textRepository;
    private final JdbcTemplate jdbcTemplate;

    public DailyTextScheduler(TextRepository textRepository, JdbcTemplate jdbcTemplate) {
        this.textRepository = textRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleDailyText(){

        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        String currentDay = dayOfWeek.toString().substring(0, 3);

        List<ScheduledText> textEntries = textRepository.findAllByEveryIsTrue()
                .stream().map(
                        txt ->new ScheduledText(txt.getId(), txt.getTime(), txt.getWeekDays()))
                .collect(Collectors.toList()
                );

        List<ScheduledText>  filteredText = filterScheduledTextsByDay(textEntries, currentDay);

        jdbcTemplate.update("DELETE FROM scheduled_text ");

        for (ScheduledText entry : filteredText) {
            jdbcTemplate.update("INSERT INTO scheduled_text (text_id, time, week_days) VALUES (?, ?, ?)",
                    entry.getTextId(), entry.getTime(), entry.getWeekDays());
        }
    }

    private List<ScheduledText> filterScheduledTextsByDay(List<ScheduledText> scheduledTexts, String currentDay) {
        List<ScheduledText> filteredTexts = new ArrayList<>();
        for (ScheduledText scheduledText : scheduledTexts) {
            if (scheduledText.getWeekDays().contains(currentDay) || scheduledText.getWeekDays().contains("ALL")) {
                filteredTexts.add(scheduledText);
            }
        }
        return filteredTexts;
    }
}
