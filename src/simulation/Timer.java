package simulation;

import org.lwjgl.Sys;

public class Timer {
    private static long now, then = 0;
    private static int delta;

    public static int getDelta() {
        now = Sys.getTime() * 1000 / Sys.getTimerResolution();
        delta = (int) (now - then);
        then = now;

        return delta;
    }
}
