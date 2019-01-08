import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

/**
 * Created by jiajianchen
 * on 2019/1/7
 * 用于定义警察机器人
 */

public class PoliceRobot extends Agent {

    RangeSensorBelt sonars;
    CameraSensor camera;

    public PoliceRobot(Vector3d position, String name) {
        super(position,name);
        camera = RobotFactory.addCameraSensor(this);
        sonars = RobotFactory.addSonarBeltSensor(this);
    }

    public void initBehavior() {}

    /**
     * 运动算法
     */
    public void performBehavior() {

        // 瞎跑~~~~~~~
        // progress at 0.5 m/s
        setTranslationalVelocity(0.5);
        // frequently change orientation
        if ((getCounter() % 100) == 0)
            setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));

        // print front sonar every 100 frames
        if (getCounter() % 100 == 0)
            System.out.println("Sonar num 0  = " + sonars.getMeasurement(0));
    }

    /**
     * 获取当前位置
     */
    public Vector2d getPosition() {
        Point3d p = new Point3d();
        getCoords(p);
        return new Vector2d(p.z, p.x);
    }


}
