import simbad.gui.Simbad;
import simbad.sim.EnvironmentDescription;

/**
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