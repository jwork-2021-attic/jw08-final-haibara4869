package world;

import java.awt.Color;
import java.util.Random;

public class FireCow extends Creature {

    public Player player;
    public boolean iswake = false;
    public int range = 4;
    int bx, by;

    public FireCow(World world, char glyph, Color color, int maxHP, int maxSP, int attack, int defense,
            int visionRadius, Player player,int money) {
        super(world, glyph, color, maxHP, maxSP, attack, defense, visionRadius,money);
        this.player = player;
        this.type = ENEMY_TYPE;
        bx = 4;
        by = world.height() - 6;
    }

    public void meet() {
        int x = player.x() - this.x();
        int y = player.y() - this.y();
        if (x * x + y * y <= 30) {
            if (iswake == false) {
                this.player.notify("You've disturbed MR.FireCow!");
            }
            this.iswake = true;
        } else {
            this.iswake = false;
            this.setX(bx);
            this.setY(by);
        }
    }

    @Override
    public synchronized void run() {
        while (this.hp() >= 1) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            meet();
            if (iswake) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Random rand = new Random();

                switch (rand.nextInt(3)) {
                    case 0:
                        rush();
                        break;
                    case 1:
                        shoot();
                        break;
                    case 2:
                        flash();
                        break;
                    default:
                }
            }
        }
    }

    public void shoot() {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x != 0 || y != 0) {
                    Bullet b = new Bullet(world, (char) 249, this.color(), this.range, 0, this.attackValue(), 0, 0,
                            PLAYER_TYPE,0);
                    b.setDirection(x, y);
                    world.addSpecialLocation(b, this.x() + x, this.y() + y);
                    new Thread(b).start();
                }
            }
        }
    }

    public void flash() {
        Random rand = new Random();

        switch (rand.nextInt(5)) {
            case 0:
                this.setX(bx);
                this.setY(by);
                break;
            case 1:
                this.setX(bx - 3);
                this.setY(by - 3);
                break;
            case 2:
                this.setX(bx - 3);
                this.setY(by + 3);
                break;
            case 3:
                this.setX(bx + 3);
                this.setY(by + 3);
                break;
            case 4:
                this.setX(bx + 3);
                this.setY(by - 3);
                break;
            default:
        }
        shoot();
    }

    public void rush() {
        Random rand = new Random();

        switch (rand.nextInt(4)) {
            case 0:
                this.setX(bx - 4);
                this.setY(by);
                world.fireTile(this.x(), this.y());
                moveBy(1, 0);
                break;
            case 1:
                this.setX(bx + 4);
                this.setY(by);
                world.fireTile(this.x(), this.y());
                moveBy(-1, 0);
                break;
            case 2:
                this.setX(bx);
                this.setY(by + 4);
                world.fireTile(this.x(), this.y());
                moveBy(0, -1);
                break;
            case 3:
                this.setX(bx);
                this.setY(by - 4);
                world.fireTile(this.x(), this.y());
                moveBy(0, 1);
                break;
            default:
        }
    }

    @Override
    public synchronized void moveBy(int mx, int my) {
        for (int i = 0; i < 8; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int x = this.x() + mx;
            int y = this.y() + my;
            Creature other = world.creature(x, y);
            // Tile tile = world.tile(x, y);
            this.setX(x);
            this.setY(y);
            world.fireTile(x, y);
            if (other == null || other.type == THING_TYPE) {
                
            } else {
                other.modifyHP(-1 * this.attackValue());
            }
        }

    }
}
