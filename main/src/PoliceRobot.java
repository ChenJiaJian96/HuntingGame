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

    private MyEnv ev;  // 环境实例，用于获取环境中的因素
    private double velocity = 0.5;      // 速度
    private boolean debug = false;      // 调试状态
    private boolean isLeader = false;   // 是否为领航机器人
    private Vector2d targetPoint = new Vector2d();      // 目标点

    public PoliceRobot(Vector3d position, String name) {
        super(position,name);
        camera = RobotFactory.addCameraSensor(this);
        sonars = RobotFactory.addSonarBeltSensor(this);
    }

    public void initBehavior() {}

    void setParams(MyEnv ev, double velocity, boolean debug) {
        this.ev = ev;
        this.velocity = velocity;
        this.debug = debug;
    }


    /**
     * 运动算法
     */
    public void performBehavior() {

        if ((getCounter() % 100) == 0) {
            if (ev.isThiefCaught()) {
                if (debug)
                    System.out.println("舒服，盗贼被抓住了！！！");
                setRotationalVelocity(0);
                setTranslationalVelocity(0);
            } else {
                setTranslationalVelocity(velocity);
            }
        }
    }

    /**
     * 获取当前位置
     */
    Vector2d getPosition() {
        Point3d p = new Point3d();
        getCoords(p);
        return new Vector2d(p.z, p.x);
    }

    /**
     * 设置领航状态
     */
    void setIsLeader(boolean status) {
        isLeader = status;
    }

    /**
     * 设置目标点
     *
     * @param targetPoint
     */
    public void setTargetPoint(Vector2d targetPoint) {
        this.targetPoint = targetPoint;
    }
}
