package screen;

import java.awt.event.KeyEvent;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.*;
import java.io.*;

import java.awt.Color;

import nio.*;
import asciiPanel.AsciiPanel;
import world.*;

public class ClientScreen implements Screen, Runnable {

    private SocketChannel client;
    private ByteBuffer buffer;
    private int restLen;

    private World world;
    private Player player;
    private Player player1;
    private Olderman olderman;
    private List<String> messages;

    private int screenWidth;
    private int screenHeight;

    private int gameState = 0;
    public void setGamestate(int x){
        this.gameState =x;
    }

    public ClientScreen(String host, int port) {
        this.screenWidth = 25;
        this.screenHeight = 25;
        this.messages = new ArrayList<String>();
        try {
            this.client = SocketChannel.open(new InetSocketAddress(host, port));
            buffer = ByteBuffer.allocate(8192);
            restLen = 0;

            while (true) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                buffer.clear();
                client.read(buffer);
                buffer.flip();
                if (restLen == 0) {
                    restLen = buffer.getInt();
                    //System.out.println("restlen:"+restLen);
                }
                while (restLen > 0 && buffer.remaining() > 0) {
                    baos.write(buffer.get());
                    --restLen;
                    //System.out.println("restlen:"+restLen+"; "+baos.size());
                }
                if (restLen == 0 && baos.size() > 0) {
                    //System.out.println("??????restlen:"+restLen+"; "+baos.size());
                    ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
                    //System.out.println("GET package?");
                    MyPackage pkg = (MyPackage) is.readObject();
                    //System.out.println("GET package");
                    this.world = pkg.world();
                    this.player = pkg.player();
                    this.player1 = pkg.player1();
                    this.olderman = pkg.olderman();
                    baos.reset();
                    break;
                }
            }
        } catch (Exception e) {e.printStackTrace();
        }

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while (true) {
                buffer.clear();
                client.read(buffer);
                // System.out.println("READ, from " + client.socket());
                buffer.flip();
                if (restLen == 0) {
                    restLen = buffer.getInt();
                    //System.out.println("restlen:?"+restLen);
                }
                while (restLen > 0 && buffer.remaining() > 0) {
                    baos.write(buffer.get());
                    --restLen;
                    //System.out.println("restlen:??"+restLen+";"+baos.size());
                }
                if (restLen == 0 && baos.size() > 0) {
                    ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
                    MyPackage pkg = (MyPackage) is.readObject();
                    //System.out.println("GET package");
                    this.world = pkg.world();
                    //System.out.println("creatur:" + world.getCreatures().size());
                    this.player = pkg.player();
                    this.player1 = pkg.player1();
                    this.olderman = pkg.olderman();
                    if (world == null | player == null) {
                        System.out.println("WEIRD!!");
                    }
                    baos.reset();
                }
            }
        } catch (Exception e) {e.printStackTrace();
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        //System.out.println("display?");
        if (world == null | player == null| player1 == null) {
            return;
        }
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
        // Messages
        displayMessages(terminal, this.messages);
                
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
                    terminal.write((char)255, x, y, Color.DARK_GRAY);
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
        int line = 0;
        terminal.write(player.glyph(), world.width(), line, player.color());
        line+=1;
        String hp = String.format((char)0+"%3d/%3d hp         ", player.hp(), player.maxHP());
        terminal.write(hp, world.width(),line,AsciiPanel.red,null);
        line+=1;
        String sp = String.format((char)0+"%3d/%3d sp         ", player.sp(), player.maxSP());
        terminal.write(sp,  world.width(),line,AsciiPanel.blue,null);
        line+=1;
        String a = String.format( (char)0+"%3d attack_point   ", player.attackValue());
        terminal.write(a,world.width(),line);
        line+=1;
        String d = String.format( (char)0+"%3d defence_point  ", player.defenseValue());
        terminal.write(d,world.width(),line);
        line+=1;
        String m = String.format( (char)0+"%3d "+(char)161, player.money());
        terminal.write(m,world.width(),line);
        line+=1;
        terminal.write(player1.glyph(), world.width(), line, player1.color());
        line+=1;
        String hp1 = String.format((char)0+"%3d/%3d hp         ", player1.hp(), player1.maxHP());
        terminal.write(hp1, world.width(),line,AsciiPanel.red,null);
        line+=1;
        String sp1 = String.format((char)0+"%3d/%3d sp         ", player1.sp(), player1.maxSP());
        terminal.write(sp1,  world.width(),line,AsciiPanel.blue,null);
        line+=1;
        String a1 = String.format( (char)0+"%3d attack_point   ", player1.attackValue());
        terminal.write(a1,world.width(),line);
        line+=1;
        String d1 = String.format( (char)0+"%3d defence_point  ", player1.defenseValue());
        terminal.write(d1,world.width(),line);
        line+=1;
        String m1 = String.format( (char)0+"%3d "+(char)161, player1.money());
        terminal.write(m1,world.width(),line);
        
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (this.gameState) {
            case 1:
                return new LoseScreen();
            case 2:
                return new WinScreen();
        }
        try {
            //System.out.println("GET input"+key.getKeyCode());
            client.write(ByteBuffer.wrap(int2byteArrary(key.getKeyCode())));
        } catch (Exception e) {e.printStackTrace();
        }
        return this;
    }
    @Override
    public Screen respondToUserInput_released(KeyEvent key) {
        // TODO Auto-generated method stub
        return this;
    }

    private int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    private int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }


    public static byte[] int2byteArrary(int value) {//大端存储
        byte[] res = new byte[4];
        res[0] = (byte) (value >> 24);
        res[1] = (byte) (value >> 16);
        res[2] = (byte) (value >> 8);
        res[3] = (byte) (value);
        return res;
    }

    
}