package com.es.phoneshop.model.product.security;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private static final long MINUTES_TO_DISCHARGE = 1;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private Map<String, LocalTime> timeMap = new ConcurrentHashMap<>();

    private static class Singleton {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.Singleton.INSTANCE;
    }

    private DefaultDosProtectionService() {
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        LocalTime now = LocalTime.now();
        if (count == null) {
            count = 1L;
            timeMap.put(ip, now);
        } else {
            if (ChronoUnit.MINUTES.between(timeMap.get(ip), now) >= MINUTES_TO_DISCHARGE) {
                countMap.put(ip, 1L);
                timeMap.put(ip, now);
                return true;
            }
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
