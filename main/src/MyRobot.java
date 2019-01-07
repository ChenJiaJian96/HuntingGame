import simbad.sim.Agent;

import javax.vecmath.Vector3d;

public class MyRobot extends Agent {

    public MyRobot (Vector3d position, String name) {
        super(position,name);
    }
    public void initBehavior() {}

    public void performBehavior() {
        if (collisionDetected()) {
            // stop the robot
            setTranslationalVelocity(0.0);
            setRotationalVelocity(0);
        } else {
            // progress at 0.5 m/s
            setTranslationalVelocity(0.5);
            // frequently change orientation
            if ((getCounter() % 100)==0)
                setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        }
    }
}
