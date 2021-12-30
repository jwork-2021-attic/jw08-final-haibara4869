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

import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen, Serializable, Runnable {

    private World world;
    private Player player;
    private Olderman olderman;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    //private List<String> oldMessages;
    private int gameState;
    public boolean showTable;

    public PlayScreen() {
        this.gameState = 0;
        this.showTable = false;
        this.screenWidth = 80;
        this.screenHeight = 23;
        createWorld();
        this.messages = new ArrayList<String>();

        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
    }

    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }, 0, 1000);
    }

    public void setGamestate(int x){
        this.gameState =x;
    }


    private void createCreatures(CreatureFactory creatureFactory) {
        //player
        this.player = creatureFactory.newPlayer(this.messages,this);
        //npc
        this.olderman = creatureFactory.newOlderman();
        //enemy
        for(int i = 0;i<25;i++){
            creatureFactory.newFrog();
            creatureFactory.newBat();
        }
        creatureFactory.newFireCow();       
    }

    private void createWorld() {
        world = new WorldBuilder(100,50).makeCaves().build();
    }

    public World getworld() {
        return this.world;
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                }
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                if (player.canSee(creature.x(), creature.y())) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                }
            }
        }
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        //for (int i = 0; i < messages.size(); i++) {
        //    terminal.write(messages.get(i), 1, top + i + 1);
        //}
        if(messages.size()!=0){
            terminal.write(messages.get(messages.size()-1), 1, top + messages.size());
        }
        //messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        //gamestate
        if(player.hp()<1){
            setGamestate(1);
        }
        if(olderman.hp()<1){
            setGamestate(2);
        }
        
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // Stats
        if(this.showTable == true){
            showTable(terminal);
        }
        
        // Messages
        displayMessages(terminal, this.messages);
                
    }

    public void showTable(AsciiPanel terminal){
        int line = 0;
        String boundline = String.format(".-.-.-.-.-.-.-.-.-.-");
        terminal.write(boundline, 0,line);
        line+=1;
        String hp = String.format("#%3d/%3d hp         ", player.hp(), player.maxHP());
        terminal.write(hp,  0,line,AsciiPanel.red,null);
        line+=1;
        String sp = String.format("#%3d/%3d sp         ", player.sp(), player.maxSP());
        terminal.write(sp,  0,line,AsciiPanel.blue,null);
        line+=1;
        String a = String.format( "#%3d attack_point   ", player.attackValue());
        terminal.write(a,0,line);
        line+=1;
        String d = String.format( "#%3d defence_point  ", player.defenseValue());
        terminal.write(d,0,line);
        line+=1;
        terminal.write(boundline,  0,line);
        line+=1;
        terminal.write("Press down arrow key to move.",  0,line);
        line+=1;
        terminal.write("Press down A to attack.",  0,line);
        line+=1;
        terminal.write("Press down Q to shoot.",  0,line);
        line+=1;
        terminal.write("Press down W to get treated.",  0,line);
        line+=1;
        terminal.write("Press down D to dig wall.",  0,line);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (this.gameState) {
            case 1:
                return new LoseScreen();
            case 2:
                return new WinScreen();
        }
        if(key.getKeyCode() == KeyEvent.VK_S){
            this.showTable = true;
        }
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
            return new SaveScreen(this);
        }
        player.respondToUserInput(key);
        return this;
    }

    @Override
    public Screen respondToUserInput_released(KeyEvent key){
        if(key.getKeyCode() == KeyEvent.VK_S){
            this.showTable = false;
        }    
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }

}
