package world;

import java.util.List;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

//import java.lang.*;

public class Player extends Creature {

    private List<String> messages;

    public Player(World world, char glyph, Color color, int maxHP, int maxSP, int attack, int defense, int visionRadius,
            int money) {
        super(world, glyph, color, maxHP, maxSP, attack, defense, visionRadius, money);
        this.type = PLAYER_TYPE;
        new Thread(new PlayState(this, this.world)).start();
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    // public int dx = 0,dy = -1;

    @Override
    public synchronized void moveBy(int mx, int my) {
        int x = this.x() + mx;
        int y = this.y() + my;
        Creature other = world.creature(x, y);
        Tile tile = world.tile(x, y);

        if (other == null) {
            if (tile.isGround()) {
                this.setX(x);
                this.setY(y);
            }
        }
        this.messages.clear();
    }

    public Creature find_aim() {
        if (world.creature(this.x() - 1, this.y()) != null) {
            return world.creature(this.x() - 1, this.y());
        }
        if (world.creature(this.x() + 1, this.y()) != null) {
            return world.creature(this.x() + 1, this.y());
        }
        if (world.creature(this.x(), this.y() - 1) != null) {
            return world.creature(this.x(), this.y() - 1);
        }
        if (world.creature(this.x(), this.y() + 1) != null) {
            return world.creature(this.x(), this.y() + 1);
        }
        return null;
    }

    @Override
    public synchronized void attack() {
        Creature other = find_aim();
        // for (Creature creature : world.getCreatures()){
        // if ((x-creature.x()) * (x-creature.x()) + (y-creature.y()) * (y-creature.y())
        // == 1){
        // other = creature;
        // break;
        // }
        // }
        if (other != null && other.type != PLAYER_TYPE) {
            steal(other);
            int damage = Math.max(0, this.attackValue() - other.defenseValue());
            damage = (int) (Math.random() * damage) + 1;

            other.modifyHP(-damage);

            this.notify("You attack the '%s' for %d damage.", other.glyph(), damage);
            other.notify("The '%s' attacks you for %d damage.", glyph(), damage);
        }
    }

    public void steal(Creature other) {
        Random rand = new Random();
        switch (rand.nextInt(2)) {
            case 0:
                break;
            case 1:
                int money = other.money();
                other.modifyMoney(-1*money);
                this.modifyMoney(money);
                this.notify("You steal the '%s' for %d money.", other.glyph(), money);
                other.notify("The '%s' attacks you for %d money.", glyph(), money);
                break;
        }
    }

    public int range = 10;
    public int cost_q = 1;

    public synchronized void shoot() {
        if (this.sp() < cost_q) {
            this.notify("You are running out of sp:)");
        } else {
            this.modifySP(-1 * cost_q);
            this.notify("You fire one bullet and cost %d sp.", cost_q);
            Bullet b = new Bullet(world, (char) 249, this.color(), this.range, 0, this.attackValue(), 0, 0, ENEMY_TYPE,
                    0);
            b.setDirection(this.dx, this.dy);
            world.addSpecialLocation(b, this.x() + dx, this.y() + dy);
            new Thread(b).start();
        }
    }

    public int cost_w = 20;
    public int treat_value = 20;

    public synchronized void treated() {
        if (this.sp() < cost_w) {
            this.notify("You are running out of sp:)");
        } else {
            this.modifySP(-1 * cost_w);
            this.notify("You cost %d sp and regain %d hp.", cost_w, treat_value);
            this.modifyHP(treat_value);
        }
    }

    public synchronized void dig() {
        if (world.tile(this.x() + this.dx, this.y() + this.dy).isDiggable()) {
            this.notify("You dig the wall.");
            world.clearTile(this.x() + this.dx, this.y() + this.dy);
        }

    }

    @Override
    public void notify(String message, Object... params) {
        this.messages.add(String.format(message, params));
    }

    public void respondToUserInput(KeyEvent key) {

        if (this.hp() < this.maxHP() * 0.2) {
            this.notify("You ard going to DIE! Please use W to get treated.");
        }
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                this.dx = -1;
                this.dy = 0;
                moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                this.dx = 1;
                this.dy = 0;
                moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                this.dx = 0;
                this.dy = -1;
                moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                this.dx = 0;
                this.dy = 1;
                moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                attack();
                break;
            case KeyEvent.VK_Q:
                shoot();
                break;
            case KeyEvent.VK_D:
                dig();
                break;
            case KeyEvent.VK_W:
                treated();
                break;
        }
    }

    public void respondToUserInput(int KeyCode) {

        if (this.hp() < this.maxHP() * 0.2) {
            this.notify("You ard going to DIE! Please use W to get treated.");
        }
        switch (KeyCode) {
            case KeyEvent.VK_LEFT:
                this.dx = -1;
                this.dy = 0;
                moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                this.dx = 1;
                this.dy = 0;
                moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                this.dx = 0;
                this.dy = -1;
                moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                this.dx = 0;
                this.dy = 1;
                moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                attack();
                break;
            case KeyEvent.VK_Q:
                shoot();
                break;
            case KeyEvent.VK_D:
                dig();
                break;
            case KeyEvent.VK_W:
                treated();
                break;
        }
    }
}
