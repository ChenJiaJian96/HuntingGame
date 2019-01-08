import javax.vecmath.Vector2d;

public class MyUtil {
    private static MyUtil mInstance;

    public static MyUtil getInstance() {
        if (mInstance == null)
            mInstance = new MyUtil();
        return mInstance;
    }

    public static int getQuadrant(Vector2d vector)//获取象限
    {
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
        } else
        {
            return 0;
        }
    }

    public static double getAngle(Vector2d v1, Vector2d v2)//获取两个向量间的夹角
    {

        double k = v1.y / v1.x;
        double y = k * v2.x;
        switch (getQuadrant(v1))
        {
            case 1:
            case 4:
            case -1:
                if (v2.y > y)
                {
                    return v1.angle(v2);
                } else if (v2.y < y)
                {
                    return 2 * Math.PI - v1.angle(v2);
                } else
                {
                    if (v1.x * v2.x < 0)
                    {
                        return Math.PI;
                    } else
                    {
                        return 0;
                    }
                }
            case 2:
            case 3:
            case -3:
                if (v2.y > y)
                {
                    return 2 * Math.PI - v1.angle(v2);
                } else if (v2.y < y)
                {
                    return v1.angle(v2);
                } else
                {
                    if (v1.x * v2.x < 0)
                    {
                        return Math.PI;
                    } else
                    {
                        return 0;
                    }
                }
            case -2:
                int i = getQuadrant(v2);
                if (i == -4)
                {
                    return Math.PI;
                } else if (i == -2 || i == -1 || i == 1 || i == 4)
                {
                    return 2 * Math.PI - v1.angle(v2);
                } else
                {
                    return v1.angle(v2);
                }
            case -4:
                int j = getQuadrant(v2);
                if (j == -1)
                {
                    return Math.PI;
                } else if (j == -4 || j == -1 || j == 1 || j == 4)
                {
                    return v1.angle(v2);
                } else
                {
                    return 2 * Math.PI - v1.angle(v2);
                }
            default:
                return -1;
        }

    }

    public static Vector2d transform(Vector2d v, Vector2d point)//计算方向向量
    {
        Vector2d global = new Vector2d(1, 0);
        double alfa = getAngle(global, v);
        double beta = getAngle(point, v);

        double k1 = Math.cos(alfa + beta) / Math.cos(beta);
        double k2 = Math.sin(alfa + beta) / Math.sin(beta);

        double x = point.x * k1;
        double y = point.y * k2;

        return new Vector2d(x, y);

    }
}
