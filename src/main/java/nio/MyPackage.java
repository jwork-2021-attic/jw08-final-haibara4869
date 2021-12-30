package nio;

import java.io.Serializable;
import java.util.List;

import world.*;

//作为server与clinet之间的传输形式
public class MyPackage implements Serializable {
    private World world;
    private Player player;
    private Player player1;
    private Olderman olderman;
    

    MyPackage(World w, Player p,Player p1, Olderman o){
        world = w;
        player = p;
        player1 = p1;
        olderman = o;
    }
    public World world() {
        return this.world;
    }

    public Player player() {
        return this.player;
    }
    public Player player1() {
        return this.player1;
    }
    public Olderman olderman(){
        return this.olderman;
    }

}
