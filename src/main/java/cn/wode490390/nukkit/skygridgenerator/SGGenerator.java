package cn.wode490390.nukkit.skygridgenerator;

import cn.nukkit.block.Block;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import cn.wode490390.nukkit.skygrid.*;
import java.util.ArrayList;
import java.util.List;

public class SGGenerator extends PluginBase {

    protected boolean overworldEnable = true;
    protected boolean oldEnable = true;
    public int overworldHeight = 128;
    public boolean overworldBiome = true;
    public List<int[]> overworldBlock = new ArrayList<>();

    protected boolean netherEnable = true;
    public int netherHeight = 128;
    public List<int[]> netherBlock = new ArrayList<>();

    protected boolean endEnable = true;
    public int endHeight = 128;
    public List<int[]> endBlock = new ArrayList<>();

    private static SGGenerator instance;

    public static SGGenerator getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        String cfg;
        List<List<Integer>> blocks;
        cfg = "overworld.enable";
        try {
            this.overworldEnable = this.getConfig().getBoolean(cfg);
        } catch (Exception e) {
            this.logLoadException(cfg);
        }
        if (this.overworldEnable) {
            Generator.addGenerator(SkyGridGenerator.class, "normal", Generator.TYPE_INFINITE);
            Generator.addGenerator(SkyGridGenerator.class, "default", Generator.TYPE_INFINITE);
        }
        cfg = "overworld.enable-old";
        try {
            this.oldEnable = this.getConfig().getBoolean(cfg);
        } catch (Exception e) {
            this.logLoadException(cfg);
        }
        if (this.oldEnable) Generator.addGenerator(SkyGridOldGenerator.class, "old", Generator.TYPE_OLD);
        if (this.overworldEnable || this.oldEnable) {
            cfg = "overworld.height";
            try {
                this.overworldHeight = this.getConfig().getInt(cfg);
            } catch (Exception e) {
                this.logLoadException(cfg);
            }
            cfg = "overworld.biome";
            try {
                this.overworldBiome = this.getConfig().getBoolean(cfg);
            } catch (Exception e) {
                this.logLoadException(cfg);
            }
            blocks = new ArrayList<>();
            cfg = "overworld.block";
            try {
                blocks = this.getConfig().getList(cfg);
            } catch (Exception e) {
                this.logLoadException(cfg);
            }
            for (List<Integer> block : blocks) {
                try {
                    this.overworldBlock.add(new int[] {
                            Math.abs(block.get(0)) % 256,
                            Math.abs(block.get(1))
                    });
                } catch (Exception e) {
                    this.logParseException(cfg);
                }
            }
            if (this.overworldBlock.isEmpty()) {
                this.overworldBlock.add(new int[] {
                        Block.STONE,
                        0
                });
            }
        }
        cfg = "nether.enable";
        try {
            this.netherEnable = this.getConfig().getBoolean(cfg);
        } catch (Exception e) {
            this.logLoadException(cfg);
        }
        if (this.netherEnable) {
            Generator.addGenerator(SkyGridNetherGenerator.class, "nether", Generator.TYPE_NETHER);
            cfg = "nether.height";
            try {
                this.netherHeight = this.getConfig().getInt(cfg);
            } catch (Exception e) {
                this.logLoadException(cfg);
            }
            blocks = new ArrayList<>();
            cfg = "nether.block";
            try {
                blocks = this.getConfig().getList(cfg);
            } catch (Exception e) {
                this.logLoadException(cfg);
            }
            for (List<Integer> block : blocks) {
                try {
                    this.netherBlock.add(new int[] {
                            Math.abs(block.get(0)) % 256,
                            Math.abs(block.get(1))
                    });
                } catch (Exception e) {
                    this.logParseException(cfg);
                }
            }
            if (this.netherBlock.isEmpty()) {
                this.netherBlock.add(new int[] {
                        Block.NETHERRACK,
                        0
                });
            }
        }
        cfg = "end.enable";
        try {
            this.endEnable = this.getConfig().getBoolean(cfg);
        } catch (Exception e) {
            this.logLoadException(cfg);
        }
        if (this.endEnable) {
            Generator.addGenerator(SkyGridEndGenerator.class, "end", 4);
            cfg = "end.height";
            try {
                this.endHeight = this.getConfig().getInt(cfg);
            } catch (Exception e) {
                this.logLoadException(cfg);
            }
            blocks = new ArrayList<>();
            cfg = "end.block";
            try {
                blocks = this.getConfig().getList(cfg);
            } catch (Exception e) {
                this.logLoadException(cfg);
            }
            for (List<Integer> block : blocks) {
                try {
                    this.endBlock.add(new int[] {
                            Math.abs(block.get(0)) % 256,
                            Math.abs(block.get(1))
                    });
                } catch (Exception e) {
                    this.logParseException(cfg);
                }
            }
            if (this.endBlock.isEmpty()) {
                this.endBlock.add(new int[] {
                        Block.END_STONE,
                        0
                });
            }
        }
    }

    private void logLoadException(String text) {
        this.getLogger().alert("An error occurred while reading the configuration '" + text + "'. Use the default value.");
    }

    private void logParseException(String text) {
        this.getLogger().alert("An error occurred while parsing the configuration '" + text + "'.");
    }
}
