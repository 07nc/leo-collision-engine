package leo.memory;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import leo.model.Debris;
import org.joml.Vector3d;

public class EntityPool {
    private final Queue<Debris> pool;
    private final Set<Debris> ownedEntities;
    private final int capacity;
    public EntityPool(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be positive"
            );
        }
        this.capacity = capacity;
        this.pool = new ArrayDeque<>();
        this.ownedEntities = new HashSet<>();
        for (int i = 0; i < capacity; i++) {
            Debris debris = new Debris(
                "POOL-" + i,
                new Vector3d(),
                new Vector3d()
            );
            debris.setActive(false);
            pool.offer(debris);
            ownedEntities.add(debris);
        }
    }
    public Debris acquire() {
        Debris debris = pool.poll();
        if (debris == null) {
            return null;
        }
        debris.setActive(true);
        return debris;
    }
    public void release(Debris debris) {
        if (debris == null) {
            throw new IllegalArgumentException(
                "Debris cannot be null"
            );
        }
        if (!ownedEntities.contains(debris)) {
            throw new IllegalArgumentException(
                "Debris does not belong to this pool"
            );
        }
        if (!debris.isActive()) {
            throw new IllegalStateException(
                "Debris is already inactive"
            );
        }
        debris.setActive(false);
        pool.offer(debris);
    }
    public int getCapacity() {
        return capacity;
    }
    public int getAvailableCount() {
        return pool.size();
    }
}
