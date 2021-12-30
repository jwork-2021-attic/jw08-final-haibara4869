package world;

import java.awt.Color;
//import java.lang.*;
import screen.*;
public class Olderman extends Creature{


    public Olderman(World world, char glyph, Color color, int maxHP,int maxSP, int attack, int defense, int visionRadius,int money){
        super(world, glyph, color, maxHP,maxSP, attack, defense, visionRadius,money);
        this.type = NPC_TYPE;
    }
    
    
    public Creature find_aim(){
        if(world.creature(this.x()-1, this.y())!=null){
            return world.creature(this.x()-1, this.y());
        }
        if(world.creature(this.x()+1, this.y())!=null){
            return world.creature(this.x()+1, this.y());
        }
        if(world.creature(this.x(), this.y()-1)!=null){
            return world.creature(this.x(), this.y()-1);
        }
        if(world.creature(this.x(), this.y()+1)!=null){
            return world.creature(this.x(), this.y()+1);
        }
        return null;
    }

    public synchronized void meet(){
        Creature other = find_aim();
        if(other!=null){
            if(other.type == PLAYER_TYPE){
                this.modifyHP(-1*this.maxHP());
            }
        }  
    }

    @Override
    public synchronized void run(){
        while(this.hp()>=1){
            try { 
                Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace();
            }
            meet();
        }
    }
}
