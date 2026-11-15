package leo.model;

import org.joml.Vector3d;

public class Satellite extends SpaceEntity {

    public static final double DEFAULT_HIT_RADIUS = 5.0;
    public static final double DEFAULT_FUEL_CAPACITY = 100.0;

    private final String callsign;
    private final double fuelCapacity;
    private double fuelRemaining;

    public Satellite(String id, String callsign, Vector3d initialPosition, Vector3d initialVelocity) {
        this(id, callsign, initialPosition, initialVelocity, DEFAULT_HIT_RADIUS, DEFAULT_FUEL_CAPACITY);
    }

    public Satellite(String id, String callsign, Vector3d initialPosition, Vector3d initialVelocity,
                     double hitRadius, double fuelCapacity) {
        super(id, initialPosition, initialVelocity, hitRadius);
        this.callsign = callsign;
        this.fuelCapacity = fuelCapacity;
        this.fuelRemaining = fuelCapacity;
    }

    public boolean hasFuel() {
        return fuelRemaining > 0.001;
    }

    public void applyThrust(Vector3d thrust) {
        if (!hasFuel()) {
            return;
        }

        double burnCost = thrust.length();
        if (burnCost > fuelRemaining) {
            double ratio = fuelRemaining / burnCost;
            velocity.add(thrust.x * ratio, thrust.y * ratio, thrust.z * ratio);
            fuelRemaining = 0.0;
        } else {
            velocity.add(thrust);
            fuelRemaining -= burnCost;
        }
    }

    public String getCallsign() {
        return callsign;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public double getFuelRemaining() {
        return fuelRemaining;
    }

    public double getFuelPercentage() {
        return fuelCapacity > 0 ? (fuelRemaining / fuelCapacity) * 100.0 : 0.0;
    }

    @Override
    public String toString() {
        return String.format("Satellite[id=%s, callsign=%s, fuel=%.1f%%, pos=(%.1f, %.1f, %.1f)]",
                id, callsign, getFuelPercentage(),
                currentPosition.x, currentPosition.y, currentPosition.z);
    }
}
