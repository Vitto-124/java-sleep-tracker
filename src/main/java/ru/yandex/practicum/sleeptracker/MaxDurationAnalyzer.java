package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long max = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .max()
                .orElse(0);
        return new SleepAnalysisResult("Максимальная продолжительность сессии (мин)", max);
    }
}
