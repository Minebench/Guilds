package io.github.apfelcreme.Guilds;

import io.github.apfelcreme.Guilds.Bungee.BungeeChat;
import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Bungee.SimpleBungeeChat;
import io.github.apfelcreme.Guilds.Command.Admin.AdminCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Chat.AllianceChatCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Alliance.AllianceCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Chat.GuildChatCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Guild.GuildCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Guild.GuildTabCompleter;
import io.github.apfelcreme.Guilds.Listener.*;
import io.github.apfelcreme.Guilds.Manager.*;
import net.milkbowl.vault.economy.Economy;
import net.zaiyers.UUIDDB.bukkit.UUIDDB;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Guilds
 * Copyright (C) 2015 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme on 23.04.2015.
 */
public class Guilds extends JavaPlugin {

    /**
     * Bungee connection helper
     */
    private BungeeConnection bungeeConnection;

    /**
     * the chat
     */
    private BungeeChat chat;

    /**
     * the guild manager
     */
    private GuildManager guildManager;

    /**
     * the alliance manager
     */
    private AllianceManager allianceManager;

    /**
     *  the request controller
     */
    private RequestController requestController;

    /**
     * the UUIDDB plugin
     */
    private UUIDDB uuiddb;

    /**
     * the vault economy
     */

    private Economy economy;

    /**
     * The plugin config object
     */
    private GuildsConfig guildsConfig;

    /**
     * The database connection manager
     */
    private DatabaseConnectionManager dbConnMan;

    /**
     * do stuff on enable
     */
    public void onEnable() {

        if (!setupEconomy() ) {
            getLogger().severe("No Vault economy found?");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        guildsConfig = new GuildsConfig(this);

        requestController = new RequestController(this);

        bungeeConnection = new BungeeConnection(this);

        guildManager = new GuildManager(this);
        allianceManager = new AllianceManager(this);

        dbConnMan = new DatabaseConnectionManager(this);

        if (getServer().getPluginManager().isPluginEnabled("UUIDDB")) {
            uuiddb = UUIDDB.getInstance();
        }

        chat = new SimpleBungeeChat(this);

        //set commandExecutors
        getServer().getPluginCommand("guild").setExecutor(new GuildCommandExecutor(this));
        getServer().getPluginCommand("guild").setTabCompleter(new GuildTabCompleter(this));
        getServer().getPluginCommand("alliance").setExecutor(new AllianceCommandExecutor(this));
        getServer().getPluginCommand("guildadmin").setExecutor(new AdminCommandExecutor(this));
        getServer().getPluginCommand(".").setExecutor(new GuildChatCommandExecutor(this));
        getServer().getPluginCommand(",").setExecutor(new AllianceChatCommandExecutor(this));

        getServer().getMessenger().registerOutgoingPluginChannel(this, "Guilds");
        getServer().getMessenger().registerIncomingPluginChannel(this, "Guilds", new BungeeMessageListener(this));

        if (getGuildsConfig().isEnchantmentBonusActivated()) {
            getServer().getPluginManager().registerEvents(new EnchantmentListener(this), this);
        }
        if (getGuildsConfig().isDoubleCraftingBonusActivated()) {
            getServer().getPluginManager().registerEvents(new CraftItemListener(this), this);
        }
        if (getGuildsConfig().isMoreFurnaceExpBonusActivated()) {
            getServer().getPluginManager().registerEvents(new FurnaceExtractListener(this), this);
        }
        if (getGuildsConfig().isSpecialDropBonusActivated()) {
            getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        }
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        getGuildManager().loadGuilds();

    }

    /**
     * do stuff on disable
     */
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (!hasVault()) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * is the plugin "Vault" available?
     *
     * @return true or false
     */
    public boolean hasVault() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    /**
     * returns the players uuid
     *
     * @param playerName the players name
     * @return the players uuid
     * @deprecated Why is this deprecated? Maybe it should be replaced with a full getGuildMember method?
     */
    @Deprecated
    public UUID getUUID(String playerName) {
        Player onlinePlayer = getServer().getPlayerExact(playerName);

        if (onlinePlayer != null) {
            return onlinePlayer.getUniqueId();
        } else {
            if (uuiddb != null) {
                String uuid = uuiddb.getStorage().getUUIDByName(playerName);
                if (uuid != null) {
                    return UUID.fromString(uuid);
                }
            }
            try {
                return UUIDFetcher.getUUIDOf(playerName);
            } catch (Exception ex) {
                OfflinePlayer offlinePlayer = getServer().getOfflinePlayer(playerName);
                if (offlinePlayer != null) {
                    return offlinePlayer.getUniqueId();
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * returns the chatInstance
     *
     * @return the chat
     */
    public BungeeChat getChat() {
        return chat;
    }


    /**
     * returns the servers Economy
     *
     * @return Vault economy object
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * returns the plugin instance
     *
     * @return the plugin instance
     * @deprecated References to the plugin should be passed directly instead of using a static getInstance() method
     */
    @Deprecated
    public static Guilds getInstance() {
        return (Guilds) Bukkit.getServer().getPluginManager().getPlugin("Guilds");
    }

    /**
     * Get the plugin specific config
     *
     * @return The GuildsConfig
     */
    public GuildsConfig getGuildsConfig() {
        return guildsConfig;
    }

    /**
     * Get the database connection from the plugin's DatabaseConnectionManager
     *
     * @return The plugin's database Connection
     */
    public Connection getDatabaseConnection() throws SQLException {
        return dbConnMan.getConnection();
    }

    /**
     * Get the plugin's guild manager
     * @return The GuildManager
     */
    public GuildManager getGuildManager() {
        return guildManager;
    }

    /**
     * Get the plugin's alliance manager
     * @return The AllianceManager
     */
    public AllianceManager getAllianceManager() {
        return allianceManager;
    }

    /**
     * Get the bungee plugin messageconnection helper
     * @return The BungeeConnection object
     */
    public BungeeConnection getBungeeConnection() {
        return bungeeConnection;
    }

    /**
     * Get the request controller
     * @return The RequestController object
     */
    public RequestController getRequestController() {
        return requestController;
    }

    /**
     * Run something async, if the current thread is already asnyc it wont create a new one!
     * @param runnable The runnable to run
     * @return The task id; -1 if no new one was created
     */
    public int runAsync(Runnable runnable) {
        if(getServer().isPrimaryThread()) {
            return getServer().getScheduler().runTaskAsynchronously(this, runnable).getTaskId();
        } else {
            runnable.run();
            return -1;
        }
    }

    /**
     * Run something sync, if the current thread is already snyc it wont create a new one!
     * @param runnable The runnable to run
     * @return The task id; -1 if no new one was created
     */
    public int runSync(Runnable runnable) {
        if(getServer().isPrimaryThread()) {
            runnable.run();
            return -1;
        } else {
            return getServer().getScheduler().runTask(this, runnable).getTaskId();
        }
    }

    /**
     * Log a debug message
     */
    public void debug(String message) {
        getLogger().log(getGuildsConfig().isDebugEnabled() ? Level.INFO : Level.FINE, message);
    }
}
