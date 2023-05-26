package io.collective;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a simple aged cache implementation.
 */
public class SimpleAgedCache {

    // Represents the clock used for tracking time
    private final Clock clock;

    // Represents the map of entries in the cache
    private final Map<Object, ExpirableEntry> entries = new HashMap<>();

    /**
     * Constructs a SimpleAgedCache with the specified Clock object.
     *
     * @param clock The clock used for tracking time.
     */
    public SimpleAgedCache(Clock clock) {
        this.clock = clock;
    }

    /**
     * Constructs a SimpleAgedCache using the system default clock.
     * This constructor is equivalent to SimpleAgedCache(Clock.systemDefaultZone()).
     */
    public SimpleAgedCache() {
        this(Clock.systemDefaultZone());
    }

    /**
     * Puts a new entry into the cache with the specified key, value, and retention time.
     *
     * @param key              The key of the entry.
     * @param value            The value of the entry.
     * @param retentionInMillis The retention time of the entry in milliseconds.
     */
    public void put(Object key, Object value, int retentionInMillis) {
        // Create a new ExpirableEntry with the current time, retention time, and value
        ExpirableEntry entry = new ExpirableEntry(clock.millis(), retentionInMillis, value);
        // Add the entry to the map with the specified key
        entries.put(key, entry);
    }

    /**
     * Checks if the cache is empty.
     *
     * @return true if the cache is empty, false otherwise.
     */
    public boolean isEmpty() {
        // Remove any expired entries from the cache
        clearExpired();
        // Check if the map is empty
        return entries.isEmpty();
    }

    /**
     * Removes expired entries from the cache.
     */
    public void clearExpired() {
        // Iterate over a copy of the entries to avoid ConcurrentModificationException
        for (Map.Entry<Object, ExpirableEntry> entryObject : new HashMap<>(entries).entrySet()) {
            ExpirableEntry entry = entryObject.getValue();
            // Check if the elapsed time since the entry's start time is greater than or equal to its retention time
            if ((clock.millis() - entry.startTime) >= entry.retentionInMillis) {
                // If the entry has expired, remove it from the map
                entries.remove(entryObject.getKey());
            }
        }
    }

    /**
     * Returns the size of the cache.
     *
     * @return The size of the cache.
     */
    public int size() {
        // Remove any expired entries from the cache
        clearExpired();
        // Return the size of the map
        return entries.size();
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key of the entry.
     * @return The value associated with the key if present and not expired, otherwise null.
     */
    public Object get(Object key) {
        // Check if the key is present in the map
        if (entries.containsKey(key)) {
            ExpirableEntry entry = entries.get(key);
            // Check if the elapsed time since the entry's start time is less than its retention time
            if ((clock.millis() - entry.startTime) < entry.retentionInMillis) {
                // If the entry has not expired, return its value
                return entry.getValue();
            }
        }
        // If the key is not present or the entry has expired,
        return null;
    }
}