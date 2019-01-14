# 机器人围捕（Hunting Game）
基于Simbad平台实现机器人围捕

## 环境搭建

导入Simbad的相关java包，确保代码能正常运行

## 代码结构

- MyProg.java 项目的主入口，用于初始化场景，定义调试模式；

- MyEnv.java 环境描述类，用于定义环境，初始化机器人；

- PoliceRobot.java 警察机器人定义类；

- ThiefRobot.java 盗贼机器人定义类；

- MyUtil.java 通用工具类，包含项目中常用函数。

## 调试入口

1、开启/关闭调试模式，用于输出日志【MyProg.java】
```java
MyEnv ev = new MyEnv(boolean b); // b == true 开启，反之关闭
```

2、设置警察机器人和盗贼机器人的速度【MyEnv.java】

```java
private double police_speed = 1;
private double thief_speed = 0.5;
```

3、设置围捕圈和机器人半径之比【MyEnv.java】

```java
static final int N_RADIUS = 5;     // 设置围捕圈和机器人的半径之比
```

4、定义围捕点生成策略【MyUtil.java】

```java
static Vector2d[] getCatchPosition(Vector2d tp, Vector2d lp, double radius) 
```


## 参考文献
> 陈阳舟，王文星，代桂平.基于角度优先的多机器人围捕策略[A].北京工业大学-电子信息与控制工程学院-2012

[《基于角度优先的多机器人围捕策略》文献链接](http://kns.cnki.net/KCMS/detail/detail.aspx?dbcode=CJFQ&amp;dbname=CJFD2012&amp;filename=BJGD201205014&amp;uid=WEEvREdxOWJmbC9oM1NjYkZCbDdrdXdTQ2NzdVZaeCtSb1BNSjgzUWZVcHQ=$R1yZ0H6jyaa0en3RxVUd8df-oHi7XMMDo7mtKT6mSmEvTuk11l2gFA!!&amp;v=MDA4NzdXTTFGckNVUkxPZlp1ZHJGeWpsVUw3T0p5Zk1hckc0SDlQTXFvOUVZSVI4ZVgxTHV4WVM3RGgxVDNxVHI= )