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

    public boolean equals(Object o) {
        if (!(o instanceof Rank)) {
            return false;
        }

        return ((Rank) o).getId().equals(this.getId());
    }
}

