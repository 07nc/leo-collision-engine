package leo.event;

public class ManeuverEvent {

    public final String satelliteId;
    public final String threatId;
    public final double deltaV;
    public final double fuelRemaining;
    public final long tick;

    public ManeuverEvent(String satelliteId, String threatId, double deltaV, double fuelRemaining, long tick) {
        this.satelliteId = satelliteId;
        this.threatId = threatId;
        this.deltaV = deltaV;
        this.fuelRemaining = fuelRemaining;
        this.tick = tick;
    }

    @Override
    public String toString() {
        return String.format("[BURN] Tick %d: %s dodged %s (Delta-V: %.1fm/s, Fuel Left: %.1f%%)",
                tick, satelliteId, threatId, deltaV, fuelRemaining);
    }
}

