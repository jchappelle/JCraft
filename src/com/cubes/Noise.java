package com.cubes;

import Properties.GameProperties;
import com.chappelle.jcraft.blocks.Blocks;
import java.util.Random;
import terrain.TerrainGenerator;

public class Noise implements TerrainGenerator{

    /** Generate a noise source based upon the midpoint displacement fractal.
     *
     * @param rand The random number generator
     * @param roughness a roughness parameter
     * @param width the width of the grid
     * @param height the height of the grid
     */
    public Noise(Random rand, float roughness, int width, int height){
        roughness_ = roughness / width;
        grid_ = new float[width][height];
        rand_ = ((rand == null)?new Random():rand);
        initialise();
    }

    public void setTerrainControl(BTControl terrainControl)
    {
        this.terrainControl = terrainControl;
    }

    /** Source of entropy */
    private Random rand_;
    /** Amount of roughness */
    float roughness_;
    /** Plasma fractal grid */
    private float[][] grid_;
    private BTControl terrainControl;

    public void initialise(){
        int xh = (grid_.length - 1);
        int yh = (grid_[0].length - 1);
        //Set the corner points
        grid_[0][0] = (rand_.nextFloat() - 0.5f);
        grid_[0][yh] = (rand_.nextFloat() - 0.5f);
        grid_[xh][0] = (rand_.nextFloat() - 0.5f);
        grid_[xh][yh] = (rand_.nextFloat() - 0.5f);
        //Generate the fractal
        generate(0, 0, xh, yh);
    }

    //Generate the fractal
    private void generate(int xl, int yl, int xh, int yh){
        int xm = ((xl + xh) / 2);
        int ym = ((yl + yh) / 2);
        if((xl == xm) && (yl == ym)){
            return;
        }
        grid_[xm][yl] = (0.5f * (grid_[xl][yl] + grid_[xh][yl]));
        grid_[xm][yh] = (0.5f * (grid_[xl][yh] + grid_[xh][yh]));
        grid_[xl][ym] = (0.5f * (grid_[xl][yl] + grid_[xl][yh]));
        grid_[xh][ym] = (0.5f * (grid_[xh][yl] + grid_[xh][yh]));
        float v = roughen(0.5f * (grid_[xm][yl] + grid_[xm][yh]), xl + yl, yh + xh);
        grid_[xm][ym] = v;
        grid_[xm][yl] = roughen(grid_[xm][yl], xl, xh);
        grid_[xm][yh] = roughen(grid_[xm][yh], xl, xh);
        grid_[xl][ym] = roughen(grid_[xl][ym], yl, yh);
        grid_[xh][ym] = roughen(grid_[xh][ym], yl, yh);
        generate(xl, yl, xm, ym);
        generate(xm, yl, xh, ym);
        generate(xl, ym, xm, yh);
        generate(xm, ym, xh, yh);
    }

    //Add a suitable amount of random displacement to a point
    private float roughen(float v, int l, int h){
        return (float) (v + (roughness_ * (rand_.nextGaussian() * (h - l))));
    }

    /**
     * Dump out as a CSV
     */
    public void printAsCSV(){
        for(int i = 0;i < grid_.length;i++){
            for(int j = 0;j < grid_[0].length;j++){
                System.out.print(grid_[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
    }


    /**
     * Convert to a Boolean array
     * @return the boolean array
     */
    public boolean[][] toBooleans(){
        int w = grid_.length;
        int h = grid_[0].length;
        boolean[][] ret = new boolean[w][h];
        for(int i = 0;i < w;i++) {
            for(int j = 0;j < h;j++) {
                ret[i][j] = grid_[i][j] < 0;
            }
        }
        return ret;
    }

    public float[][] getGrid(){
        return grid_;
    }

    public float getGridValue(int x, int y){
        return grid_[x][y];
    }

    public float getMinimum(){
        float minimum = Float.MAX_VALUE;
        for(int i=0;i<grid_.length;i++){
            float[] row = grid_[i];
            for(int r=0;r<row.length;r++){
                if(row[r] < minimum){
                    minimum = row[r];
                }
            }
        }
        return minimum;
    }

    public float getMaximum(){
        float maximum = Float.MIN_VALUE;
        for(int i=0;i<grid_.length;i++){
            float[] row = grid_[i];
            for(int r=0;r<row.length;r++){
                if(row[r] > maximum){
                    maximum = row[r];
                }
            }
        }
        return maximum;
    }

    public Chunk generateChunk(Vector3Int chunkLocation)
    {
        Chunk chunk = new Chunk();
        chunk.setLocation(chunkLocation);
        float gridMinimum = getMinimum();
        float gridLargestDifference = (getMaximum() - gridMinimum);
        float[][] grid = getGrid();
        for (int x = 0; x < GameProperties.CHUNK_SIZE.x; x++)
        {
            float[] row = grid[x];
            for (int z = 0; z < GameProperties.CHUNK_SIZE.z; z++)
            {
                int blockHeight = (((int) (((((row[z] - gridMinimum) * 100) / gridLargestDifference) / 100) * GameProperties.CHUNK_SIZE.y)) + 1);
                for (int y = 0; y < blockHeight; y++)
                {
                    chunk.setBlock(x, y, z, Blocks.GRASS);
                }
            }
        }
        return chunk;
    }

    @Override
    public void generateTerrain(Vector3Int offset)
    {
        System.out.print("Generating Terrain");
        for (int x = 0; x < GameProperties.CHUNK_VIEW_DISTANCE.x; x++)
        {
            for (int y = 0; y < GameProperties.CHUNK_VIEW_DISTANCE.y; y++)
            {
                for (int z = 0; z < GameProperties.CHUNK_VIEW_DISTANCE.z; z++)
                {
                    Chunk c = generateChunk(new Vector3Int(x, y, z));
                    c.placeInTerrain(terrainControl);
                }
            }
        }
        System.out.println();
        System.out.println("Adding All Chunks");
        terrainControl.addAllScheduledChunks();
    }

    @Override
    public Vector3Int getPlayerStart()
    {
        Vector3Int result = GameProperties.CHUNK_SIZE.mult(GameProperties.CHUNK_VIEW_DISTANCE).divide(2);
        int y = GameProperties.CHUNK_SIZE.y * GameProperties.CHUNK_VIEW_DISTANCE.y;
        while(terrainControl.getBlock(result.x, y, result.z) == null && y > 1)
        {
            y--;
        }
        result.setY(y+2);

        return result;
    }
}
