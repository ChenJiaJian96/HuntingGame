import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

import javax.vecmath.Vector3d;

/**
 * Created by jiajianchen
 * on 2019/1/8
 * 用于定义盗贼机器人
 */

public class ThiefRobot extends Agent {

    RangeSensorBelt sonars;
    RangeSensorBelt bumpers;

    private MyEnv ev;  // 环境实例，用于获取环境中的因素
    private double velocity = 0.5;  // 速度

    ThiefRobot(Vector3d position, String name) {
        super(position, name);
        sonars = RobotFactory.addSonarBeltSensor(this);
        bumpers = RobotFactory.addBumperBeltSensor(this);
    }

    void setParams(MyEnv ev, double velocity) {
        setParams(ev);
        this.velocity = velocity;
    }

    void setParams(MyEnv ev) {
        this.ev = ev;
    }

    @Override
    protected void initBehavior() {
        super.initBehavior();
    }

    /**
     * 运动策略：
     * 随机逃跑，警察靠近时选择和自己最近的警察的反方向逃跑
     */
    @Override
    protected void performBehavior() {
        setTranslationalVelocity(velocity);
        if (getCounter() % 100 == 0) {
            if (sonars.getMeasurement(1) > 0) {
                //探测到附近有物体
                System.out.println("Police1's Location is:" + ev.getP1().getPosition().toString());
                System.out.println("Police2's Location is:" + ev.getP2().getPosition().toString());
                System.out.println("Police3's Location is:" + ev.getP3().getPosition().toString());
                System.out.println("Police4's Location is:" + ev.getP4().getPosition().toString());
            }
        }
    }
}
