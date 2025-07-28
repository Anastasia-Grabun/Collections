package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**### 11. Реализация многопоточного счетчика событий
Создайте потокобезопасный класс EventCounter,
который подсчитывает количество различных типов событий.
Класс должен поддерживать concurrent доступ и предоставлять методы
для инкремента счетчика и получения статистики.
 Дополнительные требования:
 Класс должен быть потокобезопасным
 Метод getAllCounts() должен возвращать snapshot данных
 Реализуйте метод для получения топ-N событий по частоте
 Добавьте возможность установки TTL для событий **/

public class EventCounter {
    private final Map<String, EventData> events;
    private volatile long defaultTTL = -1;

    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();


    public void stop() {
        cleaner.shutdown();
    }

    public EventCounter() {
        this.events = new ConcurrentHashMap<>();
    }

    private static class EventData {
        long count;
        long expireTime;

        EventData() {
            this.count = 1;
        }

        void updateExpireTime(long ttlMillis) {
            if (ttlMillis > 0) {
                this.expireTime = System.currentTimeMillis() + ttlMillis;
            } else {
                this.expireTime = -1;
            }
        }

        boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
    }

    public void setDefaultTTL(long ttlMillis) {
        this.defaultTTL = ttlMillis;
    }

    public void incrementEvent(String eventType, long customTTLMillis) {
        events.compute(eventType, (key, data) -> {
            if (data == null) {
                data = new EventData();
            } else {
                data.count++;
            }
            data.updateExpireTime(customTTLMillis);
            return data;
        });
    }

    public void incrementEvent(String eventType) {
        incrementEvent(eventType, defaultTTL);
    }

    public long getEventCount(String eventType) {
        EventData data = events.get(eventType);
        if (data == null || data.isExpired()) {
            return 0L;
        }

        return data.count;
    }

    public Map<String, Long> getAllCounts() {
        Map<String, Long> result = new HashMap<>();
        events.forEach((key, data) -> {
            if (!data.isExpired()) {
                result.put(key, data.count);
            }
        });

        return result;
    }

    public void reset() {
        events.clear();
    }

    public void cleanUpExpired() {
        events.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public void startAutoCleanup(long period, TimeUnit unit) {
        cleaner.scheduleAtFixedRate(this::cleanUpExpired, period, period, unit);
    }

}
