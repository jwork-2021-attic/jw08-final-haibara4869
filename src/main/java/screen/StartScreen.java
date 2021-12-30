/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package screen;

import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;

/**
 *
 * @author Aeranythe Echosong
 */
public class StartScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("This is the start screen.", 0, 0);
        terminal.write("Press ENTER to take your risk", 0, 1);
        terminal.write("~.New Game.~", 4, 5);
        terminal.write("~.Archives.~", 4, 7);
        terminal.write("~. OnLine .~", 4, 9);
        terminal.write((char) 26, 3, 5 + curSelected * 2);
    }

    protected int curSelected = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                curSelected = (curSelected - 1 + 3) % 3;
                break;
            case KeyEvent.VK_DOWN:
                curSelected = (curSelected + 1) % 3;
                break;
            case KeyEvent.VK_ENTER:
                switch (this.curSelected) {
                    case 0:
                        PlayScreen ps = new PlayScreen();
                        new Thread(ps).start();
                        return ps;
                    case 1:
                        return new LoadScreen(this);
                    case 2:
                        ClientScreen cs = new ClientScreen("0.0.0.0",1234);
                        new Thread(cs).start();
                        return cs;
                }
                break;
            default:
                return this;
        }
        return this;
    }
}
