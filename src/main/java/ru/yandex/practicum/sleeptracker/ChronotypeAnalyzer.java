package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeAnalyzer implements SleepAnalyzer {

    public enum Chronotype {
        OWL("Сова"),
        LARK("Жаворонок"),
        PIGEON("Голубь");

        private final String title;

        Chronotype(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(this::isNightSession)
                .map(this::classifySession)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        Chronotype result;
        if (owlCount > larkCount && owlCount > pigeonCount) {
            result = Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            result = Chronotype.LARK;
        } else {
            result = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult("Хронотип пользователя", result);
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime start = session.getStartTime().toLocalTime();
        LocalTime end = session.getEndTime().toLocalTime();
        return start.isAfter(LocalTime.of(18, 0)) || start.isBefore(LocalTime.of(6, 0)) || end.isBefore(LocalTime.of(12, 0));
    }

    private Chronotype classifySession(SleepingSession session) {
        LocalTime start = session.getStartTime().toLocalTime();
        LocalTime end = session.getEndTime().toLocalTime();

        int startHour = (start.getHour() < 12) ? start.getHour() + 24 : start.getHour();
        boolean isOwl = startHour >= 23 && !end.isBefore(LocalTime.of(9, 0));
        boolean isLark = start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0));

        if (isOwl) {
            return Chronotype.OWL;
        } else if (isLark) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }
}
