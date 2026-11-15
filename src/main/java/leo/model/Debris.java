package leo.model;

import org.joml.Vector3d;

public class Debris extends SpaceEntity {

    public static final double DEFAULT_HIT_RADIUS = 2.0;

    public Debris(String id, Vector3d initialPosition, Vector3d initialVelocity) {
        this(id, initialPosition, initialVelocity, DEFAULT_HIT_RADIUS);
    }

    public Debris(String id, Vector3d initialPosition, Vector3d initialVelocity, double hitRadius) {
        super(id, initialPosition, initialVelocity, hitRadius);
    }
}
