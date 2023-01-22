import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.Thread.sleep;

public class KeyPresser extends Thread {
    int ms_delay;
    public boolean done = false;
    public KeyPresser(int ms_delay){
        this.ms_delay = ms_delay;
    }

    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_W);

            sleep(ms_delay);
            robot.keyRelease(KeyEvent.VK_W);

        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
done = true;
    }
}
