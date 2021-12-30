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

package applicationmain;

//import java.lang.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import screen.Screen;
import screen.StartScreen;


/**
 *
 * @author Aeranythe Echosong
 */
public class ApplicationMain extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;

    public ApplicationMain() {
        super();
        //terminal = new AsciiPanel(80, 30, AsciiFont.TALRYTH_15_15);
        terminal = new AsciiPanel(80, 30, AsciiFont.MY_15x15);
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        new Thread(new Runnable(){
            @Override
            public void run(){
                long update = 0;
                long sleep = 0;
                while(true){
                    long before = System.nanoTime();
                    //long t=  sleep+update;
                    repaint();  
                    update = (System.nanoTime()-before)/1000000L;
                    sleep=Math.max(2,16-update);
                    try { 
                        Thread.sleep(sleep); } catch (InterruptedException e) { e.printStackTrace();
                    } 
                }
            }
        }
        ).start();
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    /**
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        //repaint();
    }

    /**
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        screen = screen.respondToUserInput_released(e);
    }

    /**
     *
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        ApplicationMain app = new ApplicationMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
