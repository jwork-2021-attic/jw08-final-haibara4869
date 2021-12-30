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

import java.awt.Color;
//import java.awt.event.KeyEvent;
import java.awt.Point;
//import java.lang.*;
//import java.util.Random;
import java.io.Serializable;

/**
 *
 * @author Aeranythe Echosong
 */
public class Creature implements Runnable, Serializable{

    protected World world;

    public int type = 0;
    int PLAYER_TYPE = 1;
    int ENEMY_TYPE = 2;
    int NPC_TYPE = 3;
    int THING_TYPE = 4;

    private int x;

    public void setX(int x) {
        this.x = x;
    }

    public int x() {
        return x;
    }

    private int y;

    public void setY(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    public int dx = 0,dy = -1;

    public void setDirection_random(){
        this.dx = -1;
        this.dy = 0;
    }
    public void setDirection(int dx,int dy){
    }
    


    private char glyph;

    public char glyph() {
        return this.glyph;
    }

    private String name;

    public String name() {
        return this.name;
    }

    private Color color;

    public Color color() {
        return this.color;
    }

    private int maxHP;

    public int maxHP() {
        return this.maxHP;
    }

    private int hp;

    public int hp() {
        return this.hp;
    }

    public void modifyHP(int amount) {
        this.hp += amount;
        if(this.hp>this.maxHP){
            this.hp = this.maxHP;
        }
        if (this.hp < 1) {
            this.hp = 0;
            world.remove(this);
        }
    }

    private int maxSP;

    public int maxSP() {
        return this.maxSP;
    }

    private int sp;

    public int sp() {
        return this.sp;
    }

    public void modifySP(int amount) {
        this.sp += amount;
        if(this.sp>this.maxSP){
            this.sp = this.maxSP;
        }
        if (this.sp < 1) {
            this.sp = 0;
        }
    }

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    private int defenseValue;

    public int defenseValue() {
        return this.defenseValue;
    }

    public int money;

    public int money(){
        return this.money;
    }

    public int modifyMoney(int amount) {
        if (this.money+amount<0) {
            this.money = 0;
            return this.money;
        }
        else{
            this.money += amount;
            return -1*amount;
        }
    }


    private int visionRadius;

    public int visionRadius() {
        return this.visionRadius;
    }

    public boolean canSee(int wx, int wy) {
        if ((this.x() - wx) * (this.x() - wx) + (this.y() - wy) * (this.y() - wy) > this.visionRadius()
                * this.visionRadius()) {
            return false;
        }
        for (Point p : new Line(this.x(), this.y(), x, y)) {
            if (this.tile(p.x, p.y).isGround() || (p.x == x && p.y == y)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }


    public synchronized void moveBy(int mx, int my) {
        int x = this.x() + mx;
        int y = this.y() + my;
        Creature other = world.creature(x,y);
        Tile tile = world.tile(x,y);
        
        if (other == null) {
            if (tile.isGround()) {
                this.setX(x);
                this.setY(y);
            }
        }
    }

    public void attack(){ 
    }

    public void meet(){
        
    }
    public synchronized void run() {
    }

    public boolean canEnter(int x, int y) {
        return world.tile(x, y).isGround();
    }

    public void notify(String message, Object... params){
    }


    public Creature(World world, char glyph, Color color, int maxHP,int maxSP, int attack, int defense, int visionRadius,int money) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = maxHP;
        this.maxSP = maxSP;
        this.hp = maxHP;
        this.sp = maxSP;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = visionRadius;
        this.money = money;
    }

}
