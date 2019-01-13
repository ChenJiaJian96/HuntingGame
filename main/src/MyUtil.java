import javax.vecmath.Vector2d;

public class MyUtil {
    private static MyUtil mInstance;
    private static final double REPEL_CONSTANT = 10.0;       // 斥力因子
    private static final double REPEL_RANGE = 2.0;           // 计算斥力的最小距离
    private static final double ATTRACT_CONSTANT = 30.0;     // 引力因子

    public static MyUtil getInstance() {
        if (mInstance == null)
            mInstance = new MyUtil();
        return mInstance;
    }

    /**
     * 获取象限
     */
    private static int getQuadrant(Vector2d vector) {
        double x = vector.x;
        double y = vector.y;
        if (x > 0 && y > 0)// 第一象限
        {
            return 1;
        } else if (x < 0 && y > 0)// 第二象限
        {
            return 2;
        } else if (x < 0 && y < 0)// 第三象限
        {
            return 3;
        } else if (x > 0 && y < 0)// 第四象限
        {
            return 4;
        } else if (x > 0 && y == 0)// x正半轴
        {
            return -1;
        } else if (x == 0 && y > 0)// y正半轴
        {
            return -2;
        } else if (x < 0 && y == 0)// x负半轴
        {
            return -3;
        } else if (x == 0 && y < 0)// y负半轴
        {
            return -4;
        } else {
            return 0;
        }
    }

    /**
     * 获取两个向量间的夹角
     */
    static double getAngle(Vector2d v1, Vector2d v2) {
        double k = v1.y / v1.x;
        double y = k * v2.x;
        switch (getQuadrant(v1)) {
            case 1:
            case 4:
            case -1:
                if (v2.y > y) {
                    return v1.angle(v2);
                } else if (v2.y < y) {
                    return 2 * Math.PI - v1.angle(v2);
                } else {
                    if (v1.x * v2.x < 0) {
                        return Math.PI;
                    } else {
                        return 0;
                    }
                }
            case 2:
            case 3:
            case -3:
                if (v2.y > y) {
                    return 2 * Math.PI - v1.angle(v2);
                } else if (v2.y < y) {
                    return v1.angle(v2);
                } else {
                    if (v1.x * v2.x < 0) {
                        return Math.PI;
                    } else {
                        return 0;
                    }
                }
            case -2:
                int i = getQuadrant(v2);
                if (i == -4) {
                    return Math.PI;
                } else if (i == -2 || i == -1 || i == 1 || i == 4) {
                    return 2 * Math.PI - v1.angle(v2);
                } else {
                    return v1.angle(v2);
                }
            case -4:
                int j = getQuadrant(v2);
                if (j == -1) {
                    return Math.PI;
                } else if (j == -4 || j == -1 || j == 1 || j == 4) {
                    return v1.angle(v2);
                } else {
                    return 2 * Math.PI - v1.angle(v2);
                }
            default:
                return -1;
        }

    }

    /**
     * 计算方向向量
     */
    static Vector2d transform(Vector2d v, Vector2d point) {
        Vector2d global = new Vector2d(1, 0);
        double alfa = getAngle(global, v);
        double beta = getAngle(point, v);

        double k1 = Math.cos(alfa + beta) / Math.cos(beta);
        double k2 = Math.sin(alfa + beta) / Math.sin(beta);

        double x = point.x * k1;
        double y = point.y * k2;

        return new Vector2d(x, y);

    }

    /**
     * 计算斥力
     */
    static double repelForce(double distance) {
        double force = 0;
        if (distance <= REPEL_RANGE)
            force = REPEL_CONSTANT / (distance * distance);
        return force;
    }

    /**
     * 计算引力
     */
    static double attractForce(double distance) {
        return ATTRACT_CONSTANT * distance;
    }

    /**
     * 判断p点/q点在P1P2形成直线的位置关系
     *
     * @param p  观测点
     * @param p1 成线点1
     * @param p2 成线点2
     * @return 0 至少一个点在直线上 1同侧 -1异侧
     * <p>
     * (Ax1+By1+C)(Ax2+By2+C) > 0 同侧
     * (Ax1+By1+C)(Ax2+By2+C) < 0 异侧
     */
    static int cal_point_line(Vector2d p, Vector2d q, Vector2d p1, Vector2d p2) {
        // 由p1.p2得出 Ax + By + C = 0
        double result = 0;
        double x1 = p1.x;
        double x2 = p2.x;
        double y1 = p1.y;
        double y2 = p2.y;
        if (x1 == x2) {
            result = (p.x - x1) * (q.x - x1);
        } else {
            double A = (y1 - y2) / (x1 - x2);
            double B = -1;
            double C = y2 - (y1 - y2) / (x1 - x2) * x2;
            result = (A * p.x + B * p.y + C) * (A * q.x + B * q.y + C);
        }
        if (result == 0)
            return 0;
        else if (result > 0)
            return 1;
        else return -1;
    }

    /**
     * 根据领航机器人位置生成围捕点
     * 围捕点设置在自身半径3倍的圆上，互成120度
     *
     * @param tp     thiefPosition 盗贼机器人位置
     * @param lp     leaderPosition 领航机器人位置
     * @param radius 机器人的自身半径
     * @return [领航机器人围捕点, 围捕点2, 围捕点3]
     */
    static Vector2d[] getCatchPosition(Vector2d tp, Vector2d lp, double radius) {
        double R = radius * MyEnv.N_RADIUS;
        double k = R / lp.length();
        Vector2d point1 = new Vector2d(k * lp.x, k * lp.y);   // 领航机器人的围捕点
        Vector2d point2 = new Vector2d();
        Vector2d point3 = new Vector2d();
        if (point1.x == 0) {
            point2.setX(R * Math.cos(Math.PI / 6));
            point2.setY(R / 2 * point1.y > 0 ? -1 : 1);
            point3.setX(-R * Math.cos(Math.PI / 6));
            point3.setY(R / 2 * point1.y > 0 ? -1 : 1);
        } else {
            double sita = Math.acos(point1.y / point1.x);
            //TODO:分类讨论
            point2.set(-R * Math.cos(Math.PI / 3 - sita), R * Math.sin(Math.PI / 3 - sita));
            point3.set(-R * Math.sin(Math.PI / 6 - sita), -R * Math.cos(Math.PI / 6 - sita));
        }
        point1.set(point1.x + tp.x, point1.y + tp.y);
        point2.set(point2.x + tp.x, point2.y + tp.y);
        point3.set(point3.x + tp.x, point3.y + tp.y);
        return new Vector2d[]{point1, point2, point3};
    }

    /**
     * 获取两坐标之间的距离
     */
    static double getDistance(Vector2d v1, Vector2d v2) {
        return new Vector2d(v1.x - v2.x, v1.y - v2.y).length();
    }
}
