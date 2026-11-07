package leo.event;

public class CollisionWarningEvent {

    public enum Severity {
        CRITICAL,
        WARNING
    }

    public final String entityAId;
    public final String entityBId;
    public final double distance;
    public final long tick;
    public final Severity severity;

    public CollisionWarningEvent(String entityAId, String entityBId, double distance, long tick) {
        this.entityAId = entityAId;
        this.entityBId = entityBId;
        this.distance = distance;
        this.tick = tick;
        this.severity = (distance < 50.0) ? Severity.CRITICAL : Severity.WARNING;
    }

    @Override
    public String toString() {
        return String.format("[%s] Tick %d: %s <-> %s (Distance: %.2fm)",
                severity, tick, entityAId, entityBId, distance);
    }
}

