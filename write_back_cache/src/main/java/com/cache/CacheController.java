package com.cache;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {
    private final Map<String, CacheEntry> cache = new HashMap<>();

    @GetMapping("/{key}")
    public Object get(@PathVariable String key) {
        System.out.println("GET /cache/" + key + " - Checking cache...");
        CacheEntry entry = cache.get(key);
        if (entry != null && entry.isExpired()) {
            System.out.println("GET /cache/" + key + " - Key expired, returning null");
            return null;
        }
        if (entry != null) {
            System.out.println("GET /cache/" + key + " - Key found: " + entry.getValue());
            return entry.getValue();
        }
        System.out.println("GET /cache/" + key + " - Key not found");
        return null;
    }

    // Additional APIs can be added here
}

class CacheEntry {
    private final String value;
    private final long createdAt;
    private final int ttl;

    public CacheEntry(String value, int ttl) {
        this.value = value;
        this.createdAt = System.currentTimeMillis();
        this.ttl = ttl;
    }

    public String getValue() {
        return value;
    }

    public boolean isExpired() {
        return ttl == 0 || System.currentTimeMillis() - createdAt > ttl * 1000;
    }
}
