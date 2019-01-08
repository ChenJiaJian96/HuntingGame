import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

import javax.vecmath.Vector3d;

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

    public MyEnv(){
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

        // 场景构建成功后构建机器人
        thiefRobot = new ThiefRobot(new Vector3d(0, 0, 0), "THIEF");
        thiefRobot.setParams(this, 1);
        add(thiefRobot);
        p1 = new PoliceRobot(new Vector3d(-9, 0, -9),"POLICE1");
        add(p1);
        p2 = new PoliceRobot(new Vector3d(9, 0, -9),"POLICE2");
        add(p2);
        p3 = new PoliceRobot(new Vector3d(9, 0, 9),"POLICE3");
        add(p3);
        p4 = new PoliceRobot(new Vector3d(-9, 0, 9),"POLICE4");
        add(p4);
    }

    /**
     * 定义公共接口获取机器人实例
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
