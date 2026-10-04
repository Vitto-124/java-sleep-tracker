package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SleepLogParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static List<SleepingSession> parseFile(String filePath) throws IOException {
        try (var lines = Files.lines(Path.of(filePath))) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(SleepLogParser::parseLine)
                    .toList();
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());
        return new SleepingSession(start, end, quality);
    }
}
