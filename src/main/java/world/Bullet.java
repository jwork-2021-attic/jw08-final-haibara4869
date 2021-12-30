package world;

import java.awt.Color;
//import java.lang.*;
public class Bullet extends Creature{

    //public int dx,dy;
    public int aim_type ;

    @Override
    public void setDirection(int dx,int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public  Bullet(World world, char glyph, Color color, int maxHP,int maxSP, int attack, int defense, int visionRadius,int aim_typeint ,int money){
        super(world, glyph, color, maxHP,maxSP, attack, defense, visionRadius,money);
        this.type = THING_TYPE;
        this.aim_type = aim_type;
    }
    
    @Override
    public synchronized void run(){
        while(this.hp()>=1){
            try { 
                Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace();
            }
            fly();
        }
    }  

    @Override
    public synchronized void moveBy(int mx, int my) {

        Creature other = world.creature(this.x(),this.y());
        if(other!=null){
            if (other.type == aim_type) {
                hurt(other);
             }
        } 
        
        int x = this.x() + mx;
        int y = this.y() + my;
        
        Tile tile = world.tile(x,y);
        
        if (tile.isGround()) {
            this.setX(x);
            this.setY(y);
        }
        else{
            this.modifyHP(-this.maxHP());
        } 
          
    }

    public synchronized void hurt(Creature other){
        int damage = Math.max(0, this.attackValue() - other.defenseValue());
        damage = (int) (Math.random() * damage) + 1;
    
        other.modifyHP(-damage);
            
        other.notify("The '%s' is shot by %d damage.", other.glyph(), damage);
        //other.notify("You're shot for %d damage." , damage);
        this.modifyHP(-this.maxHP());
    }

    public synchronized void fly(){
        moveBy(dx,dy);
        this.modifyHP(-1);
    }
}
