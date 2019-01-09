import simbad.sim.Arch;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * Created by jiajianchen
 * on 2019/1/7
 * 用于定义场景，并启动机器人构建
 */

public class MyEnv extends EnvironmentDescription {

    private ThiefRobot thiefRobot;
    private PoliceRobot p1;
    private PoliceRobot p2;
    private PoliceRobot p3;
    private PoliceRobot p4;

    private boolean debug;  // 调试模式
    private double police_speed = 1;
    private double thief_speed = 1;

    MyEnv(boolean d) {
        this.debug = d;
        // 外围墙壁
        Wall w1 = new Wall(new Vector3d(10, 0, 0), 20, 1, this);
        w1.rotate90(1);
        add(w1);
        Wall w2 = new Wall(new Vector3d(-10, 0, 0), 20, 1, this);
        w2.rotate90(1);
        add(w2);
        Wall w3 = new Wall(new Vector3d(0, 0, 10), 20, 1, this);
        add(w3);
        Wall w4 = new Wall(new Vector3d(0, 0, -10), 20, 1, this);
        add(w4);

        // 构造障碍物
        buildObstacles();

        // 场景构建成功后构建机器人
        thiefRobot = new ThiefRobot(new Vector3d(0, 0, 0), "THIEF");
        thiefRobot.setParams(this, thief_speed, debug);
        add(thiefRobot);
//        p1 = new PoliceRobot(new Vector3d(-9, 0, -9), "POLICE1");
//        p1.setParams(this, police_speed, debug);
//        add(p1);
//        p2 = new PoliceRobot(new Vector3d(9, 0, -9), "POLICE2");
//        add(p2);
//        p3 = new PoliceRobot(new Vector3d(9, 0, 9), "POLICE3");
//        add(p3);
//        p4 = new PoliceRobot(new Vector3d(-9, 0, 9), "POLICE4");
//        add(p4);
    }

    private void buildObstacles() {
        //墙型障碍物
        Wall w7 = new Wall(new Vector3d(-7, 0, 7), 3, 1, this);
        w7.rotate90(1);
        add(w7);
        Wall w8 = new Wall(new Vector3d(7, 0, -7), 3, 1, this);
        w8.rotate90(1);
        add(w8);
        Wall w9 = new Wall(new Vector3d(-7, 0, -7), 3, 1, this);
        w9.rotate90(1);
        add(w9);
        Wall w10 = new Wall(new Vector3d(7, 0, 7), 2, 1, this);
        w10.rotate90(1);
        add(w10);


        //盒子型障碍物
//        Box b1 = new Box(new Vector3d(0,0,0), new Vector3f(3, 1, 3),this);
//        add(b1);
        Box b2 = new Box(new Vector3d(-4,0,-4), new Vector3f(1, 3, 1),this);
        add(b2);
        Box b3 = new Box(new Vector3d(4,0,4), new Vector3f(1, 3, 1),this);
        add(b3);
        Box b4 = new Box(new Vector3d(-4,0,4), new Vector3f(1, 3, 1),this);
        add (b4);
        Box b5 = new Box(new Vector3d(4,0,-4), new Vector3f(1, 3, 1),this);
        add(b5);

        //桥型障碍物
        Arch a1=new Arch(new Vector3d(0,0,6),this);
        a1.rotate90(2);
        add(a1);
        Arch a2=new Arch(new Vector3d(0,0,-6),this);
        a2.rotate90(4);
        add(a2);
    }

    /**
     * 定义接口定义机器人速度
     *
     * @param ps 警察速度
     * @param ts 盗贼速度
     */
    void setSpeed(double ps, double ts) {
        this.police_speed = ps;
        this.thief_speed = ts;
    }

    /**
     * 定义公共接口获取机器人实例
     *
     * @return 机器人实例
     */
    public ThiefRobot getThiefRobot() {
        return thiefRobot;
    }

    public PoliceRobot getP1() {
        return p1;
    }

    public PoliceRobot getP2() {
        return p2;
    }

    public PoliceRobot getP3() {
        return p3;
    }

    public PoliceRobot getP4() {
        return p4;
    }
}
