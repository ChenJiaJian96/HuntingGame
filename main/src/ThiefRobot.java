import simbad.sim.Agent;
import simbad.sim.LampActuator;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

/**
 * Created by jiajianchen
 * on 2019/1/8
 * 用于定义盗贼机器人
 */

public class ThiefRobot extends Agent {

    RangeSensorBelt sonars;
    RangeSensorBelt bumpers;
    LampActuator lamp;//灯

    private MyEnv ev;  // 环境实例，用于获取环境中的因素
    private double velocity = 0.5;  // 速度
    boolean debug = false;
    private static int freeze_time = 0;

    ThiefRobot(Vector3d position, String name) {
        super(position, name);
        sonars = RobotFactory.addSonarBeltSensor(this, 8);
        bumpers = RobotFactory.addBumperBeltSensor(this);
        lamp = RobotFactory.addLamp(this);
    }

    void setParams(MyEnv ev, double velocity, boolean debug) {
        setParams(ev);
        this.velocity = velocity;
        this.debug = debug;
    }

    private void setParams(MyEnv ev) {
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

        if (getCounter() % 10 == 0) {

            if (ev.getThiefState() == 2) {
                if (debug)
                    System.out.println("GG...我被抓住了！！！");
                setRotationalVelocity(0);
                setTranslationalVelocity(0);
            } else {
                // 更新领航机器人
                ev.updateLeader();

                // 开始漫无目的地移动
                walkNoGoal();
            }
        }
    }

    /**
     * 判断机器人是否达到平衡点而导致无法移动
     * 人工势场法无法避免的bug
     * 机器人受力为零
     */
    private boolean isStopForLong(Vector2d pre, Vector2d cur) {
        if (cur.equals(pre)) {
            if (freeze_time == 30)
                // 如果边界设置太小会影响正常的碰撞处理
                return true;
            else {
                freeze_time++;
                return false;
            }
        } else {
            freeze_time = 0;
            return false;
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
     * 漫无目的地移动，避开障碍物
     * 人工势场法
     */
    private void walkNoGoal() {
        setTranslationalVelocity(this.velocity);

        // 获取速度
        Vector3d velocity = this.linearVelocity;

        // 获取当前运动方向
        Vector2d direct = new Vector2d(velocity.z, velocity.x);

        // 获取前一次运动位置
        Point3d p = new Point3d();
        getCoords(p);
        Vector2d pre_pos = new Vector2d(p.z, p.x);

        Vector2d composition = new Vector2d(0, 0);

        for (int i = 0; i < 8; i++) {
            double d = sonars.getMeasurement(i);
            double rf = MyUtil.repelForce(d);
            if (rf > 0) {
                double k1 = Math.cos(i * Math.PI / 4);
                double k2 = Math.sin(i * Math.PI / 4);
                Vector2d vf = new Vector2d(rf * -k1, rf * -k2);
                composition.set(composition.x + vf.x, composition.y + vf.y);
            }
        }

//            if (debug)
//                System.out.println("斥力：(" + composition.x + ","
//                        + composition.y);

        //给定斥力合力向量，此处合力就是斥力
        Vector2d allForces = MyUtil.transform(direct, composition);
        // 根据合力的方向和当前运动方向的夹角来判断当前转动角度
        double angle = MyUtil.getAngle(direct, allForces);

//        if (debug)
//            System.out.println("旋转角度：(" + angle);

        // 判断转动方向
        if (angle < Math.PI) {
            setRotationalVelocity(angle);
        } else if (angle > Math.PI) {
            setRotationalVelocity((angle - 2 * Math.PI));
        }

        // 碰撞情况处理
        // 检测是否碰撞
        if (bumpers.oneHasHit()) {
            if (debug)
                System.out.println(this.name + "撞到了！！！");
            // 机器人身体三个方向和障碍物的距离
            double left = sonars.getFrontLeftQuadrantMeasurement();
            double right = sonars.getFrontRightQuadrantMeasurement();
            double front = sonars.getFrontQuadrantMeasurement();
            // 如果接近障碍物
            if ((front < 1) || (left < 1) || (right < 1)) {
                setTranslationalVelocity(0);
                if (left < right) {
                    setRotationalVelocity(-1 - (0.1 * Math.random()));// 随机向右转
                } else {
                    setRotationalVelocity(1 - (0.1 * Math.random()));// 随机向左转
                }
            }
        }

        // 判断是否一直不动
        // 获取前一次运动位置
        getCoords(p);
        Vector2d cur_pos = new Vector2d(p.z, p.x);
        if (isStopForLong(pre_pos, cur_pos)) {
            if (debug)
                System.out.println(this.name + "卡住了！！！");
            setTranslationalVelocity(0);
            setRotationalVelocity(Math.PI);
            freeze_time = 0;
        }
    }
}
