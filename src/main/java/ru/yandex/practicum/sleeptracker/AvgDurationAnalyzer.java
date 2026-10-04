package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AvgDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double avg = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", Math.round(avg));
    }
}
