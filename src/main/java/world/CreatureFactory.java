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
package world;

import java.io.Serializable;
import java.util.List;
//import java.util.Scanner;

import asciiPanel.AsciiPanel;
import screen.*;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory implements Serializable{

    private World world;
    private Player player;

    public CreatureFactory(World world) {
        this.world = world;
        player = null;
    }
    //player
    public void setPlayer0(Player player){
        this.player = player;
    }
    public Player newPlayer(List<String> messages,PlayScreen s) {
        Player player = new Player(this.world, (char)1, AsciiPanel.brightWhite, 100,100, 25, 5, 9,0);
        world.addSpecialLocation(player,1,1);
        player.setMessages(messages);
        this.player = player;
        return player;
    }
    //enemy & boss
    public Frog newFrog() {
        Frog frog = new Frog(this.world, (char)3, AsciiPanel.brightRed, 20,0, 20, 0, 0,10);
        //world.addSpecialLocation(frog,3,3);
        world.addAtEmptyLocation(frog);
        new Thread(frog).start();
        return frog;
    }
    public Bat newBat() {
        Bat bat = new Bat(this.world, (char)2, AsciiPanel.brightBlue, 10,0, 20, 0, 0,10);
        //world.addSpecialLocation(bat,4,4);
        world.addAtEmptyLocation(bat);
        new Thread(bat).start();
        return bat;
    }
    public FireCow newFireCow() {
        FireCow firecow = new FireCow(this.world, (char)4, AsciiPanel.darkpink, 50, 0, 30, 0, 0,this.player,50);
        world.addSpecialLocation(firecow,4,world.height()-6);
        new Thread(firecow).start();
        return firecow;
    }
    //npc
    public Olderman newOlderman() {
        Olderman olderman = new Olderman(this.world, (char)5, AsciiPanel.pink, 150 , 0, 0, 0, 0,0);
        world.addSpecialLocation(olderman,world.width()-2,world.height()-2);
        new Thread(olderman).start();
        return olderman;
    }
}
