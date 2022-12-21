package win.oreo.quest;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.quest.command.npc.npcCommand;
import win.oreo.quest.command.npc.npcCompleter;
import win.oreo.quest.listener.npc.DeathListener;
import win.oreo.quest.listener.npc.PreLoginListener;
import win.oreo.quest.listener.npc.playerMovementListener;
import win.oreo.quest.manager.npcYmlManager;
import win.oreo.quest.util.Color;
import win.oreo.quest.util.npc.NPCPlayer;
import win.oreo.quest.version.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main plugin;

    public FileConfiguration config;
    public npcYmlManager ymlManager;

    private boolean usesPaper = false;
    private boolean updatedPaper = false;

    public static List<EntityPlayer> npcs = new ArrayList<>();

    private Version version = Version.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);

    public static Main getPlugin() {
        return plugin;
    }

    public boolean usesPaper() {
        return usesPaper;
    }

    public boolean isPaperUpdated() {
        return updatedPaper;
    }

    public Version getVersion() {
        return version;
    }

    public void checkForClasses() {
        try {
            usesPaper = Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
            if (usesPaper) {
                Bukkit.getLogger().info("Paper detected.");
            }
        } catch (ClassNotFoundException ignored) {

        }
        try {
            updatedPaper = Class.forName("net.kyori.adventure.text.ComponentLike") != null;
        } catch (ClassNotFoundException ignored) {

        }
    }

    @Override
    public void onEnable() {
        if (version == null) {
            Bukkit.getLogger().warning("ERROR! NOT SUPPORTED VERSION!");
        }

        Bukkit.getLogger().info("Detected version : " + version.name());

        getCommand("npc").setExecutor(new npcCommand());
        getCommand("npc").setTabCompleter(new npcCompleter());

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new PreLoginListener(), this);
        getServer().getPluginManager().registerEvents(new playerMovementListener(), this);

        plugin = this;

        checkForClasses();

        this.saveDefaultConfig();
        config = this.getConfig();
        this.ymlManager = new npcYmlManager(this);

        initializeNPC();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().setCollidesWithEntities(false);
        }

        Bukkit.getConsoleSender().sendMessage("load complete!");
    }

    @Override
    public void onDisable() {
        saveNPC();
        List<NPCPlayer> list = new ArrayList<>(NPCPlayer.getNPCPlayerList());
        for (NPCPlayer player : list) {
            player.removePlayer();
        }
    }

    public void initializeNPC() {
        String[] args = new String[2];
        for (String uuid : plugin.ymlManager.getConfig().getConfigurationSection("npc.").getKeys(false)) {
            if (NPCPlayer.summon(plugin.ymlManager.getConfig().getString("npc." + uuid + ".name"),
                    plugin.ymlManager.getConfig().getDouble("npc." + uuid + ".locX"),
                    plugin.ymlManager.getConfig().getDouble("npc." + uuid + ".locY"),
                    plugin.ymlManager.getConfig().getDouble("npc." + uuid + ".locZ"),
                    UUID.fromString(uuid),
                    plugin.ymlManager.getConfig().getString("npc." + uuid + ".skin"))) {
                args[0] = plugin.ymlManager.getConfig().getString("npc." + uuid + ".name");
                args[1] = uuid;
                Bukkit.getConsoleSender().sendMessage( getConfigMessage(config, "messages.npc.npc-load", args));
            }
        }
        for (EntityPlayer player : Main.npcs) {
            if (player.getBukkitEntity().isDead()) {
                player.getBukkitEntity().spigot().respawn();
            }
        }
    }

    public static UUID getRandomUUID(String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        return offlinePlayer.getUniqueId();
    }

    public void saveNPC() {
        String[] args = new String[2];
        for (EntityPlayer entityPlayer : Main.npcs) {
            for (NPCPlayer npcPlayer : NPCPlayer.npcPlayerList) {
                if (npcPlayer.getEntityPlayer().equals(entityPlayer)) {
                    plugin.ymlManager.getConfig().set("npc." + npcPlayer.getUUID().toString() + ".name", npcPlayer.getName());
                    plugin.ymlManager.getConfig().set("npc." + npcPlayer.getUUID().toString() + ".locX", npcPlayer.getEntityPlayer().getBukkitEntity().getLocation().getX());
                    plugin.ymlManager.getConfig().set("npc." + npcPlayer.getUUID().toString() + ".locY", npcPlayer.getEntityPlayer().getBukkitEntity().getLocation().getY());
                    plugin.ymlManager.getConfig().set("npc." + npcPlayer.getUUID().toString() + ".locZ", npcPlayer.getEntityPlayer().getBukkitEntity().getLocation().getZ());
                    plugin.ymlManager.getConfig().set("npc." + npcPlayer.getUUID().toString() + ".skin", npcPlayer.getSkinName());
                    plugin.ymlManager.saveConfig();
                    args[0] = npcPlayer.getName();
                    args[1] = npcPlayer.getUUID().toString();
                    Bukkit.getConsoleSender().sendMessage( getConfigMessage(config, "messages.npc.npc-save", args));
                }
            }
        }
    }

    public static String getConfigMessage(FileConfiguration config, String path, String[] args) {
        String text = config.getString(path);
        String prefix = config.getString("prefix");
        if (text == null) {
            return ChatColor.RED +"ERROR";
        }
        if (path.contains("account")) {
            prefix = config.getString("prefixAcc");
        } else if (path.contains("npc")) {
            prefix = config.getString("prefixNpc");
        }

        boolean open = false;
        StringBuilder chars = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == '%') {
                if (open) {
                    final char[] CHARACTERS = chars.toString().toCharArray();
                    if (CHARACTERS[0] == 'a' && CHARACTERS[1] == 'r' && CHARACTERS[2] == 'g') {
                        final int ARG = Integer.parseInt(String.valueOf(CHARACTERS[3]));

                        text = text.replace(chars.toString(), args[ARG]);

                        chars = new StringBuilder();
                    }
                    open = false;
                } else {
                    open = true;
                }
                continue;
            }

            if (open) {
                chars.append(c);
            }
        }

        return Color.format(prefix + " " + text.replace("%", ""));
    }
}