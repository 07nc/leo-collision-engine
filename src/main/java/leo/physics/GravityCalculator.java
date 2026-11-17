package leo.physics;

import leo.model.SpaceEntity;
import org.joml.Vector3d;

public class GravityCalculator {

    public static final double MU = 3.986004418e14;
    public static final double EARTH_RADIUS = 6371000.0;

    private GravityCalculator() {}

    public static Vector3d calculateAcceleration(Vector3d position) {
        double rDist = position.length();
        if (rDist < 1.0) {
            return new Vector3d(0.0, 0.0, 0.0);
        }
        double factor = -MU / (rDist * rDist * rDist);
        return new Vector3d(position.x * factor, position.y * factor, position.z * factor);
    }

    public static void applyEuler(SpaceEntity entity, double dt) {
        Vector3d pos = entity.getCurrentPosition();
        Vector3d vel = entity.getVelocity();

        Vector3d accel = calculateAcceleration(pos);

        vel.add(accel.x * dt, accel.y * dt, accel.z * dt);

        entity.getNextPosition().set(
                pos.x + vel.x * dt,
                pos.y + vel.y * dt,
                pos.z + vel.z * dt
        );
    }

    public static void applyPhysics(SpaceEntity entity, double dt) {
        applyEuler(entity, dt);
    }
}
