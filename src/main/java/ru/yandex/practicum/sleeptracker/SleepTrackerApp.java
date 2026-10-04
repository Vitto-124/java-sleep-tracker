package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class SleepTrackerApp {

    private final List<SleepAnalyzer> analyzers;

    public SleepTrackerApp() {
        this.analyzers = List.of(
                new TotalSessionsAnalyzer(),
                new MinDurationAnalyzer(),
                new MaxDurationAnalyzer(),
                new AvgDurationAnalyzer(),
                new BadQualitySessionsAnalyzer(),
                new SleeplessNightsAnalyzer(),
                new ChronotypeAnalyzer()
        );
    }

    public void run(String filePath) {
        try {
            List<SleepingSession> sessions = SleepLogParser.parseFile(filePath);

            analyzers.stream()
                    .map(analyzer -> analyzer.apply(sessions))
                    .forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Ошибка при чтении или обработке файла: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите путь к файлу лога сна в аргументах командной строки.");
            return;
        }
        new SleepTrackerApp().run(args[0]);
    }
}