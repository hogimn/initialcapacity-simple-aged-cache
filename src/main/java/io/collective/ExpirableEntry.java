package io.collective;

/**
 * Represents an entry that expires after a certain retention time.
 */
public class ExpirableEntry {

    // Represents the retention time of the entry in milliseconds
    long retentionInMillis;

    // Represents the start time of the entry in milliseconds
    long startTime;

    // Represents the entry object
    private final Object entry;

    /**
     * Constructor that initializes the ExpirableEntry with the specified start time, retention time, and entry object.
     *
     * @param startTime         The start time of the entry in milliseconds.
     * @param retentionInMillis The retention time of the entry in milliseconds.
     * @param entry             The entry object.
     */
    public ExpirableEntry(long startTime, long retentionInMillis, Object entry) {
        this.startTime = startTime;
        this.retentionInMillis = retentionInMillis;
        this.entry = entry;
    }

    /**
     * Returns the entry object.
     *
     * @return The entry object.
     */
    public Object getValue() {
        return entry;
    }
}
