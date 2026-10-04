package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsAnalyzer implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDateTime firstStart = sessions.getFirst().getStartTime();
        LocalDate firstNightDate = firstStart.toLocalTime().isBefore(LocalTime.NOON)
                ? firstStart.toLocalDate().minusDays(1)
                : firstStart.toLocalDate();

        LocalDateTime lastEnd = sessions.getLast().getEndTime();
        LocalDate lastNightDate = lastEnd.toLocalTime().isBefore(LocalTime.NOON)
                ? lastEnd.toLocalDate().minusDays(1)
                : lastEnd.toLocalDate();

        long totalNights = Period.between(firstNightDate, lastNightDate.plusDays(1)).getDays();

        long sleeplessNightsCount = LongStream.range(0, totalNights)
                .mapToObj(firstNightDate::plusDays)
                .filter(nightDate -> isSleeplessNight(nightDate, sessions))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNightsCount);
    }

    private boolean isSleeplessNight(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = nightDate.plusDays(1).atStartOfDay(); // 00:00
        LocalDateTime nightEnd = nightStart.plusHours(6);                // 06:00

        return sessions.stream().noneMatch(session ->
                session.getStartTime().isBefore(nightEnd) && session.getEndTime().isAfter(nightStart)
        );
    }
}
