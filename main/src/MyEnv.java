import simbad.sim.Arch;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.POSITIVE_INFINITY;

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
    private ArrayList<PoliceRobot> PRs = new ArrayList<>();
    private Vector2d[] catchPosition;

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

//        buildObstacles();

        // 场景构建成功后构建机器人
        thiefRobot = new ThiefRobot(new Vector3d(0, 0, 0), "THIEF");
        thiefRobot.setParams(this, thief_speed, debug);
        add(thiefRobot);
        p1 = new PoliceRobot(new Vector3d(-9, 0, -9), "POLICE1");
        p1.setParams(this, police_speed, debug);
        add(p1);
        p2 = new PoliceRobot(new Vector3d(9, 0, -9), "POLICE2");
        p2.setParams(this, police_speed, debug);
        add(p2);
        p3 = new PoliceRobot(new Vector3d(9, 0, 9), "POLICE3");
        p3.setParams(this, police_speed, debug);
        add(p3);
        PRs.add(p1);
        PRs.add(p2);
        PRs.add(p3);
    }

    /**
     * 生成障碍物
     */
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
        Box b2 = new Box(new Vector3d(-4, 0, -4), new Vector3f(1, 3, 1), this);
        add(b2);
        Box b3 = new Box(new Vector3d(4, 0, 4), new Vector3f(1, 3, 1), this);
        add(b3);
        Box b4 = new Box(new Vector3d(-4, 0, 4), new Vector3f(1, 3, 1), this);
        add(b4);
        Box b5 = new Box(new Vector3d(4, 0, -4), new Vector3f(1, 3, 1), this);
        add(b5);

        //桥型障碍物
        Arch a1 = new Arch(new Vector3d(0, 0, 6), this);
        a1.rotate90(2);
        add(a1);
        Arch a2 = new Arch(new Vector3d(0, 0, -6), this);
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

    public Vector2d[] getCatchPosition() {
        return catchPosition;
    }

    /**
     * 定时更新领航警察机器人
     */
    void updateLeader() {
        Vector2d tp = thiefRobot.getPosition();
        Vector2d pp1 = p1.getPosition();
        Vector2d pp2 = p1.getPosition();
        Vector2d pp3 = p3.getPosition();
        double[] result = new double[3];
        List<Vector2d[]> cPList = new ArrayList<>(3);
        if (MyUtil.cal_point_line(pp2, pp3, pp1, tp) == -1) {
            // 除去警察1以外两个警察处于两侧
            Vector2d[] catchPosition = MyUtil.getCatchPosition(tp, pp1, thiefRobot.getRadius());
            cPList.add(catchPosition);
            Vector2d v1 = catchPosition[1];
            Vector2d v2 = catchPosition[2];
            double result1 = new Vector2d(pp2.x - v1.x, pp2.y - v1.y).length() + new Vector2d(pp3.x - v2.x, pp3.y - v2.y).length();
            double result2 = new Vector2d(pp2.x - v2.x, pp2.y - v2.y).length() + new Vector2d(pp3.x - v1.x, pp3.y - v1.y).length();
            result[0] = result1 < result2 ? result1 : result2;
        } else {
            result[0] = POSITIVE_INFINITY;
            cPList.add(new Vector2d[]{});
        }
        if (MyUtil.cal_point_line(pp1, pp3, pp2, tp) == -1) {
            // 除去警察2以外两个警察处于两侧
            Vector2d[] catchPosition = MyUtil.getCatchPosition(tp, pp2, thiefRobot.getRadius());
            cPList.add(catchPosition);
            Vector2d v1 = catchPosition[1];
            Vector2d v2 = catchPosition[2];
            double result1 = new Vector2d(pp1.x - v1.x, pp1.y - v1.y).length() + new Vector2d(pp3.x - v2.x, pp3.y - v2.y).length();
            double result2 = new Vector2d(pp1.x - v2.x, pp1.y - v2.y).length() + new Vector2d(pp3.x - v1.x, pp3.y - v1.y).length();
            result[1] = result1 < result2 ? result1 : result2;
        } else {
            result[1] = POSITIVE_INFINITY;
            cPList.add(new Vector2d[]{});
        }
        if (MyUtil.cal_point_line(pp2, pp1, pp3, tp) == -1) {
            // 除去警察3以外两个警察处于两侧
            Vector2d[] catchPosition = MyUtil.getCatchPosition(tp, pp3, thiefRobot.getRadius());
            cPList.add(catchPosition);
            Vector2d v1 = catchPosition[1];
            Vector2d v2 = catchPosition[2];
            double result1 = new Vector2d(pp2.x - v1.x, pp2.y - v1.y).length() + new Vector2d(pp1.x - v2.x, pp1.y - v2.y).length();
            double result2 = new Vector2d(pp2.x - v2.x, pp2.y - v2.y).length() + new Vector2d(pp1.x - v1.x, pp1.y - v1.y).length();
            result[2] = result1 < result2 ? result1 : result2;
        } else {
            result[2] = POSITIVE_INFINITY;
            cPList.add(new Vector2d[]{});
        }
        p1.setIsLeader(false);
        p2.setIsLeader(false);
        p3.setIsLeader(false);
        if (result[0] < result[1] && result[0] < result[2]) {
            p1.setIsLeader(true);
            catchPosition = cPList.get(0);
            updateCaughtPoint(1);
        } else if (result[1] < result[0] && result[1] < result[2]) {
            p2.setIsLeader(true);
            catchPosition = cPList.get(1);
            updateCaughtPoint(2);
        } else {
            p3.setIsLeader(true);
            catchPosition = cPList.get(2);
            updateCaughtPoint(3);
        }
    }

    /**
     * 定时更新警察机器人各自的围捕点
     */
    private void updateCaughtPoint(int i) {
        if (i == 1) {
            p1.setTargetPoint(catchPosition[0]);
            Vector2d v1 = catchPosition[1];
            Vector2d v2 = catchPosition[2];
            double d1 = new Vector2d(p2.getPosition().x - v1.x, p2.getPosition().y - v1.y).length();
            double d2 = new Vector2d(p2.getPosition().x - v2.x, p2.getPosition().y - v2.y).length();
            if (d1 < d2) {
                p2.setTargetPoint(v1);
                p3.setTargetPoint(v2);
            } else {
                p2.setTargetPoint(v2);
                p3.setTargetPoint(v1);
            }
        }
    }

    /**
     * 判断警察和盗贼位置的当前状态
     * 0:未形成包围圈 1:形成包围圈 2:已被围捕
     */
    int getThiefState() {
        if (hasSurrounded()) {
            if (hasCaught()) {
                return 1;
            } else return 1;
        } else
            return 0;
    }

    /**
     * 判断是否已经形成包围圈
     */
    private boolean hasSurrounded() {
        double lambda = police_speed / thief_speed;
        double R = 3 * thiefRobot.getRadius();
        Vector2d tp = thiefRobot.getPosition();
        for (PoliceRobot robot : PRs) {
            double dis = MyUtil.getDistance(robot.getPosition(), tp);
            double sita = Math.acos((Math.pow(MyUtil.getDistance(robot.getPosition(), robot.getTargetPoint()), 2) - R * R - dis * dis) / (2 * R * dis));
            if (Math.sin(sita) > 1 / lambda)
                return false;
        }
        return true;
    }

    /**
     * 判断是否已经围捕
     * 三等分位置均有碰撞
     */
    private boolean hasCaught() {
        return thiefRobot.bumpers.getQuadrantHits(0, Math.PI * 2 / 3) >= 1
                && thiefRobot.bumpers.getQuadrantHits(Math.PI * 2 / 3, Math.PI * 4 / 3) >= 1
                && thiefRobot.bumpers.getQuadrantHits(Math.PI * 4 / 3, Math.PI * 2) >= 1;
    }
}
