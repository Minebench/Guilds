package io.github.apfelcreme.Guilds;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Alliance.AllianceInvite;
import io.github.apfelcreme.Guilds.Bungee.BungeeChat;
import io.github.apfelcreme.Guilds.Bungee.SimpleBungeeChat;
import io.github.apfelcreme.Guilds.Command.Admin.AdminCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Chat.AllianceChatCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Alliance.AllianceCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Chat.GuildChatCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Guild.GuildCommandExecutor;
import io.github.apfelcreme.Guilds.Command.Guild.GuildTabCompleter;
import io.github.apfelcreme.Guilds.Guild.*;
import io.github.apfelcreme.Guilds.Listener.*;
import io.github.apfelcreme.Guilds.Manager.*;
import net.milkbowl.vault.economy.Economy;
import net.zaiyers.UUIDDB.bukkit.UUIDDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
     * the chat
     */
    private BungeeChat chat;

    /**
     * the guilds set
     */
    private Map<Integer, Guild> guilds;

    /**
     * the guilds set
     */
    private Map<Integer, Alliance> alliances;

    /**
     * the UUIDDB plugin
     */
    private static UUIDDB uuiddb;

    /**
     * the vault economy
     */

    private Economy economy;

    /**
     * do stuff on enable
     */
    public void onEnable() {

        if (!setupEconomy() ) {
            getLogger().severe("No Vault economy found?");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        guilds = new Hashtable<Integer, Guild>();
        alliances = new Hashtable<Integer, Alliance>();

        //set commandExecutors
        getServer().getPluginCommand("guild").setExecutor(new GuildCommandExecutor());
        getServer().getPluginCommand("guild").setTabCompleter(new GuildTabCompleter());
        getServer().getPluginCommand("alliance").setExecutor(new AllianceCommandExecutor(this));
        getServer().getPluginCommand("guildadmin").setExecutor(new AdminCommandExecutor());
        getServer().getPluginCommand(".").setExecutor(new GuildChatCommandExecutor());
        getServer().getPluginCommand(",").setExecutor(new AllianceChatCommandExecutor());

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Guilds");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "Guilds", new BungeeMessageListener());

        if (GuildsConfig.isEnchantmentBonusActivated()) {
            Bukkit.getPluginManager().registerEvents(new EnchantmentListener(), this);
        }
        if (GuildsConfig.isDoubleCraftingBonusActivated()) {
            Bukkit.getPluginManager().registerEvents(new CraftItemListener(), this);
        }
        if (GuildsConfig.isMoreFurnaceExpBonusActivated()) {
            Bukkit.getPluginManager().registerEvents(new FurnaceExtractListener(), this);
        }
        if (GuildsConfig.isSpecialDropBonusActivated()) {
            Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        }
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);

        if (getServer().getPluginManager().isPluginEnabled("UUIDDB")) {
            uuiddb = UUIDDB.getInstance();
        }

        GuildsConfig.init();

        GuildsConfig.getNewRandomDrop();

        DatabaseConnectionManager.getInstance().initConnection();

        chat = new SimpleBungeeChat();

        loadGuilds();

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
     */
    @Deprecated
    public static UUID getUUID(String playerName) {
        Player onlinePlayer = Guilds.getInstance().getServer().getPlayerExact(playerName);

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
                OfflinePlayer offlinePlayer = Guilds.getInstance().getServer().getOfflinePlayer(playerName);
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
     * loads the list of all guilds
     */
    public void loadGuilds() {
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        guilds.clear();
                        PreparedStatement statement = connection.prepareStatement("Select guildId from " + GuildsConfig.getGuildsTable());
                        ResultSet resultSet = statement.executeQuery();
                        int z = 0;
                        while (resultSet.next()) {
                            reloadGuild(resultSet.getInt("guildId"));
                            z++;
                        }
                        getLogger().info(z + " Gilden synchronisiert");
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * loads the list of all guilds
     */
    public void loadAlliances() {
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
//                        guilds.clear();
                        alliances.clear();
                        PreparedStatement statement = connection.prepareStatement("Select allianceId from " + GuildsConfig.getAllianceTable());
                        ResultSet resultSet = statement.executeQuery();
                        int z = 0;
                        while (resultSet.next()) {
                            reloadAlliance(resultSet.getInt("allianceId"));
                            z++;
                        }
                        getLogger().info(z + " Allianzen synchronisiert");
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * reloads a single guild
     *
     * @param guildId the id of the guild to be reloaded
     */
    public void reloadGuild(final Integer guildId) {
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
            public void run() {

                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        Guild guild;

                        /**
                         * load the players, their ranks and the guild itself
                         */
                        PreparedStatement statement = connection.prepareStatement(
                                "Select " +
                                        "  g.guildId, g.guild, g.tag, g.color, g.balance, g.exp, g.level, g.founded, " +
                                        "  g.allianceId, g.guildHomeX, g.guildHomeY, g.guildHomeZ, g.guildHomeWorld, " +
                                        "  g.guildHomeServer " +
                                        "from " + GuildsConfig.getGuildsTable() + " g " +
                                        "where g.guildId = ? ");
                        statement.setInt(1, guildId);
                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.first()) {
                            Integer guildId = resultSet.getInt("guildId");
                            Integer allianceId = resultSet.getInt("allianceId");
                            String name = resultSet.getString("guild");
                            String tag = resultSet.getString("tag");
                            double balance = resultSet.getDouble("balance");
                            ChatColor color = ChatColor.valueOf(resultSet.getString("color"));
                            int exp = resultSet.getInt("exp");
                            int level = resultSet.getInt("level");
                            Date founded = new Date(resultSet.getLong("founded"));
                            Double guildHomeX = resultSet.getDouble("guildHomeX");
                            Double guildHomeY = resultSet.getDouble("guildHomeY");
                            Double guildHomeZ = resultSet.getDouble("guildHomeZ");
                            String guildHomeWorld = resultSet.getString("guildHomeWorld");
                            String guildHomeServer = resultSet.getString("guildHomeServer");
                            List<GuildMember> members = new ArrayList<GuildMember>();

                            statement = connection.prepareStatement("select p.*, r.* " +
                                    "from " + GuildsConfig.getPlayerTable() + " p " +
                                    "left join " + GuildsConfig.getRanksTable() + " r on p.rankId = r.rankId " +
                                    "where p.guildId = ?");
                            statement.setInt(1, guildId);
                            resultSet = statement.executeQuery();

                            while (resultSet.next()) {
                                GuildMember guildMember = new GuildMember(
                                        UUID.fromString(resultSet.getString("uuid")),
                                        resultSet.getString("playerName"),
                                        resultSet.getLong("lastSeen"),
                                        resultSet.getLong("joined"),
                                        resultSet.getString("prefix"),
                                        new Rank(
                                                resultSet.getInt("rankId"),
                                                resultSet.getString("rankName"),
                                                resultSet.getBoolean("canInvite"),
                                                resultSet.getBoolean("canKick"),
                                                resultSet.getBoolean("canPromote"),
                                                resultSet.getBoolean("canDisband"),
                                                resultSet.getBoolean("canUpgrade"),
                                                resultSet.getBoolean("canWithdrawMoney"),
                                                resultSet.getBoolean("canUseBlackboard"),
                                                resultSet.getBoolean("canDoDiplomacy"),
                                                resultSet.getBoolean("isBaseRank"),
                                                resultSet.getBoolean("isLeader")
                                        )
                                );
                                members.add(guildMember);
                            }
                            statement.close();

                            /**
                             * load all the ranks (this has to be done, as some ranks may exist but no one carries
                             * them at this time.
                             */
                            statement = connection.prepareStatement(
                                    " Select r.rankId, r.rankName, r.canInvite, r.canKick, r.canPromote, " +
                                            "r.canDisband, r.canUpgrade, r.canWithdrawMoney, r.canUseBlackboard, " +
                                            "r.canDoDiplomacy, r.isBaseRank, r.isLeader " +
                                            "FROM " + GuildsConfig.getRanksTable() + " r " +
                                            "where guildId = ?");
                            statement.setInt(1, guildId);
                            resultSet = statement.executeQuery();

                            List<Rank> ranks = new ArrayList<Rank>();
                            while (resultSet.next()) {
                                ranks.add(new Rank(
                                        resultSet.getInt("rankId"),
                                        resultSet.getString("rankName"),
                                        resultSet.getBoolean("canInvite"),
                                        resultSet.getBoolean("canKick"),
                                        resultSet.getBoolean("canPromote"),
                                        resultSet.getBoolean("canDisband"),
                                        resultSet.getBoolean("canUpgrade"),
                                        resultSet.getBoolean("canWithdrawMoney"),
                                        resultSet.getBoolean("canUseBlackboard"),
                                        resultSet.getBoolean("canDoDiplomacy"),
                                        resultSet.getBoolean("isBaseRank"),
                                        resultSet.getBoolean("isLeader")
                                ));
                            }
                            statement.close();

                            GuildLevel guildLevel = GuildsConfig.getLevelData(level);

                            guild = new Guild(guildId, GuildsUtil.replaceChatColors(name),
                                    GuildsUtil.replaceChatColors(tag), color, balance, exp, members,
                                    ranks, guildLevel, founded, guildHomeX, guildHomeY, guildHomeZ, guildHomeWorld,
                                    guildHomeServer);
                            statement.close();

                            for (Rank rank : guild.getRanks()) {
                                rank.setGuild(guild);
                            }
                            for (GuildMember guildMember : guild.getMembers()) {
                                guildMember.getRank().setGuild(guild);
                                guildMember.setGuild(guild);
                            }

                            /**
                             * load the latest blackboard messages
                             */
                            statement = connection.prepareStatement(
                                    "SELECT s.* from (Select messageId, player, message, timestamp from "
                                            + GuildsConfig.getBlackboardTable() + " " +
                                            "where guildId = ? and cleared = 0 " +
                                            "ORDER BY timestamp desc LIMIT ?) as s order by timestamp asc");
                            statement.setInt(1, guildId);
                            statement.setInt(2, GuildsConfig.getBlackboardMessageLimit());
                            resultSet = statement.executeQuery();
                            resultSet.beforeFirst();
                            while (resultSet.next()) {
                                Integer messageId = resultSet.getInt("messageId");
                                UUID sender = UUID.fromString(resultSet.getString("player"));
                                String message = resultSet.getString("message");
                                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                                guild.addBlackboardMessage(new BlackboardMessage(messageId, sender, timestamp, message, guild));
                            }
                            statement.close();

                            /**
                             * load the pending invites which are yet to be accepted or declined
                             */
                            statement = connection.prepareStatement(
                                    "SELECT player, targetPlayer " +
                                            "from " + GuildsConfig.getInvitesTable() +
                                            " where status = 0 and guildId = ?");
                            statement.setInt(1, guildId);
                            resultSet = statement.executeQuery();
                            resultSet.beforeFirst();
                            while (resultSet.next()) {
                                UUID targetPlayer = UUID.fromString(resultSet.getString("targetPlayer"));
                                guild.putPendingInvite(targetPlayer, new Invite(guild, targetPlayer,
                                        guild.getMember(UUID.fromString(resultSet.getString("player"))), null));
                            }
                            statement.close();

                            guilds.remove(guildId);
                            guilds.put(guildId, guild);

                            if (!alliances.containsKey(allianceId)) {
                                reloadAlliance(allianceId);
                            }
                        }

                        connection.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ConcurrentModificationException e) {
                        e.printStackTrace();
                        getLogger().info("Beim Laden ist ein Fehler aufgetreten!");
                    }
                }
            }
        });
    }

    /**
     * reloads an alliance and all guilds that are in it
     *
     * @param allianceId the id of the alliance
     */
    public void reloadAlliance(final Integer allianceId) {
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
            public void run() {

                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        Alliance alliance;

                        PreparedStatement statement = connection.prepareStatement(
                                "Select g.guildId, a.allianceId, a.alliance, a.tag as allianceTag, a.color as allianceColor, a.founded from " + GuildsConfig.getGuildsTable() + " g " +
                                        "left join " + GuildsConfig.getAllianceTable() + " a on g.allianceId = a.allianceId " +
                                        "where a.allianceId = ?");
                        statement.setInt(1, allianceId);
                        ResultSet resultSet = statement.executeQuery();
                        List<Guild> allianceMembers = new ArrayList<Guild>();
                        if (resultSet.next()) {
                            Integer allianceId = resultSet.getInt("allianceId");
                            String tag = resultSet.getString("allianceTag");
                            resultSet.beforeFirst();
                            while (resultSet.next()) {
                                allianceMembers.add(getGuild(resultSet.getInt("guildId")));
                            }
                            resultSet.first();
                            alliance = new Alliance(allianceId,
                                    GuildsUtil.replaceChatColors(resultSet.getString("a.alliance")),
                                    GuildsUtil.replaceChatColors(resultSet.getString("allianceTag")),
                                    ChatColor.valueOf(resultSet.getString("allianceColor")),
                                    resultSet.getLong("founded"),
                                    allianceMembers);

                            /**
                             * load the pending AllianceInvites which are yet to be accepted or declined
                             */
                            statement = connection.prepareStatement(
                                    "SELECT guildId " +
                                            "from " + GuildsConfig.getAllianceInviteTable() +
                                            " where status = 0 and allianceId = ?");
                            statement.setInt(1, allianceId);
                            resultSet = statement.executeQuery();
                            resultSet.beforeFirst();
                            while (resultSet.next()) {
                                Guild guild = getGuild(resultSet.getInt("guildId"));
                                alliance.putPendingAllianceInvite(
                                        new AllianceInvite(
                                                alliance,
                                                guild
                                        )
                                );
                            }

                            alliances.remove(allianceId);
                            alliances.put(allianceId, alliance);
                            getLogger().info("Allianz " + alliance.getName() + " [" + tag + "] geladen");
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ConcurrentModificationException e) {
                        e.printStackTrace();
                        getLogger().info("Beim Laden ist ein Fehler aufgetreten!");
                    }
                }
            }
        });
    }

    /**
     * returns a list of all guilds
     *
     * @return all guilds in a list
     */
    public Collection<Guild> getGuilds() {
        return guilds.values();
    }

    /**
     * returns a list of all alliances
     *
     * @return all alliances in a list
     */
    public Collection<Alliance> getAlliances() {
        return alliances.values();
    }

    /**
     * returns the players guild
     *
     * @param player the player
     * @return the players guild
     */
    public Guild getGuild(OfflinePlayer player) {
        for (Guild guild : guilds.values()) {
            if (guild.getMember(player.getUniqueId()) != null) {
                return guild;
            }
        }
        return null;
    }

    /**
     * returns the guild with the given id
     *
     * @param id the guild id
     * @return the matching guild
     */
    public Guild getGuild(Integer id) {
        return guilds.get(id);
    }

    /**
     * returns the guild with the given name
     *
     * @param name the guild name
     * @return the guild
     */
    public Guild getGuild(String name) {
        for (Guild guild : guilds.values()) {
            if (GuildsUtil.strip(guild.getName()).equals(GuildsUtil.strip(name))) {
                return guild;
            }
        }
        return null;
    }

    /**
     * returns the players guild
     *
     * @param uuid the players uuid
     * @return the players guild
     */
    public Guild getGuild(UUID uuid) {
        for (Guild guild : guilds.values()) {
            if (guild.getMember(uuid) != null) {
                return guild;
            }
        }
        return null;
    }

    /**
     * returns a guild with the given tag
     *
     * @param tag the guild tag
     * @return the guild or null
     */
    public Guild getGuildByTag(String tag) {
        for (Guild guild : guilds.values()) {
            if (GuildsUtil.strip(guild.getTag()).equalsIgnoreCase(GuildsUtil.strip(tag))) {
                return guild;
            }
        }
        return null;
    }

    /**
     * returns an invite for a player if there is one
     *
     * @param targetPlayer the target player uuid
     * @return the invite sent to this player
     */
    public Invite getInvite(UUID targetPlayer) {
        for (Guild guild : guilds.values()) {
            for (Map.Entry<UUID, Invite> entry : guild.getPendingInvites().entrySet()) {
                if (entry.getKey().equals(targetPlayer)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * returns an invite for a player if there is one
     *
     * @param uuid the player uuid
     * @return the invite sent to this player
     */
    public GuildMember getGuildMember(UUID uuid) {
        for (Guild guild : guilds.values()) {
            if (guild.getMember(uuid) != null) {
                return guild.getMember(uuid);
            }
        }
        return null;
    }

    /**
     * returns a guilds alliance
     *
     * @param guild the guild
     * @return the alliance the given guild is part of
     */
    public Alliance getAlliance(Guild guild) {
        String guildName = guild.getName();
        for (Alliance alliance : alliances.values()) {
            for (Guild member : alliance.getGuilds()) {
                if (member.getName().equals(guildName)) {
                    return alliance;
                }
            }
        }
        return null;
    }

    /**
     * returns a players alliance
     *
     * @param player the player
     * @return the alliance the given players guild is in
     */
    public Alliance getAlliance(OfflinePlayer player) {
        for (Alliance alliance : alliances.values()) {
            for (Guild guild : alliance.getGuilds()) {
                if (guild.getMember(player.getUniqueId()) != null) {
                    return alliance;
                }
            }
        }
        return null;
    }

    /**
     * returns the alliance with the given name
     *
     * @param name the name
     * @return the alliance with the given name
     */
    public Alliance getAlliance(String name) {
        for (Alliance alliance : alliances.values()) {
            if (alliance.getName().equalsIgnoreCase(name)) {
                return alliance;
            }
        }
        return null;
    }


    /**
     * returns an alliance invite for a guild if there is one
     *
     * @param guild the guild
     * @return the invite sent to this guild
     */
    public AllianceInvite getAllianceInvite(Guild guild) {
        for (Alliance alliance : alliances.values())
            for (AllianceInvite invite : alliance.getPendingAllianceInvites()) {
                if (invite.getGuild().getName().equals(guild.getName())) {
                    return invite;
                }
            }

        return null;
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
     */
    public static Guilds getInstance() {
        return (Guilds) Bukkit.getServer().getPluginManager().getPlugin("Guilds");
    }


}
