package leo.event;

import java.util.LinkedList;
import java.util.List;

public class TerminalDashboard implements CollisionEventManager.Listener {

    private static final String RESET   = "\u001B[0m";
    private static final String BOLD    = "\u001B[1m";
    private static final String RED     = "\u001B[31m";
    private static final String GREEN   = "\u001B[32m";
    private static final String YELLOW  = "\u001B[33m";
    private static final String CYAN    = "\u001B[36m";
    private static final String WHITE   = "\u001B[37m";
    private static final String CLEAR   = "\033[H\033[2J";

    private static final int MAX_LOG_SIZE = 8;
    private final List<String> eventLog = new LinkedList<>();

    private int criticalCount = 0;
    private int warningCount = 0;
    private int maneuverCount = 0;

    @Override 
    public void onCollisionWarning(CollisionWarningEvent event) {
        if (event.severity == CollisionWarningEvent.Severity.CRITICAL) {
            criticalCount++;
            addLogEntry(RED + BOLD + event + RESET);
        } else {
            warningCount++;
            addLogEntry(YELLOW + event + RESET);
        }
    }

    @Override
    public void onManeuver(ManeuverEvent event) {
        maneuverCount++;
        addLogEntry(GREEN + BOLD + event + RESET);
    }

    private void addLogEntry(String entry) {
        if (eventLog.size() >= MAX_LOG_SIZE) {
            eventLog.remove(0);
        }
        eventLog.add(entry);
    }

    public void render(long tick, int satelliteCount, int debrisCount, double tps) {
        StringBuilder sb = new StringBuilder();
        sb.append(CLEAR);

        sb.append(CYAN).append("================================================================================\n").append(RESET);
        sb.append(BOLD).append(WHITE).append("           🛰️  LEO SATELLITE COLLISION AVOIDANCE ENGINE  🛰️\n").append(RESET);
        sb.append(CYAN).append("================================================================================\n").append(RESET);

        long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        int threadCount = Thread.activeCount();
        int totalEntities = satelliteCount + debrisCount;

        sb.append(String.format(" %sTICK:%s %06d | %sSATS:%s %,d | %sDEBRIS:%s %,d | %sTOTAL:%s %,d | %sTPS:%s %.1f | %sRAM:%s %dMB | %sTHREADS:%s %d\n",
                BOLD, RESET, tick,
                BOLD, RESET, satelliteCount,
                BOLD, RESET, debrisCount,
                BOLD, RESET, totalEntities,
                BOLD, RESET, tps,
                BOLD, RESET, usedMemory,
                BOLD, RESET, threadCount));

        sb.append(CYAN).append("================================================================================\n").append(RESET);
        sb.append(BOLD).append(" LIVE EVENT & AVOIDANCE LOG:\n").append(RESET);
        sb.append("--------------------------------------------------------------------------------\n");

        for (String log : eventLog) {
            sb.append("  ").append(log).append("\n");
        }

        for (int i = eventLog.size(); i < MAX_LOG_SIZE; i++) {
            sb.append("\n");
        }

        sb.append(CYAN).append("================================================================================\n").append(RESET);
        sb.append(String.format(" TOTAL STATS | %sCritical: %d%s | %sWarnings: %d%s | %sAvoidance Burns: %d%s\n",
                RED, criticalCount, RESET,
                YELLOW, warningCount, RESET,
                GREEN, maneuverCount, RESET));
        sb.append(CYAN).append("================================================================================\n").append(RESET);

        System.out.print(sb.toString());
        System.out.flush();
    }

}
