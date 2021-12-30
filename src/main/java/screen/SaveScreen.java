package screen;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Currency;

public class SaveScreen extends SaveLoadScreen {

    public SaveScreen(Screen preScreen) {
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
                try {
                    if (!save[curSelected].exists()) {
                        // 先得到文件的上级目录，并创建上级目录，在创建文件
                        save[curSelected].getParentFile().mkdir();
                        try {
                            // 创建文件
                            save[curSelected].createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileOutputStream f = new FileOutputStream(save[curSelected]);
                    ObjectOutputStream o = new ObjectOutputStream(f);
                    o.writeObject(preScreen);
                    o.close();
                    f.close();
                } catch (Exception e) {
                    System.out.println(e);
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