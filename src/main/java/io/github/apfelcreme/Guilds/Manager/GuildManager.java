package io.github.apfelcreme.Guilds.Manager;

import io.github.apfelcreme.Guilds.Command.Guild.Session.EditRankSession;
import io.github.apfelcreme.Guilds.Guild.BlackboardMessage;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildLevel;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guild.Invite;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
 * <p/>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */
public class GuildManager {

    private Guilds plugin;

    /**
     * the guilds set
     */
    private Map<Integer, Guild> guilds;

    public GuildManager(Guilds plugin) {
        this.plugin = plugin;
        guilds = new Hashtable<Integer, Guild>();
    }

    /**
     * loads the list of all guilds
     */
    public void loadGuilds() {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                try {
                    guilds.clear();
                    connection = plugin.getDatabaseConnection();
                    statement = connection.prepareStatement("Select guildId from " + plugin.getGuildsConfig().getGuildsTable());
                    ResultSet resultSet = statement.executeQuery();
                    int z = 0;
                    while (resultSet.next()) {
                        reloadGuild(resultSet.getInt("guildId"));
                        z++;
                    }
                    plugin.getLogger().info(z + " Guilds synchronized");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }


    /**
     * reloads a single guild
     *
     * @param guildId the id of the guild to be reloaded
     */
    public void reloadGuild(final int guildId) {
        plugin.runAsync(new Runnable() {
            public void run() {

                Connection connection = null;
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    Guild guild;
                    /**
                     * load the players, their ranks and the guild itself
                     */
                    statement = connection.prepareStatement(
                            "Select " +
                                    "  g.guildId, g.guild, g.tag, g.color, g.balance, g.exp, g.level, g.founded, " +
                                    "  g.allianceId, g.guildHomeX, g.guildHomeY, g.guildHomeZ, g.guildHomeWorld, " +
                                    "  g.guildHomeServer " +
                                    "from " + plugin.getGuildsConfig().getGuildsTable() + " g " +
                                    "where g.guildId = ? ");
                    statement.setInt(1, guildId);
                    resultSet = statement.executeQuery();

                    if (resultSet.first()) {
                        int guildId = resultSet.getInt("guildId");
                        int allianceId = resultSet.getInt("allianceId");
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
                                "from " + plugin.getGuildsConfig().getPlayerTable() + " p " +
                                "left join " + plugin.getGuildsConfig().getRanksTable() + " r on p.rankId = r.rankId " +
                                "where p.guildId = ?");
                        statement.setInt(1, guildId);
                        resultSet = statement.executeQuery();

                        while (resultSet.next()) {
                            Player player = plugin.getServer().getPlayer(UUID.fromString(resultSet.getString("uuid")));
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
                                    ),
                                    player != null && player.isOnline()
                            );
                            members.add(guildMember);
                        }

                        /**
                         * load all the ranks (this has to be done, as some ranks may exist but no one carries
                         * them at this time.
                         */
                        statement = connection.prepareStatement(
                                " Select r.rankId, r.rankName, r.canInvite, r.canKick, r.canPromote, " +
                                        "r.canDisband, r.canUpgrade, r.canWithdrawMoney, r.canUseBlackboard, " +
                                        "r.canDoDiplomacy, r.isBaseRank, r.isLeader " +
                                        "FROM " + plugin.getGuildsConfig().getRanksTable() + " r " +
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

                        GuildLevel guildLevel = plugin.getGuildsConfig().getLevelData(level);

                        guild = new Guild(guildId, GuildsUtil.replaceChatColors(name),
                                GuildsUtil.replaceChatColors(tag), color, balance, exp, members,
                                ranks, guildLevel, founded, guildHomeX, guildHomeY, guildHomeZ, guildHomeWorld,
                                guildHomeServer);

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
                                        + plugin.getGuildsConfig().getBlackboardTable() + " " +
                                        "where guildId = ? and cleared = 0 " +
                                        "ORDER BY timestamp desc LIMIT ?) as s order by timestamp asc");
                        statement.setInt(1, guildId);
                        statement.setInt(2, plugin.getGuildsConfig().getBlackboardMessageLimit());
                        resultSet = statement.executeQuery();
                        resultSet.beforeFirst();
                        while (resultSet.next()) {
                            int messageId = resultSet.getInt("messageId");
                            UUID sender = UUID.fromString(resultSet.getString("player"));
                            String message = resultSet.getString("message");
                            Timestamp timestamp = resultSet.getTimestamp("timestamp");
                            guild.addBlackboardMessage(new BlackboardMessage(messageId, sender, timestamp, message, guild));
                        }

                        /**
                         * load the pending invites which are yet to be accepted or declined
                         */
                        statement = connection.prepareStatement(
                                "SELECT player, targetPlayer " +
                                        "from " + plugin.getGuildsConfig().getInvitesTable() +
                                        " where status = 0 and guildId = ?");
                        statement.setInt(1, guildId);
                        resultSet = statement.executeQuery();
                        resultSet.beforeFirst();
                        while (resultSet.next()) {
                            UUID targetPlayer = UUID.fromString(resultSet.getString("targetPlayer"));
                            guild.putPendingInvite(targetPlayer, new Invite(guild, targetPlayer,
                                    guild.getMember(UUID.fromString(resultSet.getString("player"))), null));
                        }

                        guilds.remove(guildId);
                        guilds.put(guildId, guild);

                        plugin.getAllianceManager().checkForReload(allianceId);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ConcurrentModificationException e) {
                    plugin.getLogger().log(Level.WARNING, "Error while loading guild " + guildId + "!", e);
                } finally {
                    DatabaseConnectionManager.close(connection);
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
     * returns the guild with the given id
     *
     * @param id the guild id
     * @return the matching guild
     */
    public Guild getGuild(int id) {
        return guilds.get(id);
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
     * returns a a guild member if the player is a guil
     *
     * @param uuid the player uuid
     * @return the GuildMember
     */
    public GuildMember getGuildMember(UUID uuid) {
        for (Guild guild : guilds.values()) {
            GuildMember member = guild.getMember(uuid);
            if (member != null) {
                return member;
            }
        }
        return null;
    }

    /**
     * sets the invite status to 1 (=Accepted)
     */
    public void acceptInvite(final Invite invite) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    if (connection != null) {
                        statement = connection.prepareStatement(
                                "UPDATE " + plugin.getGuildsConfig().getInvitesTable() + " SET status = 1 " +
                                        "WHERE player = ? " +
                                        "AND targetplayer = ? " +
                                        "AND guildId = ?");
                        statement.setString(1, invite.getSender().getUuid().toString());
                        statement.setString(2, invite.getTargetPlayer().toString());
                        statement.setInt(3, invite.getGuild().getId());
                        statement.executeUpdate();

                        addMember(invite.getGuild(), invite.getTargetPlayer());
                        plugin.getBungeeConnection().forceGuildSync(invite.getGuild().getId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the invite status to 2 (=Denied)
     */
    public void denyInvite(final Invite invite) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    if (connection != null) {
                        statement = connection.prepareStatement(
                                "UPDATE " + plugin.getGuildsConfig().getInvitesTable() + " SET status = 2 " +
                                        "WHERE player = ? " +
                                        "AND targetplayer = ? " +
                                        "AND guildId = ?");
                        statement.setString(1, invite.getSender().getUuid().toString());
                        statement.setString(2, invite.getTargetPlayer().toString());
                        statement.setInt(3, invite.getGuild().getId());
                        statement.executeUpdate();

                        plugin.getBungeeConnection().forceGuildSync(invite.getGuild().getId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * creates a new invite
     */
    public void addInvite(final Invite invite) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    if (connection != null) {

                        statement = connection.prepareStatement(
                                "INSERT IGNORE INTO "
                                        + plugin.getGuildsConfig().getPlayerTable() + " (playerName, uuid) " +
                                        "VALUES (?, ?) ");
                        statement.setString(1, invite.getTargetName());
                        statement.setString(2, invite.getTargetPlayer().toString());
                        statement.executeUpdate();
                        statement.close();

                        statement = connection.prepareStatement(
                                "INSERT INTO " + plugin.getGuildsConfig().getInvitesTable() + " (player, targetPlayer, guildId) " +
                                        "VALUES (?, ?, ?); ");
                        statement.setString(1, invite.getSender().getUuid().toString());
                        statement.setString(2, invite.getTargetPlayer().toString());
                        statement.setInt(3, invite.getGuild().getId());
                        statement.executeUpdate();

                        plugin.getBungeeConnection().forceGuildSync(invite.getGuild().getId());

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * creates a new guild
     *
     * @param founder the creator of the guild
     */
    public void create(final Guild guild, final OfflinePlayer founder) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();

                    PreparedStatement statement = connection
                            .prepareStatement("INSERT INTO " + plugin.getGuildsConfig().getGuildsTable() +
                                    " (guild, tag, color, balance, exp, level, founded) VALUES (?, ?, ?, ?, ?, 1, ?);");
                    statement.setString(1, guild.getName());
                    statement.setString(2, guild.getTag());
                    statement.setString(3, guild.getColor().name());
                    statement.setDouble(4, 0.0);
                    statement.setDouble(5, 0.0);
                    statement.setLong(6, new Date().getTime());
                    statement.executeUpdate();

                    statement = connection.prepareStatement("Select guildId from " + plugin.getGuildsConfig().getGuildsTable() + " where guild = ?");
                    statement.setString(1, guild.getName());
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.first();
                    guild.setId(resultSet.getInt("guildId"));

                    statement = connection.prepareStatement("INSERT IGNORE INTO "
                            + plugin.getGuildsConfig().getPlayerTable() + " (playerName, uuid, guildId, lastSeen, joined) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE guildId = ?;");
                    statement.setString(1, founder.getName());
                    statement.setString(2, founder.getUniqueId().toString());
                    statement.setInt(3, guild.getId());
                    statement.setLong(4, new Date().getTime());
                    statement.setLong(5, new Date().getTime());
                    statement.setInt(6, guild.getId());
                    statement.executeUpdate();

                    //add the founder rank
                    statement = connection.prepareStatement(
                            "INSERT INTO " + plugin.getGuildsConfig().getRanksTable() +
                                    "(rankName, guildId, canInvite, canKick, canPromote," +
                                    " canDisband, canUpgrade, canWithdrawMoney, canUseBlackboard," +
                                    " canDoDiplomacy, isBaseRank, isLeader) VALUES(" +
                                    " ?, ?, " +
                                    " true, true, true, true, true, true, true, true, false, true)"
                    );
                    statement.setString(1, plugin.getGuildsConfig().getText("standard.founderRank"));
                    statement.setInt(2, guild.getId());
                    statement.executeUpdate();

                    //add the base rank for all new members
                    statement = connection.prepareStatement(
                            "INSERT INTO " + plugin.getGuildsConfig().getRanksTable() +
                                    "(rankName, guildId, canInvite, canKick, canPromote," +
                                    " canDisband, canUpgrade, canWithdrawMoney, canUseBlackboard," +
                                    " canDoDiplomacy, isBaseRank, isLeader) VALUES(" +
                                    " ?, ?," +
                                    " false, false, false, false, false, false, false, false, true, false)"
                    );
                    statement.setString(1, plugin.getGuildsConfig().getText("standard.newbyRank"));
                    statement.setInt(2, guild.getId());
                    statement.executeUpdate();

                    statement = connection.prepareStatement(
                            "Select * from " + plugin.getGuildsConfig().getRanksTable() +
                                    " where rankName = ? and guildId = ?");
                    statement.setString(1, plugin.getGuildsConfig().getText("standard.founderRank"));
                    statement.setInt(2, guild.getId());
                    resultSet = statement.executeQuery();

                    resultSet.first();

                    Rank leaderRank =
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
                            );

                    //add the command sender as the new owner
                    setLeader(guild, founder);
                    setPlayerRank(guild, new GuildMember(founder), leaderRank);

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the players rank to the given
     *  @param guildMember the player
     * @param rank        the new rank
     */
    public void setPlayerRank(final Guild guild, final GuildMember guildMember, final Rank rank) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getPlayerTable() + " SET rankId = ? " +
                                    "where uuid = ?;");
                    statement.setInt(1, rank.getId());
                    statement.setString(2, guildMember.getUuid().toString());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the guilds leader
     *
     * @param guildMember the members targetPlayer
     */
    private void setLeader(final Guild guild, final OfflinePlayer guildMember) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    if (connection != null) {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + plugin.getGuildsConfig().getPlayerTable() +
                                        " SET " +
                                        " rankId = (Select rankId from " + plugin.getGuildsConfig().getRanksTable() +
                                        "  WHERE guildId = ? " +
                                        "  and isLeader = 1 )" +
                                        " where uuid = ?;"
                        );
                        statement.setInt(1, guild.getId());
                        statement.setString(2, guildMember.getUniqueId().toString());
                        statement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * deletes the guild
     */
    public void delete(final Guild guild) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getPlayerTable() + " SET guildId = NULL, rankId = NULL" +
                                    " where guildId = ? "
                    );
                    statement.setInt(1, guild.getId());
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement(
                            "DELETE FROM " + plugin.getGuildsConfig().getInvitesTable() + " WHERE guildId = ? ");
                    statement.setInt(1, guild.getId());
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement(
                            "DELETE FROM " + plugin.getGuildsConfig().getAllianceInviteTable() + " WHERE guildId = ? ");
                    statement.setInt(1, guild.getId());
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement(
                            "DELETE FROM " + plugin.getGuildsConfig().getBlackboardTable() + " WHERE guildId = ? ");
                    statement.setInt(1, guild.getId());
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement(
                            "DELETE FROM " + plugin.getGuildsConfig().getRanksTable() + " WHERE guildId = ? ");
                    statement.setInt(1, guild.getId());
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement(
                            "DELETE FROM " + plugin.getGuildsConfig().getGuildsTable() + " WHERE guildId = ? ");
                    statement.setInt(1, guild.getId());
                    statement.executeUpdate();
                    statement.close();

                    plugin.getBungeeConnection().forceGuildsSync();
                    plugin.getBungeeConnection().forceAlliancesSync();

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * adds a member to the given guild
     *
     * @param targetPlayer the target uuid
     */
    public void addMember(final Guild guild, final UUID targetPlayer) {
         plugin.runAsync(new Runnable() {
             public void run() {
                 Connection connection = null;
                 try {
                     connection = plugin.getDatabaseConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "UPDATE " + plugin.getGuildsConfig().getPlayerTable() +
                                     " SET " +
                                     "guildId = ?, " +
                                     "rankId = (Select rankId from " + plugin.getGuildsConfig().getRanksTable() +
                                     " where isBaseRank = 1 " +
                                     " and guildId = ?) " +
                                     "WHERE uuid = ? ");
                     statement.setInt(1, guild.getId());
                     statement.setInt(2, guild.getId());
                     statement.setString(3, targetPlayer.toString());
                     statement.executeUpdate();

                     plugin.getLogger().info("Player '" + targetPlayer + "' was added to " + guild.getName());
                     plugin.getBungeeConnection().forceGuildSync(guild.getId());
                     if (plugin.getAllianceManager().getAlliance(guild) != null) {
                         plugin.getBungeeConnection().forceAllianceSync(plugin.getAllianceManager().getAlliance(guild).getId());
                     }
                 } catch (SQLException e) {
                     e.printStackTrace();
                 } finally {
                     DatabaseConnectionManager.close(connection);
                 }
             }
         });
    }

    /**
     * removes a member from the guild
     *
     * @param guildMember the player uuid that shall be removed
     */
    public void removeMember(final Guild guild, final GuildMember guildMember) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getPlayerTable() +
                                    " SET guildId = NULL, rankId = null, prefix = null" +
                                    " WHERE uuid = ?");
                    statement.setString(1, guildMember.getUuid().toString());
                    statement.executeUpdate();

                    plugin.getLogger().info("Player '" + guildMember.getUuid().toString()
                            + "' was removed from " + guild.getName());

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                    if (plugin.getAllianceManager().getAlliance(guild) != null) {
                        plugin.getBungeeConnection().forceAllianceSync(plugin.getAllianceManager().getAlliance(guild).getId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the guilds balance to a given amount
     *
     * @param balance the new balance
     */
    public void setBalance(final Guild guild, final double balance) {
        guild.setBalance(balance);
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getGuildsTable() +
                                    " SET balance = ? where guildId = ? ");
                    statement.setDouble(1, balance);
                    statement.setInt(2, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the guild color
     *
     * @param color the new color
     */
    public void setColor(final Guild guild, final ChatColor color) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getGuildsTable() +
                                    " SET color = ? where guildId = ? ");
                    statement.setString(1, color.name());
                    statement.setInt(2, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the guild color
     *
     * @param newName the new name
     */
    public void setName(final Guild guild, final String newName) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getGuildsTable() +
                                    " SET guild = ? where guildId = ? ");
                    statement.setString(1, newName);
                    statement.setInt(2, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the guild color
     *
     * @param newTag the new name
     */
    public void setTag(final Guild guild, final String newTag) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getGuildsTable() +
                                    " SET tag = ? where guildId = ? ");
                    statement.setString(1, newTag);
                    statement.setInt(2, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the guilds balance to a given amount
     *
     * @param exp the new exp
     */
    public void setExp(final Guild guild, final int exp) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getGuildsTable() +
                                    " SET exp = ? where guildId = ? ");
                    statement.setInt(1, exp);
                    statement.setInt(2, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * increases the guild level by 1
     */
    public void upgrade(final Guild guild) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    GuildLevel nextLevel = getNextLevel(guild);
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getGuildsTable() +
                                    " SET level = ?, balance = ?, exp = ? where guildId = ? ");
                    statement.setInt(1, nextLevel.getLevel());
                    statement.setDouble(2, guild.getBalance() - nextLevel.getCost());
                    statement.setDouble(3, guild.getExp() - nextLevel.getExpCost());
                    statement.setInt(4, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the guild home
     *
     * @param location the location
     */
    public void setHome(final Guild guild, final Location location) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getGuildsTable() + " SET " +
                                    "guildHomeX = ?, " +
                                    "guildHomeY = ?, " +
                                    "guildHomeZ = ?, " +
                                    "guildHomeWorld = ?, " +
                                    "guildHomeServer = ? " +
                                    "where guildId = ?");
                    statement.setDouble(1, location.getX());
                    statement.setDouble(2, location.getY());
                    statement.setDouble(3, location.getZ());
                    statement.setString(4, location.getWorld().getName());
                    statement.setString(5, plugin.getServer().getIp() + ":"
                            + plugin.getServer().getPort());
                    statement.setInt(6, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * sets the message status to cleared, so it wont be shown again
     */
    public void clearMessages(final Guild guild) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getBlackboardTable() + " SET cleared = 1 where guildId = ?");
                    statement.setInt(1, guild.getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    public void setMemberPrefix(final GuildMember member, final String prefix) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getPlayerTable() +
                                    " SET prefix = ?" +
                                    " WHERE uuid = ?");
                    statement.setString(1, prefix);
                    statement.setString(2, member.getUuid().toString());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(plugin.getGuildManager().getGuild(member.getUuid()).getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    public void addBlackboardMessage(final Guild guild, final BlackboardMessage message) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT IGNORE INTO "
                                    + plugin.getGuildsConfig().getBlackboardTable() + " (player, guildId, message, timestamp) " +
                                    "VALUES (?, ?, ?, ?) ");
                    statement.setString(1, message.getSender().toString());
                    statement.setInt(2, guild.getId());
                    statement.setString(3, message.getMessage());
                    statement.setTimestamp(4, message.getTimestamp());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(guild.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * puts a blackboard into a nice Message
     *
     * @return a nice message
     */
    public String formatMessage(Guild guild, BlackboardMessage message) {
        String name = plugin.getPlayerName(message.getSender());
        if (name == null) {
            name = plugin.getGuildsConfig().getText("error.unknown");
        }
        return plugin.getGuildsConfig().getColoredText("info.guild.blackboard.message", guild.getColor())
                .replace("{0}", new SimpleDateFormat("dd.MM.yy HH:mm").format(message.getTimestamp()))
                .replace("{1}", name)
                .replace("{2}", message.getMessage());
    }


    /**
     * returns a level object of the next higher level
     *
     * @param guild the Guild to get the next level for
     * @return a level object of the next higher level
     */
    public GuildLevel getNextLevel(Guild guild) {
        return plugin.getGuildsConfig().getLevelData(guild.getCurrentLevel().getLevel() + 1);
    }

    /**
     * is there a level after the current level?
     *
     * @return true or false
     */
    public boolean hasNextLevel(Guild guild) {
        return plugin.getConfig().getConfigurationSection("level." + (guild.getCurrentLevel().getLevel() + 1)) != null;
    }

    /**
     * is the guild ready for upgrade
     *
     * @param upgrader the player who upgrades the guild (has to have the upgrade requirements in his inventory)
     * @return true or false
     */
    public boolean canBeUpgraded(Guild guild, Player upgrader) {
        if (!hasNextLevel(guild)) {
            return false;
        }
        GuildLevel nextLevel = getNextLevel(guild);
        if (nextLevel == null) {
            return false;
        }
        if (plugin.getGuildsConfig().requireMoneyForUpgrade() && guild.getBalance() < nextLevel.getCost()) {
            return false;
        }
        if (plugin.getGuildsConfig().requireExpForUpgrade() && guild.getExp() < nextLevel.getExpCost()) {
            return false;
        }
        if(plugin.getGuildsConfig().requireMoneyForUpgrade()) {
            for (Map.Entry<Material, Integer> entry : nextLevel.getMaterialRequirements().entrySet()) {
                if (GuildsUtil.countItems(upgrader.getInventory(), entry.getKey(), false) < entry.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * adds a new rank and stores it into the db
     */
    public void addRank(final Rank rank) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement;
                    if (rank.isBaseRank()) {
                        statement = connection.prepareStatement(
                                "UPDATE " + plugin.getGuildsConfig().getRanksTable() + " SET isBaseRank = 0 " +
                                        "where guildId = ?;");
                        statement.setInt(1, rank.getGuild().getId());
                        statement.executeUpdate();
                    }
                    statement = connection.prepareStatement(
                            "INSERT INTO " + plugin.getGuildsConfig().getRanksTable() +
                                    "(rankName, guildId, canInvite, canKick, canPromote, canDisband, " +
                                    "canUpgrade, canWithdrawMoney, canUseBlackboard, canDoDiplomacy, isBaseRank, isLeader) VALUES(" +
                                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    );
                    statement.setString(1, rank.getName());
                    statement.setInt(2, rank.getGuild().getId());
                    statement.setBoolean(3, rank.canInvite());
                    statement.setBoolean(4, rank.canKick());
                    statement.setBoolean(5, rank.canPromote());
                    statement.setBoolean(6, rank.canDisband());
                    statement.setBoolean(7, rank.canUpgrade());
                    statement.setBoolean(8, rank.canWithdrawMoney());
                    statement.setBoolean(9, rank.canUseBlackboard());
                    statement.setBoolean(10, rank.canDoDiplomacy());
                    statement.setBoolean(11, rank.isBaseRank());
                    statement.setBoolean(12, rank.isLeader());
                    statement.executeUpdate();
                    plugin.getBungeeConnection().forceGuildSync(rank.getGuild().getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * remove a rank from the db
     */
    public void deleteRank(final Rank rank) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    //give all players the newby rank who previously owned that rank that is now being deleted
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getPlayerTable() + " SET rankId = " +
                                    "(Select rankId from " + plugin.getGuildsConfig().getRanksTable() +
                                    " where guildId = ? and isBaseRank = 1) " +
                                    "WHERE rankId = ?;");
                    statement.setInt(1, rank.getGuild().getId());
                    statement.setInt(2, rank.getId());
                    statement.executeUpdate();

                    statement = connection.prepareStatement(
                            "DELETE FROM " + plugin.getGuildsConfig().getRanksTable() +
                                    " WHERE guildId = ?" +
                                    " AND rankName = ?");

                    statement.setInt(1, rank.getGuild().getId());
                    statement.setString(2, rank.getName());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(rank.getGuild().getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * Save a rank editing session
     * @param session The session to save
     */
    public void saveEditedRank(final EditRankSession session) {
        plugin.runAsync(new Runnable() {
            public void run() {
                Connection connection = null;
                try {
                    connection = plugin.getDatabaseConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "UPDATE " + plugin.getGuildsConfig().getRanksTable() + " SET " +
                                    "rankName = ?, " +
                                    "canInvite = ?, " +
                                    "canKick = ?, " +
                                    "canPromote = ?, " +
                                    "canDisband = ?, " +
                                    "canUpgrade = ?, " +
                                    "canWithdrawMoney = ?, " +
                                    "canUseBlackboard = ?, " +
                                    "canDoDiplomacy = ?, " +
                                    "isBaseRank = ? " +
                                    "WHERE rankId = ? " +
                                    "and guildId = ?");

                    statement.setString(1, session.getName());
                    statement.setBoolean(2, session.isCanInvite());
                    statement.setBoolean(3, session.isCanKick());
                    statement.setBoolean(4, session.isCanPromote());
                    statement.setBoolean(5, session.isCanDisband());
                    statement.setBoolean(6, session.isCanUpgrade());
                    statement.setBoolean(7, session.isCanWithdrawMoney());
                    statement.setBoolean(8, session.isCanUseBlackboard());
                    statement.setBoolean(9, session.isCanDoDiplomacy());
                    statement.setBoolean(10, session.isBaseRank());
                    statement.setInt(11, session.getRank().getId());
                    statement.setInt(12, session.getRank().getGuild().getId());
                    statement.executeUpdate();

                    plugin.getBungeeConnection().forceGuildSync(session.getRank().getGuild().getId());

                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseConnectionManager.close(connection);
                }
            }
        });
    }

    /**
     * returns the guild home location of the guild
     *
     * @return the guild home location
     */
    public Location getHome(Guild guild) {
        if (guild.getHomeWorld() != null && guild.getHomeServer() != null) {
            World world = plugin.getServer().getWorld(guild.getHomeWorld());
            if(world != null) {
                return new Location(world, guild.getHomeX(), guild.getHomeY(), guild.getHomeZ());
            }
        }
        return null;
    }
}
