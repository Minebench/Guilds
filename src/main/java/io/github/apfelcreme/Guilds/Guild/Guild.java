package io.github.apfelcreme.Guilds.Guild;

import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Double getGuildHomeX() {
        return guildHomeX;
    }

    public Double getGuildHomeY() {
        return guildHomeY;
    }

    public Double getGuildHomeZ() {
        return guildHomeZ;
    }

    public String getGuildHomeWorld() {
        return guildHomeWorld;
    }
}
