package world;

import java.awt.Color;
//import java.lang.*;
import java.util.Random;
public class Bat extends Creature{

    public int range = 10;


    public Bat(World world, char glyph, Color color, int maxHP,int maxSP, int attack, int defense, int visionRadius,int money){
        super(world, glyph, color, maxHP,maxSP, attack, defense, visionRadius,money);
        this.type = ENEMY_TYPE;
        setDirection_random();
    }
    

    @Override
    public void setDirection_random(){
        Random rand = new Random();
            switch (rand.nextInt(4)) {
                case 0:
                    this.dx=-1;this.dy=0;
                    break;
                case 1:
                    this.dx=1;this.dy=0;
                    break;
                case 2:
                    this.dx=0;this.dy=-1;
                    break;
                case 3:
                    this.dx=0;this.dy=1;          
                    break;
                }
    }

    @Override
    public synchronized void run(){
        while(this.hp()>=1){
            try { 
                Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();
            }
            shoot();
        }
    }  

    public synchronized void shoot(){
        Bullet b = new Bullet(world, (char)249, this.color(), this.range, 0, this.attackValue(), 0, 0,PLAYER_TYPE,0);
        b.setDirection(this.dx,this.dy);
        world.addSpecialLocation(b,this.x()+dx,this.y()+dy);
        new Thread(b).start();
    }
}
