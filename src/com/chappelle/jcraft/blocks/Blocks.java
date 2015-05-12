package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import static com.chappelle.jcraft.blocks.Blocks.WOOL;
import com.cubes.BlockSkin;
import com.jme3.app.SimpleApplication;
import java.util.LinkedList;
import java.util.List;

public class Blocks
{
    public static JBlock TEST;
    public static JBlock GRASS;
    public static JBlock STONE;
    //public static JBlock OAK_PLANKS;
    public static JBlock BRICK;
    public static JBlock GRAVEL;
    public static JBlock LOG;
    public static JBlock BEDROCK;
    //public static JBlock COBBLE;
    public static JBlock DIRT;
    public static JBlock GLASS;
    public static JBlock SLABSTONE;
    public static JBlock SMOOTHSTONE;
    public static JBlock COAL_BLOCK;
    public static JBlock IRON_ORE;
    public static JBlock GOLD_ORE;
    public static JBlock DIAMOND_ORE;
    public static JBlock REDSTONE_ORE;
    public static JBlock SAND;
    public static JBlock BIRCHLOG;
    public static JBlock BOOKCASE;
    public static JBlock CACTUS;
    public static JBlock DARKCOBBLE;
    public static JBlock ICE;
    public static JBlock MOSSYCOBBLE;
    public static JBlock OBSIDIAN;
    public static JBlock SNOW;
    public static JBlock SNOWBLOCK;
    public static JBlock SPAWNER;
    public static JBlock SPONGE;
    public static JBlock WOOL;
    public static JBlock PUMPKIN;
    public static JBlock FURNACE;
    public static JBlock CRAFTING_TABLE;
    public static JBlock TNT;
    public static JBlock BLUE;
    public static JBlock CHEST;
    public static JBlock STONEBRICK;
    public static JBlock FURNACELIT;
    public static JBlock GOLDBLOCK;
    public static JBlock IRONBLOCK;
    public static JBlock JACKOLANTERN;
    public static JBlock LAVA;
    public static JBlock NETERAK;
    public static JBlock NOTEBLOCK;
    public static JBlock POLISHEDSTONE;
    public static JBlock SOUL_SAND;
    public static JBlock WATER;
    public static JBlock LEAVES;
    public static JBlock OAK_LEAVES;
    //public static JBlock NETHERBRICK;

    public static JBlock LAPIS_BLOCK;
    public static JBlock LAPIS_ORE;
    //public static JBlock ENDSTONE;
    public static JBlock END_COBBLE1;
    public static JBlock END_COBBLE2;

    public static JBlock WOOL_BLACK;
    public static JBlock WOOL_RED;
    public static JBlock WOOL_BROWN;
    public static JBlock WOOL_DARK_RED;
    public static JBlock WOOL_BLUE;
    public static JBlock WOOL_PURPLE;
    public static JBlock WOOL_AQUA;
    public static JBlock WOOL_LIGHT_GRAY;
    public static JBlock WOOL_DARK_GRAY;
    public static JBlock WOOL_PINK;
    public static JBlock WOOL_GREEN;
    public static JBlock WOOL_YELLOW;
    public static JBlock WOOL_LIGHT_BLUE;
    public static JBlock WOOL_LIGHT_PURPLE;
    public static JBlock WOOL_ORANGE;
    public static JBlock REPEATER_OFF;
    public static JBlock REPEATHER_ON;
    public static JBlock REDSTONE_LAMP_OFF;

    public static JBlock TRAPDOOR;
    public static JBlock MOSSY_STONE_BRICK;
    public static JBlock SPRUCE_LOG;
    public static JBlock REDSTONE_LAMP_ON;
    public static JBlock WOODEN_DOOR_TOP;
    public static JBlock WOODEN_DOOR_BOTTOM;

    public static JBlock IRON_BARS;
    public static JBlock CRACKED_STONE_BRICK;
    public static JBlock DRAGON_SCALE;
    public static JBlock CHISELED_STONE;
    public static JBlock CHISELED_STANDSTONE;

    //public static JBlock SPRUCE_PLANKS;
    //public static JBlock BIRCH_PLANKS;
    public static JBlock POLISHED_SANDSTONE;
    public static JBlock TORCH;

    private static List<JBlock> blocks = new LinkedList<JBlock>();

    public static void registerBlocks(SimpleApplication app)
    {
        TEST = new Test();
        blocks.add(TEST);

        addStoneVariants(BlockSkinProperties.COBBLE);
        addStoneVariants(BlockSkinProperties.NETHER_BRICK);
        addStoneVariants(BlockSkinProperties.ENDSTONE);
        addStoneVariants(BlockSkinProperties.STONE);
        addStoneVariants(BlockSkinProperties.STONE_BRICK);
        addStoneVariants(BlockSkinProperties.SLABSTONE);
        addStoneVariants(BlockSkinProperties.SMOOTSTONE);
        addWoodVariants(BlockSkinProperties.OAK_PLANKS);
        addWoodVariants(BlockSkinProperties.BIRCH_PLANKS);
        addWoodVariants(BlockSkinProperties.SPRUCE_PLANKS);
        addStoneVariants(BlockSkinProperties.DARK_COBBLE);
        addWoolVariants(BlockSkinProperties.WOOL_BLUE);
        addWoolVariants(BlockSkinProperties.WOOL);
        addStoneVariants(BlockSkinProperties.MOSSY_COBBLE);
        addStoneVariants(BlockSkinProperties.OBSIDIAN);
        addStoneVariants(BlockSkinProperties.POLISHED_STONE);
        addWoolVariants(BlockSkinProperties.WOOL_BLACK);
        addWoolVariants(BlockSkinProperties.WOOL_RED);
        addWoolVariants(BlockSkinProperties.WOOL_BROWN);
        addWoolVariants(BlockSkinProperties.WOOL_DARK_RED);
        addWoolVariants(BlockSkinProperties.WOOL_PURPLE);
        addWoolVariants(BlockSkinProperties.WOOL_AQUA);
        addWoolVariants(BlockSkinProperties.WOOL_LIGHT_GRAY);
        addWoolVariants(BlockSkinProperties.WOOL_DARK_GRAY);
        addWoolVariants(BlockSkinProperties.WOOL_PINK);
        addWoolVariants(BlockSkinProperties.WOOL_GREEN);
        addWoolVariants(BlockSkinProperties.WOOL_YELLOW);
        addWoolVariants(BlockSkinProperties.WOOL_LIGHT_BLUE);
        addWoolVariants(BlockSkinProperties.WOOL_LIGHT_PURPLE);
        addWoolVariants(BlockSkinProperties.WOOL_ORANGE);
        addStoneVariants(BlockSkinProperties.ENDCOBBLE1);
        addStoneVariants(BlockSkinProperties.ENDCOBBLE2);
        addStoneVariants(BlockSkinProperties.DRAGON_SCALE);
        addStoneVariants(BlockSkinProperties.CHISELED_STONE);
        addStoneVariants(BlockSkinProperties.CHISELED_SANDSTONE);
        addStoneVariants(BlockSkinProperties.POLISHED_SANDSTONE);

        GRASS = new Grass();
        blocks.add(GRASS);
        STONE = new Stone(BlockSkinProperties.STONE);
        blocks.add(STONE);

        GRAVEL = new Gravel();
        blocks.add(GRAVEL);
        LOG = new Log();
        blocks.add(LOG);
        GLASS = new Glass();
        blocks.add(GLASS);
        BEDROCK = new Bedrock();
        blocks.add(BEDROCK);
        DIRT = new Dirt();
        blocks.add(DIRT);
        COAL_BLOCK = new Stone(BlockSkinProperties.COAL_BLOCK);
        blocks.add(COAL_BLOCK);
        GOLD_ORE = new Stone(BlockSkinProperties.GOLD_ORE);
        blocks.add(GOLD_ORE);
        IRON_ORE = new Stone(BlockSkinProperties.IRON_ORE);
        blocks.add(IRON_ORE);
        DIAMOND_ORE = new Stone(BlockSkinProperties.DIAMOND_ORE);
        blocks.add(DIAMOND_ORE);
        REDSTONE_ORE = new Stone(BlockSkinProperties.REDSTONE_ORE);
        blocks.add(REDSTONE_ORE);
        SAND = new Sand();
        blocks.add(SAND);
        BIRCHLOG = new BirchLog();
        blocks.add(BIRCHLOG);
        BOOKCASE= new BookCase();
        blocks.add(BOOKCASE);
        CACTUS = new Cactus();
        blocks.add(CACTUS);
        ICE = new Ice();
        blocks.add(ICE);
        SNOW = new Snow();
        blocks.add(SNOW);
        SNOWBLOCK = new SnowBlock();
        blocks.add(SNOWBLOCK);
        SPAWNER = new Spawner();
        blocks.add(SPAWNER);
        SPONGE= new Stone(BlockSkinProperties.SPONGE);
        blocks.add(SPONGE);
        PUMPKIN = new Pumpkin();
        blocks.add(PUMPKIN);
        FURNACE = new FurnaceUnlit();
        blocks.add(FURNACE);
        CRAFTING_TABLE = new CraftingTable();
        blocks.add(CRAFTING_TABLE);

        TNT = new TNT();
        blocks.add(TNT);
        BLUE = new Blue();
        blocks.add(BLUE);

        CHEST = new Chest();
        blocks.add(CHEST);
        FURNACELIT = new FurnaceLit();
        blocks.add(FURNACELIT);
        GOLDBLOCK = new Stone(BlockSkinProperties.GOLD_BLOCK);
        blocks.add(GOLDBLOCK);
        IRONBLOCK = new Stone(BlockSkinProperties.IRON_BLOCK);
        blocks.add(IRONBLOCK);
        JACKOLANTERN = new JackOLantern();
        blocks.add(JACKOLANTERN);
        LAVA = new Lava();
        blocks.add(LAVA);
        NETERAK = new Stone(BlockSkinProperties.NETHERAK);
        blocks.add(NETERAK);
        NOTEBLOCK = new NoteBlock();
        blocks.add(NOTEBLOCK);
        SOUL_SAND = new SoulSand();
        blocks.add(SOUL_SAND);
        WATER = new Water();
        blocks.add(WATER);
        LEAVES = new Leaves();
        blocks.add(LEAVES);

        WOODEN_DOOR_TOP = new WoodenDoorTop();
        blocks.add(WOODEN_DOOR_TOP);
        WOODEN_DOOR_BOTTOM = new WoodenDoorBottom();
        blocks.add(WOODEN_DOOR_BOTTOM);


        LAPIS_BLOCK = new Stone(BlockSkinProperties.LAPISBLOCK);
        blocks.add(LAPIS_BLOCK);
        LAPIS_ORE = new Stone(BlockSkinProperties.LAPIS_ORE);
        blocks.add(LAPIS_ORE);
        //ENDSTONE = new Stone(BlockSkinProperties.ENDSTONE);
        //blocks.add(ENDSTONE);

        REPEATER_OFF = new Stone(BlockSkinProperties.REPEATER_OFF);
        blocks.add(REPEATER_OFF);
        REPEATHER_ON = new Stone(BlockSkinProperties.REPEATER_ON);
        blocks.add(REPEATHER_ON);
        REDSTONE_LAMP_OFF = new Stone(BlockSkinProperties.REDSTONE_LAMP_OFF);
        blocks.add(REDSTONE_LAMP_OFF);

        TRAPDOOR = new Wood(BlockSkinProperties.TRAP_DOOR);
        blocks.add(TRAPDOOR);

        MOSSY_STONE_BRICK = new Stone(BlockSkinProperties.MOSSY_STONE_BRICK);
        blocks.add(MOSSY_STONE_BRICK);
        REDSTONE_LAMP_ON = new Stone(BlockSkinProperties.REDSTONE_LAMP_ON);
        blocks.add(REDSTONE_LAMP_ON);

        IRON_BARS = new Stone(BlockSkinProperties.IRON_BARS);
        blocks.add(IRON_BARS);
        CRACKED_STONE_BRICK = new Stone(BlockSkinProperties.CRACKED_STONE_BRICK);
        blocks.add(CRACKED_STONE_BRICK);

        SPRUCE_LOG = new Stone(BlockSkinProperties.SPRUCE_LOG);
        blocks.add(SPRUCE_LOG);

        OAK_LEAVES = new OakLeaves();
        blocks.add(OAK_LEAVES);
        
        TORCH = new Torch(BlockSkinProperties.TORCH_LIT);
        blocks.add(TORCH);
    }

    public static void addWoodVariants(BlockSkin blockSkin)
    {
        JBlock block = new Wood(blockSkin);
        blocks.add(block);
        block = new Wood(blockSkin, 0);
        blocks.add(block);
        block = new Wood(blockSkin, 1);
        blocks.add(block);
        block = new Wood(blockSkin, 2);
        blocks.add(block);
        block = new Wood(blockSkin, 3);
        blocks.add(block);
        block = new Wood(blockSkin, 4);
        blocks.add(block);
        block = new Wood(blockSkin, 5);
        blocks.add(block);
        block = new ConnectorRod(blockSkin);
        blocks.add(block);
    }

    public static void addWoolVariants(BlockSkin blockSkin)
    {
        JBlock block = new Wool(blockSkin);
        blocks.add(block);
        block = new Wool(blockSkin, 0);
        blocks.add(block);
        block = new Wool(blockSkin, 1);
        blocks.add(block);
        block = new Wool(blockSkin, 2);
        blocks.add(block);
        block = new Wool(blockSkin, 3);
        blocks.add(block);
        block = new Wool(blockSkin, 4);
        blocks.add(block);
        block = new Wool(blockSkin, 5);
        blocks.add(block);
        block = new ConnectorRod(blockSkin);
        blocks.add(block);
    }

    public static void addStoneVariants(BlockSkin blockSkin)
    {
        JBlock block = new Stone(blockSkin);
        blocks.add(block);
        block = new Stone(blockSkin, 0);
        blocks.add(block);
        block = new Stone(blockSkin, 1);
        blocks.add(block);
        block = new Stone(blockSkin, 2);
        blocks.add(block);
        block = new Stone(blockSkin, 3);
        blocks.add(block);
        block = new Stone(blockSkin, 4);
        blocks.add(block);
        block = new Stone(blockSkin, 5);
        blocks.add(block);
        block = new ConnectorRod(blockSkin);
        blocks.add(block);
    }

    public static JBlock nextBlock(JBlock block)
    {
        int index = blocks.indexOf(block) + 1;
        if(index > blocks.size() - 1)
        {
            index = 0;
        }
        return blocks.get(index);
    }

    public static JBlock previousBlock(JBlock block)
    {
        int index = blocks.indexOf(block) - 1;
        if(index < 0)
        {
            index = blocks.size() - 1;
        }
        return blocks.get(index);
    }

    public static List<JBlock> getBlocks()
    {
        return blocks;
    }
}
