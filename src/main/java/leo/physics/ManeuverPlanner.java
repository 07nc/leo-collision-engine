package leo.physics;

import leo.model.SpaceEntity;
import org.joml.Vector3d;

public class ManeuverPlanner {

    public static final double DEFAULT_DODGE_IMPULSE = 5.0;

    private ManeuverPlanner() {}

    public static Vector3d calculateDodge(SpaceEntity entity, SpaceEntity threat) {
        return calculateDodge(entity, threat, DEFAULT_DODGE_IMPULSE);
    }

    public static Vector3d calculateDodge(SpaceEntity entity, SpaceEntity threat, double impulseMagnitude) {
        Vector3d dodgeDir = new Vector3d(entity.getNextPosition()).sub(threat.getNextPosition());

        double dist = dodgeDir.length();
        if (dist < 1e-6) {
            Vector3d pos = entity.getCurrentPosition();
            if (pos.length() > 1e-6) {
                dodgeDir.set(pos).normalize();
            } else {
                dodgeDir.set(0.0, 1.0, 0.0);
            }
        } else {
            dodgeDir.normalize();
        }

        return dodgeDir.mul(impulseMagnitude);
    }
}
