package world;

import java.util.Random;

/*
 * Copyright (C) 2015 s-zhouj
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
/**
 *
 * @author s-zhouj
 */
public class WorldBuilder {

    private int width;
    private int height;
    private Tile[][] tiles;

    public WorldBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
    }

    public World build() {
        return new World(tiles);
    }

    private WorldBuilder randomizeTiles() {
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                Random rand = new Random();
                
                switch (rand.nextInt(World.TILE_TYPES)) {
                    case 0:
                        tiles[width][height] = Tile.FLOOR;
                        break;
                   case 1:
                        tiles[width][height] = Tile.WALL;
                        break;
                }
            }
        }
        return this;
    }

    private WorldBuilder smooth(int factor) {
        Tile[][] newtemp = new Tile[width][height];
        if (factor > 1) {
            smooth(factor - 1);
        }
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                // Surrounding walls and floor
                int surrwalls = 0;
                int surrfloor = 0;

                // Check the tiles in a 3x3 area around center tile
                for (int dwidth = -1; dwidth < 2; dwidth++) {
                    for (int dheight = -1; dheight < 2; dheight++) {
                        if (width + dwidth < 0 || width + dwidth >= this.width || height + dheight < 0
                                || height + dheight >= this.height) {
                            continue;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.FLOOR) {
                            surrfloor++;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.WALL) {
                            surrwalls++;
                        }
                    }
                }
                Tile replacement;
                if (surrwalls > surrfloor) {
                    replacement = Tile.WALL;
                } else {
                    replacement = Tile.FLOOR;
                }
                newtemp[width][height] = replacement;
            }
        }
        tiles = newtemp;
        return this;
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles().smooth(8).addSpecialTile();

    }

    public WorldBuilder makeOnlineCaves() {
        return randomizeTiles().smooth(6);

    }

    public WorldBuilder addSpecialTile() {

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if(tiles[x][y]==Tile.FLOOR){
                    Random rand = new Random();                    
                    switch (rand.nextInt(50)) {
                        case 0:
                            tiles[x][y] = Tile.FIRE;
                            break;
                        //case 1:
                        //    tiles[x][y] = Tile.WATER;
                        //    break;
                        //case 2:
                        //case 3:
                        //case 4:
                        default:break;
                    }
                    if(x*x==y||y*y==x){
                        tiles[x][y] = Tile.WATER;
                    }
                }    
            }
        }
        //born place
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y] = Tile.FLOOR;
            }    
        }
        //boss fiercow place
        int bx = 0;
        int by = height-10;
        for (int x = bx; x < bx+9; x++) {
            for (int y = by; y < by+9; y++) {
                tiles[x][y] = Tile.FLOOR;
            }    
        }
        tiles[bx][by] = Tile.WALL;
        tiles[bx][by+4] = Tile.WALL;
        tiles[bx][by+8] = Tile.WALL;
        tiles[bx+2][by+4] = Tile.WALL;
        tiles[bx+4][by] = Tile.WALL;
        tiles[bx+4][by+2] = Tile.WALL;
        tiles[bx+4][by+6] = Tile.WALL;
        tiles[bx+4][by+8] = Tile.WALL;
        tiles[bx+6][by+4] = Tile.WALL;
        tiles[bx+8][by] = Tile.WALL;
        tiles[bx+8][by+4] = Tile.WALL;
        tiles[bx+8][by+8] = Tile.WALL;

        tiles[bx+1][by+1] = Tile.FIRE;tiles[bx+1-1][by+1-1] = Tile.FIRE;tiles[bx+1+1][by+1+1] = Tile.FIRE;tiles[bx+1+1][by+1-1] = Tile.FIRE;tiles[bx+1-1][by+1+1] = Tile.FIRE;
        tiles[bx+7][by+1] = Tile.FIRE;tiles[bx+7-1][by+1-1] = Tile.FIRE;tiles[bx+7+1][by+1+1] = Tile.FIRE;tiles[bx+7+1][by+1-1] = Tile.FIRE;tiles[bx+7-1][by+1+1] = Tile.FIRE;
        tiles[bx+1][by+7] = Tile.FIRE;tiles[bx+1-1][by+7-1] = Tile.FIRE;tiles[bx+1+1][by+7+1] = Tile.FIRE;tiles[bx+1+1][by+7-1] = Tile.FIRE;tiles[bx+1-1][by+7+1] = Tile.FIRE;
        tiles[bx+7][by+7] = Tile.FIRE;tiles[bx+7-1][by+7-1] = Tile.FIRE;tiles[bx+7+1][by+7+1] = Tile.FIRE;tiles[bx+7+1][by+7-1] = Tile.FIRE;tiles[bx+7-1][by+7+1] = Tile.FIRE;
        tiles[bx+4][by+4] = Tile.FIRE;tiles[bx+3][by+3] = Tile.FIRE;tiles[bx+5][by+5] = Tile.FIRE;tiles[bx+3][by+5] = Tile.FIRE;tiles[bx+5][by+3] = Tile.FIRE;
                 
        return this;
    }

    /*
    private WorldBuilder testCaves() {

        MazeGenerator mazeGenerator;
        mazeGenerator = new MazeGenerator(Math.min(width, height));
        mazeGenerator.generateMaze();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(mazeGenerator.getMaze()[i][j]==1){
                    tiles[i][j]= Tile.FLOOR;
                }
                else{
                    tiles[i][j]= Tile.WALL;
                }
            }
        }

        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                tiles[width][height] = Tile.FLOOR;
            }
        }
        for (int m = this.width-3;m<this.width;m++){
            for(int n = this.height-3;n<this.height;n++){
                tiles[m][n] = Tile.WALL;
            }
        }
        tiles[this.width-2][this.height-3] = Tile.FLOOR;
        tiles[this.width-2][this.height-2] = Tile.FLOOR;
        
        return this;
    }*/
}
