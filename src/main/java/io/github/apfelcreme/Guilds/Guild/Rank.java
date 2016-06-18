package io.github.apfelcreme.Guilds.Guild;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
 * @author Lord36 aka Apfelcreme on 28.04.2015.
 */
public class Rank implements Comparable<Rank> {

    private Integer id;
    private String name;

    private Guild guild;

    private boolean canInvite;
    private boolean canKick;
    private boolean canPromote;
    private boolean canDisband;
    private boolean canUpgrade;
    private boolean canWithdrawMoney;
    private boolean canUseBlackboard;
    private boolean canDoDiplomacy;

    private int asdf = 0;
    private boolean isBaseRank;
    private boolean isLeader;

    public Rank(Integer id, String name, boolean canInvite, boolean canKick, boolean canPromote,
                boolean canDisband, boolean canUpgrade, boolean canWithdrawMoney, boolean canUseBlackboard,
                boolean canDoDiplomacy, boolean isBaseRank, boolean isLeader) {
        this.id = id;
        this.name = name;
        this.canInvite = canInvite;
        this.canKick = canKick;
        this.canPromote = canPromote;
        this.canDisband = canDisband;
        this.canUpgrade = canUpgrade;
        this.canWithdrawMoney = canWithdrawMoney;
        this.canUseBlackboard = canUseBlackboard;
        this.canDoDiplomacy = canDoDiplomacy;
        this.isBaseRank = isBaseRank;
        this.isLeader = isLeader;
    }

    /**
     * returns the rank id
     *
     * @return the rank id
     */
    public Integer getId() {
        return id;
    }

    /**
     * returns the ranks name
     *
     * @return the rank name
     */
    public String getName() {
        return GuildsUtil.replaceChatColors(name);
    }

    /**
     * sets the name
     *
     * @param name the rank name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the guild
     *
     * @param guild the guild
     */
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    /**
     * can a user with this rank invite others to join the guild?
     *
     * @return true or false
     */
    public boolean canInvite() {
        return canInvite;
    }

    /**
     * can a user with this rank kick other players from the guild?
     *
     * @return true or false
     */
    public boolean canKick() {
        return canKick;
    }

    /**
     * can a user with this rank promote other players?
     *
     * @return true or false
     */
    public boolean canPromote() {
        return canPromote;
    }

    /**
     * can a user with this rank disband the guild?
     *
     * @return true or false
     */
    public boolean canDisband() {
        return canDisband;
    }

    /**
     * can a user with this rank upgrade the guild?
     *
     * @return true or false
     */
    public boolean canUpgrade() {
        return canUpgrade;
    }

    /**
     * can a user withdraw money from the guilds bank?
     *
     * @return true or false
     */
    public boolean canWithdrawMoney() {
        return canWithdrawMoney;
    }

    /**
     * can a user with this rank use the blackboard?
     *
     * @return true or false
     */
    public boolean canUseBlackboard() {
        return canUseBlackboard;
    }

    /**
     * can a user with this rank do diplomacy?
     *
     * @return true or false
     */
    public boolean canDoDiplomacy() {
        return canDoDiplomacy;
    }

    /**
     * is this rank the baseRank that new players are assigned to?
     *
     * @return true or false
     */
    public boolean isBaseRank() {
        return isBaseRank;
    }

    /**
     * is this the leaders rank?
     *
     * @return true or false
     */
    public boolean isLeader() {
        return isLeader;
    }

    /**
     * returns the guild this rank belongs to
     *
     * @return the guild
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * creates a new rank and stores it into the db
     */
    public BukkitTask save() {
        BukkitTask task = Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(),
                new Runnable() {
                    public void run() {
                        Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                        if (connection != null) {
                            try {
                                PreparedStatement statement;
                                if (isBaseRank) {
                                    statement = connection.prepareStatement(
                                            "UPDATE " + GuildsConfig.getRanksTable() + " SET isBaseRank = 0 " +
                                                    "where guildId = ?;");
                                    statement.setInt(1, guild.getId());
                                    statement.executeUpdate();

                                }
                                statement = connection.prepareStatement(
                                        "INSERT INTO " + GuildsConfig.getRanksTable() +
                                                "(rankName, guildId, canInvite, canKick, canPromote, canDisband, " +
                                                "canUpgrade, canWithdrawMoney, canUseBlackboard, canDoDiplomacy, isBaseRank, isLeader) VALUES(" +
                                                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                                );
                                statement.setString(1, name);
                                statement.setInt(2, guild.getId());
                                statement.setBoolean(3, canInvite);
                                statement.setBoolean(4, canKick);
                                statement.setBoolean(5, canPromote);
                                statement.setBoolean(6, canDisband);
                                statement.setBoolean(7, canUpgrade);
                                statement.setBoolean(8, canWithdrawMoney);
                                statement.setBoolean(9, canUseBlackboard);
                                statement.setBoolean(10, canDoDiplomacy);
                                statement.setBoolean(11, isBaseRank);
                                statement.setBoolean(12, isLeader);
                                statement.executeUpdate();
                                statement.close();
                                connection.close();
                                BungeeConnection.forceGuildSync(guild.getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        return task;
    }

    /**
     * remove a rank from the db
     *
     * @param guild the guild the rank belongs to
     */
    public void delete(final Guild guild) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        //give all players the newby rank who previously owned that rank that is now being deleted
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getPlayerTable() + " SET rank = " +
                                        "(Select rankId from " + GuildsConfig.getRanksTable() +
                                        " where guildId = ? and isBaseRank = 1) " +
                                        "WHERE rankId = ?;");
                        statement.setInt(1, guild.getId());
                        statement.setInt(2, id);
                        statement.executeUpdate();

                        statement = connection.prepareStatement(
                                "DELETE FROM " + GuildsConfig.getRanksTable() +
                                        " WHERE guildId = ?" +
                                        " AND rankName = ?");

                        statement.setInt(1, guild.getId());
                        statement.setString(2, getName());
                        statement.executeUpdate();
                        statement.close();

                        connection.close();
                        BungeeConnection.forceGuildSync(guild.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * edits a new rank and stores it into the db
     *
     * @param rank             the old rank
     * @param guild            the guild
     * @param newName          the ranks new name
     * @param canInvite        can a user with this rank invite others?
     * @param canKick          can a user with this rank kick others?
     * @param canPromote       can a user with this rank promote players?
     * @param canDisband       can a user with this rank disband the guild?
     * @param canUpgrade       can a user with this rank upgrade the guild?
     * @param canWithdrawMoney can a user with this rank take money from the guild bank?
     * @param canUseBlackboard can a user with this rank write on the blackboard?
     * @param canDoDiplomacy   can a user with this rank do diplomacy?
     * @param isBaseRank       is this rank the base rank for new members?
     */
    public void editRank(final Rank rank, final Guild guild, final String newName,
                         final boolean canInvite, final boolean canKick, final boolean canPromote,
                         final boolean canDisband, final boolean canUpgrade, final boolean canWithdrawMoney,
                         final boolean canUseBlackboard, final boolean canDoDiplomacy, final boolean isBaseRank) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {

            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getRanksTable() + " SET " +
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

                        statement.setString(1, newName);
                        statement.setBoolean(2, canInvite);
                        statement.setBoolean(3, canKick);
                        statement.setBoolean(4, canPromote);
                        statement.setBoolean(5, canDisband);
                        statement.setBoolean(6, canUpgrade);
                        statement.setBoolean(7, canWithdrawMoney);
                        statement.setBoolean(8, canUseBlackboard);
                        statement.setBoolean(9, canDoDiplomacy);
                        statement.setBoolean(10, isBaseRank);
                        statement.setInt(11, rank.getId());
                        statement.setInt(12, guild.getId());
                        statement.executeUpdate();
                        statement.close();

                        BungeeConnection.forceGuildSync(guild.getId());

                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * returns the number of 'true's that are set in this rank
     *
     * @return the number of 'true's that are set in this rank
     */
    public int getNumberOfRankPermissions() {
        int ret = 0;
        if (canInvite) ret++;
        if (canKick) ret++;
        if (canPromote) ret++;
        if (canDisband) ret++;
        if (canUpgrade) ret++;
        if (canWithdrawMoney) ret++;
        if (canUseBlackboard) ret++;
        if (canDoDiplomacy) ret++;
        return ret;
    }


    /**
     * compareto
     *
     * @param that
     * @return
     */
    public int compareTo(Rank that) {
        if (this.getNumberOfRankPermissions() < that.getNumberOfRankPermissions()) {
            return -1;
        } else if (this.getNumberOfRankPermissions() > that.getNumberOfRankPermissions()) {
            return 1;
        }
        return 0;
    }
}

