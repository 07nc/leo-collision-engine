package leo.spatial;
import java.util.List;
import java.util.ArrayList;
import leo.model.SpaceEntity;
import org.joml.Vector3d;

public class VoxelGrid {
    private final double cellSize;
    private final int bucketCount;
    private final List<VoxelBucket>[] buckets;
    @SuppressWarnings("unchecked")
    public VoxelGrid(double cellSize,  int bucketCount){
        if (cellSize <= 0 || !Double.isFinite(cellSize)) {
            throw new IllegalArgumentException("Invalid cell size");
        }
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("Invalid bucket count");
        }
        this.cellSize = cellSize;
        this.bucketCount = bucketCount;
        this.buckets = (List<VoxelBucket>[]) new List<?>[bucketCount];
        for(int i = 0; i < bucketCount; i++){
            buckets[i] = new ArrayList<>();
        }
    }
    public int[] getCellCoordinates(double x, double y, double z){
        int cellX = (int) Math.floor(x / cellSize);
        int cellY = (int) Math.floor(y / cellSize);
        int cellZ = (int) Math.floor(z / cellSize);
        return new int[]{cellX, cellY, cellZ};
    }
    public int computeHash(int x, int y, int z){
        int hash = (x*73856093) ^ (y*19349663) ^ (z*83492791);
        return Math.floorMod(hash, bucketCount);
    }
    public void insert(SpaceEntity entity){
        if (entity == null || entity.getCurrentPosition() == null) {
            throw new IllegalArgumentException("Entity and position cannot be null");
        }
        if (!Double.isFinite(entity.getCurrentPosition().x) || !Double.isFinite(entity.getCurrentPosition().y) || !Double.isFinite(entity.getCurrentPosition().z)) {
            throw new IllegalArgumentException("Position must contain finite coordinates");
        }
        int[] coordinates = getCellCoordinates(entity.getCurrentPosition().x, entity.getCurrentPosition().y, entity.getCurrentPosition().z);
        int x = coordinates[0];
        int y = coordinates[1];
        int z = coordinates[2];
        int index = computeHash(x, y, z);
        List<VoxelBucket> bucketList = buckets[index];
        for(VoxelBucket bucket : bucketList){
            if (bucket.matches(x, y, z)){
                bucket.addEntity(entity);
                return;
            }
        }
        VoxelBucket newBucket = new VoxelBucket(x, y, z);
        newBucket.addEntity(entity);
        bucketList.add(newBucket);
    }
    public List<SpaceEntity> getNeighbors(Vector3d pos) {
        if (pos == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (!Double.isFinite(pos.x) || !Double.isFinite(pos.y) || !Double.isFinite(pos.z)){
            throw new IllegalArgumentException("Position must contain finite coordinates");
        }
        int[] coordinates = getCellCoordinates(pos.x, pos.y, pos.z);
        int centerX = coordinates[0];
        int centerY = coordinates[1];
        int centerZ = coordinates[2];
        List<SpaceEntity> neighbors = new ArrayList<>();
        for (int x = centerX - 1; x <= centerX + 1; x++) {
            for (int y = centerY - 1; y <= centerY + 1; y++) {
                for (int z = centerZ - 1; z <= centerZ + 1; z++) {
                    int index = computeHash(x, y, z);
                    List<VoxelBucket> bucketList = buckets[index];
                    for (VoxelBucket bucket : bucketList) {
                        if (bucket.matches(x, y, z)) {
                            neighbors.addAll(bucket.getEntities());
                        }
                    }
                }
            }
        }
        return neighbors;
    }
    public void clear() {
        for (List<VoxelBucket> bucketList : buckets) {
            bucketList.clear();
        }
    }
}
