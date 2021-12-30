package screen;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

import world.Creature;

public class LoadScreen extends SaveLoadScreen {

    public LoadScreen(Screen preScreen) {
        super(preScreen);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                return this.preScreen;
            case KeyEvent.VK_UP:
                curSelected = (curSelected - 1 + 3) % 3;
                break;
            case KeyEvent.VK_DOWN:
                curSelected = (curSelected + 1) % 3;
                break;
            case KeyEvent.VK_ENTER:
                if (save[curSelected].exists()) {
                    try {
                        FileInputStream f = new FileInputStream(save[curSelected]);
                        ObjectInputStream o = new ObjectInputStream(f);
                        PlayScreen ps = (PlayScreen) o.readObject();
                        o.close();
                        f.close();
                        for (Creature c : ps.getworld().getCreatures())
                            new Thread(c).start();
                        new Thread(ps).start();
                        return ps;
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                break;
            default:
                return this;
        }
        return this;
    }

    @Override
    public Screen respondToUserInput_released(KeyEvent key) {
        return this;
    }
}