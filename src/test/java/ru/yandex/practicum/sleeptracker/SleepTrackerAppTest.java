package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    @Test
    void testTotalSessionsCountWithSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(7), SleepQuality.NORMAL)
        );
        assertEquals(2L, new TotalSessionsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testTotalSessionsCountEmpty() {
        assertEquals(0L, new TotalSessionsAnalyzer().apply(List.of()).getValue());
    }

    @Test
    void testMinDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD), // 480 мин
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 0), LocalDateTime.of(2025, 10, 3, 15, 0), SleepQuality.NORMAL) // 60 мин
        );
        assertEquals(60L, new MinDurationAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testMinDurationEmpty() {
        assertEquals(0L, new MinDurationAnalyzer().apply(List.of()).getValue());
    }

    @Test
    void testMaxDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD), // 480 мин
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 0), LocalDateTime.of(2025, 10, 3, 15, 0), SleepQuality.NORMAL) // 60 мин
        );
        assertEquals(480L, new MaxDurationAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testMaxDurationEmpty() {
        assertEquals(0L, new MaxDurationAnalyzer().apply(List.of()).getValue());
    }

    @Test
    void testAvgDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD), // 480 мин
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 12, 0), LocalDateTime.of(2025, 10, 3, 14, 0), SleepQuality.NORMAL) // 120 мин
        );

        assertEquals(300L, new AvgDurationAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testAvgDurationEmpty() {
        assertEquals(0L, new AvgDurationAnalyzer().apply(List.of()).getValue());
    }

    @Test
    void testBadQualitySessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(7), SleepQuality.BAD),
                new SleepingSession(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(6), SleepQuality.BAD)
        );
        assertEquals(2L, new BadQualitySessionsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testBadQualitySessionsZero() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD)
        );
        assertEquals(0L, new BadQualitySessionsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsNormalSleep() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD)
        );
        assertEquals(0L, new SleeplessNightsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsDaySleepOnly() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 7, 0), LocalDateTime.of(2025, 10, 2, 11, 0), SleepQuality.BAD)
        );
        assertEquals(1L, new SleeplessNightsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsEdgeCaseBoundaryTime() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 18, 0),
                        LocalDateTime.of(2025, 10, 2, 0, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        LocalDateTime.of(2025, 10, 2, 11, 0),
                        SleepQuality.NORMAL
                )
        );
        assertEquals(1L, new SleeplessNightsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsAcrossMonths() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 31, 23, 0), LocalDateTime.of(2025, 11, 1, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 11, 2, 23, 0), LocalDateTime.of(2025, 11, 3, 7, 0), SleepQuality.GOOD)
        );

        assertEquals(1L, new SleeplessNightsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void testChronotypeOwl() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD)
        );
        assertEquals("Сова", new ChronotypeAnalyzer().apply(sessions).getValue().toString());
    }

    @Test
    void testChronotypeLark() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 21, 30), LocalDateTime.of(2025, 10, 2, 6, 30), SleepQuality.GOOD)
        );
        assertEquals("Жаворонок", new ChronotypeAnalyzer().apply(sessions).getValue().toString());
    }

    @Test
    void testChronotypePigeonDefault() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 30), SleepQuality.GOOD)
        );
        assertEquals("Голубь", new ChronotypeAnalyzer().apply(sessions).getValue().toString());
    }
}