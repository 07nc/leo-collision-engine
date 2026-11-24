package leo.spatial;
import java.util.ArrayList;
import java.util.List;
import leo.model.SpaceEntity;

public class VoxelBucket {
    private final int x;
    private final int y;
    private final int z;
    private final List<SpaceEntity> entities;
    public VoxelBucket(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.entities = new ArrayList<>();
    }
    public void addEntity(SpaceEntity entity) {
        entities.add(entity);
    }
    public List<SpaceEntity> getEntities() {
        return entities;
    }
    public boolean matches(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }
}
