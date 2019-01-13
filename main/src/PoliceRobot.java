import simbad.sim.Agent;
import simbad.sim.LampActuator;
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
    RangeSensorBelt bumpers;
    LampActuator lamp;//灯

    private MyEnv ev;  // 环境实例，用于获取环境中的因素
    private double velocity = 0.5;      // 速度
    private boolean debug = false;      // 调试状态
    private boolean isLeader = false;   // 是否为领航机器人
    private Vector2d targetPoint = new Vector2d(0, 0);      // 目标点

    public PoliceRobot(Vector3d position, String name) {
        super(position,name);
        sonars = RobotFactory.addSonarBeltSensor(this, 8);
        bumpers = RobotFactory.addBumperBeltSensor(this);
        lamp = RobotFactory.addLamp(this);
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

        if ((getCounter() % 10) == 0) {
            if (ev.getThiefState() == 2) {
                if (debug)
                    System.out.println("盗贼被抓住了！！！");
                setRotationalVelocity(0);
                setTranslationalVelocity(0);
            } else {
                if (ev.getThiefState() == 0)
                    walkWithGoal(targetPoint);
                else walkWithGoal(ev.getThiefRobot().getPosition());
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
    void setTargetPoint(Vector2d targetPoint) {
        this.targetPoint = targetPoint;
    }

    Vector2d getTargetPoint() {
        return targetPoint;
    }

    private void walkWithGoal(Vector2d goal) {

        setTranslationalVelocity(this.velocity);
        // 获取速度
        Vector3d velocity = this.linearVelocity;

        // 获取当前运动方向
        Vector2d direct = new Vector2d(velocity.z, velocity.x);

        // 获取当前运动位置
        Point3d p = new Point3d();
        getCoords(p);
        Vector2d pos = new Vector2d(p.z, p.x);

        //获取三方个障碍物的距离
        double d0 = sonars.getMeasurement(0);// front声纳，正前方
        double d1 = sonars.getMeasurement(1);// frontleft声纳，左前方
        double d2 = sonars.getMeasurement(7);// frontright声纳，右前方

        // 计算三个方向障碍物的斥力
        double rf0 = MyUtil.repelForce(d0);
        double rf1 = MyUtil.repelForce(d1);
        double rf2 = MyUtil.repelForce(d2);

        //计算三个方向障碍物斥力的合力
        double k1 = Math.cos(2 * Math.PI / 9);
        double k2 = Math.sin(2 * Math.PI / 9);
        Vector2d vf0 = new Vector2d(0 - rf0, 0);
        Vector2d vf1 = new Vector2d((0 - rf1 * k1), (0 - rf1 * k2));
        Vector2d vf2 = new Vector2d((rf2 * k1), (rf2 * k2));
        Vector2d composition = new Vector2d();
        composition.setX(vf0.x + vf1.x + vf2.x);
        composition.setY(vf0.y + vf1.y + vf2.y);

        if (debug)
            System.out.println("(" + composition.x + ","
                    + composition.y);

        //给定斥力合力向量
        Vector2d repelForceVector = MyUtil.transform(direct, composition);

        // 计算目标的对机器人的引力
        Vector2d toGoal = new Vector2d((goal.x - pos.x),
                (goal.y - pos.y));
        double disGoal = toGoal.length();//和目的的距离
        if (debug)
            System.out.println("distance to goal:" + disGoal);
        double goalForce = MyUtil.attractForce(disGoal);//计算引力

        if (debug)
            System.out.println("attract force from goal:" + goalForce);
        Vector2d goalForceVector = new Vector2d(
                (goalForce * toGoal.x / disGoal),
                (goalForce * toGoal.y / disGoal));

        // 计算斥力和引力的合力
        double x = repelForceVector.x + goalForceVector.x;
        double y = repelForceVector.y + goalForceVector.y;

        Vector2d allForces = new Vector2d(x, y);
        if (debug) {
            System.out.println("total force(" + allForces.x + ","
                    + allForces.y + ")");
            System.out.println("force direct(" + direct.x + ","
                    + direct.y + ")");
        }
        // 根据合力的方向和当前运动方向的夹角来判断当前转动角度
        double angle = MyUtil.getAngle(direct, allForces);

        if (debug)
            System.out.println("angle:" + angle);

        // 判断转动方向
        if (angle < Math.PI) {
            setRotationalVelocity(angle);
        } else if (angle > Math.PI) {
            setRotationalVelocity((angle - 2 * Math.PI));
        }

        // 是否到达目标点
        if (checkGoal(new Point3d(goal.x, 0, goal.y))) {
            // 到达目标点，亮绿灯 停止运动
            setTranslationalVelocity(0);
            setRotationalVelocity(0);
//            lamp.setOn(true);
            return;
        } else {
//            lamp.setOn(false);
            setTranslationalVelocity(0.5);
        }

        // 检测是否碰撞
        if (bumpers.oneHasHit()) {
            lamp.setBlink(true);
            // 机器人身体三个方向和障碍物的距离
            double left = sonars.getFrontLeftQuadrantMeasurement();
            double right = sonars.getFrontRightQuadrantMeasurement();
            double front = sonars.getFrontQuadrantMeasurement();
            // 如果接近障碍物
            if ((front < 0.7) || (left < 0.7) || (right < 0.7)) {
                setTranslationalVelocity(0);
                if (left < right) {
                    setRotationalVelocity(-1 - (0.1 * Math.random()));// 随机向右转
                } else {
                    setRotationalVelocity(1 - (0.1 * Math.random()));// 随机向左转
                }
            }
        } else
            lamp.setBlink(false);
    }

    private boolean checkGoal(Point3d goal3d)//检查是否到达目标
    {
        // 当前位置
        Point3d currentPos = new Point3d();
        getCoords(currentPos);
        Point3d goalPos = new Point3d(goal3d.x, goal3d.y, goal3d.z);

        // 如果当前距离目标点小于0.5那么即认为是到达
        return currentPos.distance(goalPos) <= 0.5;
    }
}
