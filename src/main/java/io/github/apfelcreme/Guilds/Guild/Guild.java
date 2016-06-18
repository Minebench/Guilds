package io.github.apfelcreme.Guilds.Guild;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
public class Guild implements Comparable<Guild> {

    /**
     * the guild id
     */
    private Integer id;

    /**
     * the guild name
     */
    private String name;

    /**
     * the guild tag
     */
    private String tag;

    /**
     * the list of members
     */
    private List<GuildMember> members;

    /**
     * the color used in the guild chat
     */
    private ChatColor color;

    /**
     * the amount of money in the guild bank
     */
    private Double balance;

    /**
     * the amount of exp
     */
    private Integer exp;

    /**
     * a list of all ranks that are available for this guild
     * also contains the ranks which no one is currently assigned to
     */
    private List<Rank> ranks;

    /**
     * the guild level
     */
    private GuildLevel currentLevel;

    /**
     * the date the guild was founded
     */
    private Date founded;

    /**
     * the x coordinate of the guild home
     */
    private Double guildHomeX;

    /**
     * the y coordinate of the guild home
     */
    private Double guildHomeY;

    /**
     * the u coordinate of the guild home
     */
    private Double guildHomeZ;

    /**
     * the world of the guild home
     */
    private String guildHomeWorld;

    /**
     * the server of the guild home (ip adress with port)
     */
    private String guildHomeServer;

    /**
     * the list of invites that haven't been answered yet
     */
    private Map<UUID, Invite> pendingInvites;

    /**
     * the list of the latest blackboard messages
     */
    private List<BlackboardMessage> blackboardMessages;

    public Guild(Integer id, String name, String tag, ChatColor color, Double balance) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.color = color;
        this.balance = balance;
        members = new ArrayList<GuildMember>();
        ranks = new LinkedList<Rank>();
        pendingInvites = new HashMap<UUID, Invite>();
        blackboardMessages = new ArrayList<BlackboardMessage>();
    }

    public Guild(Integer id, String name, String tag, ChatColor color, Double balance, Integer exp, List<GuildMember> members, List<Rank> ranks,
                 GuildLevel currentLevel, Date founded, Double guildHomeX, Double guildHomeY, Double guildHomeZ, String guildHomeWorld, String guildHomeServer) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.color = color;
        this.members = members;
        this.balance = balance;
        this.exp = exp;
        this.ranks = ranks;
        this.currentLevel = currentLevel;
        this.founded = founded;
        this.guildHomeX = guildHomeX;
        this.guildHomeY = guildHomeY;
        this.guildHomeZ = guildHomeZ;
        this.guildHomeWorld = guildHomeWorld;
        this.guildHomeServer = guildHomeServer;
        blackboardMessages = new ArrayList<BlackboardMessage>();
        pendingInvites = new HashMap<UUID, Invite>();
    }

    /**
     * sends a new invite to the targetPlayer
     *
     * @param sender       the sender
     * @param targetPlayer the receiver
     */
    public void sendInviteTo(GuildMember sender, UUID targetPlayer, String targetPlayerName) {
        new Invite(this, targetPlayer, getMember(sender.getPlayer().getUniqueId()), targetPlayerName).save();
    }

    /**
     * returns the guild id
     *
     * @return the guild id
     */
    public Integer getId() {
        return id;
    }

    /**
     * returns the rank name
     *
     * @return the rank name
     */
    public String getName() {
        return GuildsUtil.replaceChatColors(name);
    }

    /**
     * returns a member list
     *
     * @return the list of members
     */
    public List<GuildMember> getMembers() {
        return members;
    }

    /**
     * returns the clan tag
     *
     * @return the clan tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * returns the date the clan was founded
     *
     * @return the date the clan was founded
     */
    public Date getFounded() {
        return founded;
    }

    /**
     * returns the player with the given uuid
     *
     * @param uuid the uuid
     * @return the matching player object
     */
    public GuildMember getMember(UUID uuid) {
        for (GuildMember member : members) {
            if (member.getUuid().equals(uuid)) {
                return member;
            }
        }
        return null;
    }

    /**
     * returns the guild home location of the guild
     *
     * @return the guild home location
     */
    public Location getGuildHome() {
        if (guildHomeX != null && guildHomeY != null && guildHomeZ != null
                && guildHomeWorld != null && guildHomeServer != null) {
            return new Location(Guilds.getInstance().getServer()
                    .getWorld(guildHomeWorld), guildHomeX, guildHomeY, guildHomeZ);
        } else {
            return null;
        }
    }

    /**
     * returns the server name of the guild home
     *
     * @return the server name of the guild home
     */
    public String getGuildHomeServer() {
        return guildHomeServer;
    }

    /**
     * returns the rank with the given name
     *
     * @param name the rank name
     * @return the rank with the given name or null
     */
    public Rank getRank(String name) {
        for (Rank rank : ranks) {
            if (GuildsUtil.strip(rank.getName()).equalsIgnoreCase(GuildsUtil.strip(name))) {
                return rank;
            }
        }
        return null;
    }

    /**
     * is the guild ready for upgrade
     *
     * @param upgrader the player who upgrades the guild (has to have the upgrade requirements in his inventory)
     * @return true or false
     */
    public boolean canBeUpgraded(Player upgrader) {
        if (!currentLevel.hasNextLevel()) {
            return false;
        }
        if (balance < currentLevel.nextLevel().getCost()) {
            return false;
        }
        if (exp < currentLevel.nextLevel().getExpCost()) {
            return false;
        }
        for (Map.Entry<Material, Integer> entry : currentLevel.nextLevel().getMaterialRequirements().entrySet()) {
            if (GuildsUtil.countItems(upgrader.getInventory(), entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns the chatColor
     *
     * @return the chatColor
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * returns the clan balance
     *
     * @return the clan balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * returns the clan exp
     *
     * @return the clan exp
     */
    public Integer getExp() {
        return exp;
    }

    /**
     * retuzrn the list of ranks
     *
     * @return all ranks
     */
    public List<Rank> getRanks() {
        return ranks;
    }

    /**
     * returns the current level of the guild
     *
     * @return the current level of the guild
     */
    public GuildLevel getCurrentLevel() {
        return currentLevel;
    }

    /**
     * returns the list of pending invites sent by this guild
     *
     * @return the list of pending invites sent by this guild
     */
    public Map<UUID, Invite> getPendingInvites() {
        return pendingInvites;
    }

    /**
     * adds an invite
     *
     * @param targetPlayer the target player
     * @param invite       the invite
     */
    public void putPendingInvite(UUID targetPlayer, Invite invite) {
        pendingInvites.put(targetPlayer, invite);
    }

    /**
     * returns a list of the latest blackboard messages
     *
     * @return a list of messages
     */
    public List<BlackboardMessage> getBlackboardMessages() {
        return blackboardMessages;
    }

    /**
     * adds a blackboardMessage
     *
     * @param blackboardMessage a BlackboardMessage object
     */
    public void addBlackboardMessage(BlackboardMessage blackboardMessage) {
        blackboardMessages.add(blackboardMessage);
    }

    /**
     * creates a new guild
     *
     * @param founder the creator of the guild
     */
    public void create(final OfflinePlayer founder) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(),
                new Runnable() {
                    public void run() {
                        Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                        if (connection != null) {
                            try {
                                PreparedStatement statement;

                                statement = connection
                                        .prepareStatement("INSERT INTO " + GuildsConfig.getGuildsTable() +
                                                " (guild, tag, color, balance, exp, level, founded) VALUES (?, ?, ?, ?, ?, 1, ?);");
                                statement.setString(1, getName());
                                statement.setString(2, getTag());
                                statement.setString(3, getColor().name());
                                statement.setDouble(4, 0.0);
                                statement.setDouble(5, 0.0);
                                statement.setLong(6, new Date().getTime());
                                statement.executeUpdate();
                                statement.close();

                                statement = connection.prepareStatement("Select guildId from " + GuildsConfig.getGuildsTable() + " where guild = ?");
                                statement.setString(1, getName());
                                ResultSet resultSet = statement.executeQuery();
                                resultSet.first();
                                id = resultSet.getInt("guildId");

                                statement = connection.prepareStatement("INSERT IGNORE INTO "
                                        + GuildsConfig.getPlayerTable() + " (playerName, uuid, guildId, lastSeen, joined) " +
                                        "VALUES (?, ?, ?, ?, ?) " +
                                        "ON DUPLICATE KEY UPDATE guildId = ?;");
                                statement.setString(1, founder.getName());
                                statement.setString(2, founder.getUniqueId().toString());
                                statement.setInt(3, id);
                                statement.setLong(4, new Date().getTime());
                                statement.setLong(5, new Date().getTime());
                                statement.setInt(6, id);
                                statement.executeUpdate();
                                statement.close();


                                //add the founder rank
                                statement = connection.prepareStatement(
                                        "INSERT INTO " + GuildsConfig.getRanksTable() +
                                                "(rankName, guildId, canInvite, canKick, canPromote," +
                                                " canDisband, canUpgrade, canWithdrawMoney, canUseBlackboard," +
                                                " canDoDiplomacy, isBaseRank, isLeader) VALUES(" +
                                                " ?, ?, " +
                                                " true, true, true, true, true, true, true, true, false, true)"
                                );
                                statement.setString(1, GuildsConfig.getText("standard.founderRank"));
                                statement.setInt(2, id);
                                statement.executeUpdate();
                                statement.close();

                                //add the base rank for all new members
                                statement = connection.prepareStatement(
                                        "INSERT INTO " + GuildsConfig.getRanksTable() +
                                                "(rankName, guildId, canInvite, canKick, canPromote," +
                                                " canDisband, canUpgrade, canWithdrawMoney, canUseBlackboard," +
                                                " canDoDiplomacy, isBaseRank, isLeader) VALUES(" +
                                                " ?, ?," +
                                                " false, false, false, false, false, false, false, false, true, false)"
                                );
                                statement.setString(1, GuildsConfig.getText("standard.newbyRank"));
                                statement.setInt(2, id);
                                statement.executeUpdate();
                                statement.close();

                                statement = connection.prepareStatement(
                                        "Select * from " + GuildsConfig.getRanksTable() +
                                                " where rankName = ? and guildId = ?");
                                statement.setString(1, GuildsConfig.getText("standard.founderRank"));
                                statement.setInt(2, id);
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

                                statement.close();

                                connection.close();

                                //add the command sender as the new owner
                                setLeader(founder);
                                setPlayerRank(founder, leaderRank);

                                BungeeConnection.forceGuildSync(getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * sets the players rank to the given
     *
     * @param guildMember the player
     * @param rank        the new rank
     */
    public void setPlayerRank(final OfflinePlayer guildMember, final Rank rank) {

        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getPlayerTable() + " SET rankId = ? " +
                                        "where uuid = ?;");
                        statement.setInt(1, rank.getId());
                        statement.setString(2, guildMember.getUniqueId().toString());
                        statement.executeUpdate();
                        statement.close();

                        BungeeConnection.forceGuildSync(getId());

                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * sets the guilds leader
     *
     * @param guildMember the members targetPlayer
     */
    private void setLeader(final OfflinePlayer guildMember) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(
                Guilds.getInstance(), new Runnable() {
                    public void run() {
                        try {
                            Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                            if (connection != null) {
                                PreparedStatement statement = connection.prepareStatement(
                                        "UPDATE " + GuildsConfig.getPlayerTable() +
                                                " SET " +
                                                " rankId = (Select rankId from " + GuildsConfig.getRanksTable() +
                                                "  WHERE guildId = ? " +
                                                "  and isLeader = 1 )" +
                                                " where uuid = ?;"
                                );
                                statement.setInt(1, id);
                                statement.setString(2, guildMember.getUniqueId().toString());
                                statement.executeUpdate();
                                statement.close();

                                connection.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * deletes the guild
     */
    public void delete() {
        final Guild thisGuild = this;
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(),
                new Runnable() {
                    public void run() {
                        Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                        if (connection != null) {
                            try {
                                PreparedStatement statement = connection.prepareStatement(
                                        "UPDATE " + GuildsConfig.getPlayerTable() + " SET guildId = NULL, rankId = NULL" +
                                                " where guildId = ? "
                                );
                                statement.setInt(1, id);
                                statement.executeUpdate();
                                statement.close();

                                statement = connection.prepareStatement(
                                        "DELETE FROM " + GuildsConfig.getInvitesTable() + " WHERE guildId = ? ");
                                statement.setInt(1, id);
                                statement.executeUpdate();
                                statement.close();

                                statement = connection.prepareStatement(
                                        "DELETE FROM " + GuildsConfig.getAllianceInviteTable() + " WHERE guildId = ? ");
                                statement.setInt(1, id);
                                statement.executeUpdate();
                                statement.close();

                                statement = connection.prepareStatement(
                                        "DELETE FROM " + GuildsConfig.getBlackboardTable() + " WHERE guildId = ? ");
                                statement.setInt(1, id);
                                statement.executeUpdate();
                                statement.close();

                                statement = connection.prepareStatement(
                                        "DELETE FROM " + GuildsConfig.getRanksTable() + " WHERE guildId = ? ");
                                statement.setInt(1, id);
                                statement.executeUpdate();
                                statement.close();

                                statement = connection.prepareStatement(
                                        "DELETE FROM " + GuildsConfig.getGuildsTable() + " WHERE guildId = ? ");
                                statement.setInt(1, id);
                                statement.executeUpdate();
                                statement.close();

                                connection.close();

                                BungeeConnection.forceGuildsSync();
                                BungeeConnection.forceAlliancesSync();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * adds a member to the given guild
     *
     * @param targetPlayer the target uuid
     */
    public void addMember(final UUID targetPlayer) {
        final Guild thisGuild = this;
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(
                Guilds.getInstance(), new Runnable() {
                    public void run() {
                        try {
                            Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                            if (connection != null) {
                                PreparedStatement statement = connection.prepareStatement(
                                        "UPDATE " + GuildsConfig.getPlayerTable() +
                                                " SET " +
                                                "guildId = ?, " +
                                                "rankId = (Select rankId from " + GuildsConfig.getRanksTable() +
                                                " where isBaseRank = 1 " +
                                                " and guildId = ?) " +
                                                "WHERE uuid = ? ");
                                statement.setInt(1, id);
                                statement.setInt(2, id);
                                statement.setString(3, targetPlayer.toString());
                                statement.executeUpdate();
                                statement.close();

                                connection.close();
                                Guilds.getInstance().getLogger().info("Player '" + targetPlayer + "' was added to "
                                        + getName());
                                BungeeConnection.forceGuildSync(getId());
                                if (Guilds.getInstance().getAlliance(thisGuild) != null) {
                                    BungeeConnection.forceAllianceSync(Guilds.getInstance().getAlliance(thisGuild).getId());
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * removes a member from the guild
     *
     * @param guildMember the player uuid that shall be removed
     */
    public void removeMember(final GuildMember guildMember) {
        final Guild thisGuild = this;
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(),
                new Runnable() {
                    public void run() {
                        try {
                            Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                            if (connection != null) {
                                PreparedStatement statement = connection.prepareStatement(
                                        "UPDATE " + GuildsConfig.getPlayerTable() +
                                                " SET guildId = NULL, rankId = null, prefix = null" +
                                                " WHERE uuid = ?");
                                statement.setString(1, guildMember.getUuid().toString());
                                statement.executeUpdate();
                                statement.close();

                                connection.close();

                                Guilds.getInstance().getLogger().info("Player '" + guildMember.getUuid().toString()
                                        + "' was removed from " + getName());

                                BungeeConnection.forceGuildSync(getId());
                                if (Guilds.getInstance().getAlliance(thisGuild) != null) {
                                    BungeeConnection.forceAllianceSync(Guilds.getInstance().getAlliance(thisGuild).getId());
                                }

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * sets the guilds balance to a given amount
     *
     * @param balance the new balance
     */
    public void setBalance(final double balance) {

        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() +
                                        " SET balance = ? where guildId = ? ");
                        statement.setDouble(1, balance);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();

                        BungeeConnection.forceGuildSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * sets the guild color
     *
     * @param color the new color
     */
    public void setColor(final ChatColor color) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() +
                                        " SET color = ? where guildId = ? ");
                        statement.setString(1, color.name());
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        BungeeConnection.forceGuildSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * sets the guild color
     *
     * @param newName the new name
     */
    public void setName(final String newName) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() +
                                        " SET guild = ? where guildId = ? ");
                        statement.setString(1, newName);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        BungeeConnection.forceGuildSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * sets the guild color
     *
     * @param newTag the new name
     */
    public void setTag(final String newTag) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() +
                                        " SET tag = ? where guildId = ? ");
                        statement.setString(1, newTag);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        BungeeConnection.forceGuildSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * sets the guilds balance to a given amount
     *
     * @param exp the new exp
     */
    public void setExp(final int exp) {

        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() +
                                        " SET exp = ? where guildId = ? ");
                        statement.setInt(1, exp);
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();

                        BungeeConnection.forceGuildSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * increases the guild level by 1
     */
    public void upgrade() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() +
                                        " SET level = ?, balance = ?, exp = ? where guildId = ? ");
                        statement.setInt(1, currentLevel.nextLevel().getLevel());
                        statement.setDouble(2, getBalance() - currentLevel.nextLevel().getCost());
                        statement.setDouble(3, getExp() - currentLevel.nextLevel().getExpCost());
                        statement.setInt(4, id);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();

                        BungeeConnection.forceGuildSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * sets the guild home
     *
     * @param location the location
     */
    public void setHome(final Location location) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() + " SET " +
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
                        statement.setString(5, Guilds.getInstance().getServer().getIp() + ":"
                                + Guilds.getInstance().getServer().getPort());
                        statement.setInt(6, id);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();

                        BungeeConnection.forceGuildSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * sets the message status to cleared, so it wont be shown again
     */
    public void clearMessages() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                try {
                    Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                    if (connection != null) {

                        PreparedStatement statement;
                        statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getBlackboardTable() + " SET cleared = 1 where guildId = ?");
                        statement.setInt(1, id);
                        statement.executeUpdate();
                        statement.close();

                        connection.close();
                        BungeeConnection.forceGuildSync(getId());

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * compares the guild to another guild
     *
     * @param that the other guild
     * @return 1, 0 or -1
     */
    public int compareTo(Guild that) {
        if (this.currentLevel.getLevel().compareTo(that.getCurrentLevel().getLevel()) < 0) {
            return -1;
        } else if (this.currentLevel.getLevel().compareTo(that.getCurrentLevel().getLevel()) > 0) {
            return 1;
        }

        if (GuildsUtil.strip(this.tag).compareTo(GuildsUtil.strip(that.tag)) < 0) {
            return 1;
        } else if (GuildsUtil.strip(this.tag).compareTo(GuildsUtil.strip(that.tag)) > 0) {
            return -1;
        }

        if (this.name.compareTo(that.getName()) < 0) {
            return 1;
        } else if (this.name.compareTo(that.getName()) > 0) {
            return -1;
        }
        return 0;
    }
}
