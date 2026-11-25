package leo.engine;

import leo.event.CollisionEventManager;
import leo.event.CollisionWarningEvent;
import leo.event.ManeuverEvent;
import leo.event.TerminalDashboard;
import leo.model.Satellite;
import leo.model.SpaceEntity;
import leo.physics.GravityCalculator;
import leo.physics.ManeuverPlanner;
import leo.spatial.VoxelGrid;
import org.joml.Vector3d;

import java.util.List;

public class SimulationMaster {

    private static final double COLLISION_THRESHOLD = 100.0;
    private static final double DT = 1.0;

    private final List<SpaceEntity> entities;
    private final VoxelGrid voxelGrid;
    private final CollisionEventManager eventManager;
    private final TerminalDashboard dashboard;
    private final int satelliteCount;
    private long tick = 0;

    public SimulationMaster(List<SpaceEntity> entities, VoxelGrid voxelGrid, 
                            CollisionEventManager eventManager, TerminalDashboard dashboard) {
        this.entities = entities;
        this.voxelGrid = voxelGrid;
        this.eventManager = eventManager;
        this.dashboard = dashboard;
        this.eventManager.addListener(dashboard);

        int sats = 0;
        for (SpaceEntity e : entities) {
            if (e instanceof Satellite) sats++;
        }
        this.satelliteCount = sats;
    }

    public void tick() {
        tick++;

        // update gravity and insert into grid
        for (SpaceEntity entity : entities) {
            if (!entity.isActive()) continue;
            GravityCalculator.applyPhysics(entity, DT);
            voxelGrid.insert(entity);
        }

        // check nearby objects for close passes
        for (SpaceEntity entity : entities) {
            if (!entity.isActive()) continue;

            List<SpaceEntity> neighbors = voxelGrid.getNeighbors(entity.getCurrentPosition());
            for (SpaceEntity neighbor : neighbors) {
                if (entity == neighbor || !neighbor.isActive()) continue;

                // avoid checking the same pair twice
                if (entity.getId().compareTo(neighbor.getId()) >= 0) continue;

                double distance = entity.getCurrentPosition().distance(neighbor.getCurrentPosition());
                double combinedRadius = entity.getHitRadius() + neighbor.getHitRadius();
                double gap = distance - combinedRadius;

                if (gap < COLLISION_THRESHOLD) {
                    eventManager.fireEvent(new CollisionWarningEvent(entity.getId(), neighbor.getId(), gap, tick));

                    if (entity instanceof Satellite sat && sat.hasFuel()) {
                        Vector3d thrust = ManeuverPlanner.calculateDodge(entity, neighbor);
                        sat.applyThrust(thrust);
                        eventManager.fireEvent(new ManeuverEvent(sat.getId(), neighbor.getId(), thrust.length(), sat.getFuelPercentage(), tick));
                    } else if (neighbor instanceof Satellite satNeighbor && satNeighbor.hasFuel()) {
                        Vector3d thrust = ManeuverPlanner.calculateDodge(neighbor, entity);
                        satNeighbor.applyThrust(thrust);
                        eventManager.fireEvent(new ManeuverEvent(satNeighbor.getId(), entity.getId(), thrust.length(), satNeighbor.getFuelPercentage(), tick));
                    }
                }
            }
        }

        // update position buffers
        for (SpaceEntity entity : entities) {
            if (entity.isActive()) {
                entity.swapBuffers();
            }
        }

        // reset grid for next tick
        voxelGrid.clear();

        // render dashboard
        dashboard.render(tick, satelliteCount, entities.size() - satelliteCount, 10.0);
    }

    public void start() {
        while (true) {
            tick();
            try {
                Thread.sleep(100); // 10 tps
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
