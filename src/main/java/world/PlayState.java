package world;

import java.io.Serializable;

public class PlayState implements Runnable,Serializable{
    private Player player;
    private World world;

    public static int FIRETIME = 5;
    public int firetime = 0;
    public int firehurt = 2;
    //public boolean isfire = false;


    public PlayState(Player player,World world){
        this.player = player;
        this.world = world;
    }
    
    public void run(){
        while(true){
            try { 
                Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace();
            }
            if(world.tile(player.x(), player.y())==Tile.FIRE){
                //this.isfire = true;
                this.firetime = FIRETIME;
            }
            if(firetime>0){
                this.player.notify("You are on fire!");
                this.player.modifyHP(-1*firehurt);
                try { 
                    Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace();
                } 
                firetime-=1;
            }
        }
    }
}
