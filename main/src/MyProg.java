import simbad.gui.Simbad;
import simbad.sim.EnvironmentDescription;

/**
 * Created by jiajianchen
 * on 2019/1/7
 */

public class MyProg {

    public static void main(String[] args) {
        EnvironmentDescription ev = new MyEnv();
        Simbad frame = new Simbad(ev ,false);
    }
}