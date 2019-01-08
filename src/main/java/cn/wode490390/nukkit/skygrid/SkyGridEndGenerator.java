package cn.wode490390.nukkit.skygrid;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.wode490390.nukkit.skygridgenerator.SGGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SkyGridEndGenerator extends Generator {

    private ChunkManager level;
    private Random random;
    private NukkitRandom nukkitRandom;
    private long localSeed1;
    private long localSeed2;

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_THE_END;
    }

    @Override
    public String getName() {
        return "skygrid_end";
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.level;
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    public SkyGridEndGenerator() {
        this(new HashMap<>());
    }

    public SkyGridEndGenerator(Map<String, Object> options) {

    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.random = new Random();
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(chunkX * this.localSeed1 ^ chunkZ * this.localSeed2 ^ this.level.getSeed());
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = 0; y <= SGGenerator.getInstance().endHeight; y += 4) {
                    int[] block = SGGenerator.getInstance().endBlock.get(this.nukkitRandom.nextBoundedInt(SGGenerator.getInstance().endBlock.size() - 1));
                    chunk.setBlock(x, y, z, block[0], block[1]);
                }
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunk.setBiome(x, z, EnumBiome.OCEAN.biome);
            }
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {

    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(100, SGGenerator.getInstance().endHeight + 1, 0);
    }
}
