package leo.model;

import org.joml.Vector3d;

public abstract class SpaceEntity {

    protected final String id;
    protected final Vector3d currentPosition;
    protected final Vector3d nextPosition;
    protected final Vector3d velocity;
    protected double hitRadius;
    protected boolean isActive;

    public SpaceEntity(String id, Vector3d initialPosition, Vector3d initialVelocity, double hitRadius) {
        this.id = id;
        this.currentPosition = new Vector3d(initialPosition);
        this.nextPosition = new Vector3d(initialPosition);
        this.velocity = new Vector3d(initialVelocity);
        this.hitRadius = hitRadius;
        this.isActive = true;
    }

    public void swapBuffers() {
        this.currentPosition.set(this.nextPosition);
    }

    public String getId() {
        return id;
    }

    public Vector3d getCurrentPosition() {
        return currentPosition;
    }

    public Vector3d getNextPosition() {
        return nextPosition;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public double getHitRadius() {
        return hitRadius;
    }

    public void setHitRadius(double hitRadius) {
        this.hitRadius = hitRadius;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void reset(String newId, Vector3d newPosition, Vector3d newVelocity, double newHitRadius) {
        this.currentPosition.set(newPosition);
        this.nextPosition.set(newPosition);
        this.velocity.set(newVelocity);
        this.hitRadius = newHitRadius;
        this.isActive = true;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, pos=(%.1f, %.1f, %.1f), vel=(%.1f, %.1f, %.1f), active=%b]",
                getClass().getSimpleName(), id,
                currentPosition.x, currentPosition.y, currentPosition.z,
                velocity.x, velocity.y, velocity.z, isActive);
    }
}
