import simbad.gui.Simbad;

/**
 * 三警察机器人围捕盗贼机器人
 * Created by jiajianchen
 * on 2019/1/7
 */

public class MyProg {

    public static void main(String[] args) {
        MyEnv ev = new MyEnv(true);
        ev.setSpeed(1,1);
        Simbad frame = new Simbad(ev ,false);
    }
}