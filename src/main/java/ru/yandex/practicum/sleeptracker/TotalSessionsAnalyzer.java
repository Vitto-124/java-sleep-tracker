package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionsAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long count = sessions.size();
        return new SleepAnalysisResult("Общее количество сессий сна", count);
    }
}
