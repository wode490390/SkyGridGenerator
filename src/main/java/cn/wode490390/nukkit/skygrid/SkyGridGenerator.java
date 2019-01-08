package cn.wode490390.nukkit.skygrid;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.BiomeSelector;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.wode490390.nukkit.skygridgenerator.SGGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SkyGridGenerator extends Generator {

    private ChunkManager level;
    private Random random;
    private NukkitRandom nukkitRandom;
    private long localSeed1;
    private long localSeed2;
    private BiomeSelector selector;

    @Override
    public int getId() {
        return Generator.TYPE_INFINITE;
    }

    @Override
    public String getName() {
        return "skygrid";
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.level;
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    public SkyGridGenerator() {
        this(new HashMap<>());
    }

    public SkyGridGenerator(Map<String, Object> options) {

    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.random = new Random();
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();
        this.selector = new BiomeSelector(this.nukkitRandom);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(chunkX * this.localSeed1 ^ chunkZ * this.localSeed2 ^ this.level.getSeed());
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = 0; y <= SGGenerator.getInstance().overworldHeight; y += 4) {
                    int[] block = SGGenerator.getInstance().overworldBlock.get(this.nukkitRandom.nextBoundedInt(SGGenerator.getInstance().overworldBlock.size() - 1));
                    chunk.setBlock(x, y, z, block[0], block[1]);
                }
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (SGGenerator.getInstance().overworldBiome) {
                    chunk.setBiome(x, z, this.selector.pickBiome(baseX | x, baseZ | z));
                } else {
                    chunk.setBiome(x, z, EnumBiome.PLAINS.biome);
                }
            }
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {

    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0, SGGenerator.getInstance().overworldHeight + 1, 0);
    }
}
