package world;

import java.awt.Color;
//import java.lang.*;
import java.util.Random;
public class Frog extends Creature{

    private int movecount = 1;
    public int spores = 7;


    public Frog(World world, char glyph, Color color, int maxHP,int maxSP, int attack, int defense, int visionRadius,int money){
        super(world, glyph, color, maxHP,maxSP, attack, defense, visionRadius,money);
        this.type = ENEMY_TYPE;
        setDirection_random();
    }
    
    @Override
    public synchronized void run(){
        while(this.hp()>=1){
            try { 
                Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace();
            }
            walk();
        }
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
    public synchronized void moveBy(int mx, int my) {
        int x = this.x() + mx;
        int y = this.y() + my;
        Creature other = world.creature(x,y);
        Tile tile = world.tile(x,y);
        
        if (other == null||other.type==THING_TYPE) {
            if (tile.isGround()) {
                this.setX(x);
                this.setY(y);
            } 
        }
        else {
            while(other!=null && this.hp()>=1){
                try { 
                    Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace();
                }
                if(other.type==PLAYER_TYPE){
                    attack(other);
                }           
                other = world.creature(x,y);
            }
        }
    }

    public synchronized void attack(Creature other){
        int damage = Math.max(0, this.attackValue() - other.defenseValue());
        damage = (int) (Math.random() * damage) + 1;
    
        other.modifyHP(-damage);
            
        this.notify("You attack the '%s' for %d damage.", other.glyph(), damage);
        other.notify("The '%s' attacks you for %d damage.", glyph(), damage);
    }

    public synchronized void walk(){
        if(movecount>0 && movecount<spores){
            moveBy(dx, dy);
            movecount+=1;
        }
        if(movecount==spores){
            movecount=-1;
        }
        if(movecount<0 && movecount>-1*spores){
            moveBy(-1*dx, -1*dy);
            movecount-=1;
        }
        if(movecount==-1*spores){
            movecount=1;
        }
    }
}
