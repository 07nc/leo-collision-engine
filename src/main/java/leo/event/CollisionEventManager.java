package leo.event;

import java.util.ArrayList;
import java.util.List;

public class CollisionEventManager {

public interface Listener{

    void onCollisionWarning(CollisionWarningEvent event);
    void onManeuver(ManeuverEvent event);

}

private final List<Listener> listeners=new ArrayList<>();

public void addListener(Listener listener){

    listeners.add(listener);

}

public void fireEvent(Object event){
    for(Listener listener: listeners){

        if(event instanceof CollisionWarningEvent warning){
            listener.onCollisionWarning(warning);
        }
        else if(event instanceof ManeuverEvent maneuver){
            listener.onManeuver(maneuver);
        }
        
    }
}

}