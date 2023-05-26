package io.collective

import java.time.Clock

/**
 * Represents a simple aged cache implementation.
 *
 * @property clock The Clock instance used for time-related operations.
 */
class SimpleAgedKache(
    private val clock: Clock = Clock.systemDefaultZone()
) {

    // Map to store the cache entries
    private val entries: MutableMap<Any?, ExpirableEntry> = HashMap()

    /**
     * Puts a new entry into the cache with the specified key, value, and retention time.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     * @param retentionInMillis The retention time of the entry in milliseconds.
     */
    fun put(key: Any?, value: Any?, retentionInMillis: Int) {
        val entry = ExpirableEntry(clock.millis(), retentionInMillis.toLong(), value)
        entries[key] = entry
    }

    /**
     * Checks if the cache is empty.
     *
     * @return true if the cache is empty, false otherwise.
     */
    fun isEmpty(): Boolean {
        // Remove any expired entries from the cache
        clearExpired()
        return entries.isEmpty()
    }

    /**
     * Removes expired entries from the cache.
     */
    private fun clearExpired() {
        // Iterates through the entries and removes those that have expired
        entries.entries.removeIf { (_, entry) ->
            // Condition checks if the current time minus the entry's start time
            // is greater than or equal to the retention time
            clock.millis() - entry.startTime >= entry.retentionInMillis
        }
    }

    /**
     * Returns the size of the cache.
     *
     * @return The size of the cache.
     */
    fun size(): Int {
        // Remove any expired entries from the cache
        clearExpired()
        return entries.size
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key of the entry.
     * @return The value associated with the key if present and not expired, otherwise null.
     */
    fun get(key: Any?): Any? {
        val entry = entries[key]
        // Checks if the entry exists and if it has not expired
        if (entry != null && clock.millis() - entry.startTime < entry.retentionInMillis) {
            return entry.value
        }
        return null
    }
}
