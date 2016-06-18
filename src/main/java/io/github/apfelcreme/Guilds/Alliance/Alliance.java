package io.github.apfelcreme.Guilds.Alliance;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Alliances
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
 * @author Lord36 aka Apfelcreme on 29.05.2015.
 */
public class Alliance implements Comparable<Alliance> {

    /**
     * the alliance id
     */
    private Integer id;

    /**
     * the alliance name
     */
    private String name;

    /**
     * the alliance tag
     */
    private String tag;

    /**
     * the chat color of this alliance
     */
    private ChatColor color;

    /**
     * the date this alliance was founded
     */
    private Long founded;

    /**
     * the list of guilds
     */
    private List<Guild> guilds;

    /**
     * the list of invites
     */
    private List<AllianceInvite> allianceInvites;

    public Alliance(Integer id, String name, String tag, ChatColor color, Long founded, List<Guild> guilds) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.color = color;
        this.founded = founded;
        this.guilds = guilds;
        allianceInvites = new ArrayList<AllianceInvite>();
    }

    public Alliance(String name, String tag, ChatColor color, Long founded, Guild founder) {
        this.name = name;
        this.tag = tag;
        this.color = color;
        this.founded = founded;
        guilds = new ArrayList<Guild>();
        guilds.add(founder);
    }

    /**
     * saves the alliance to the database
     */
    public void create() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "INSERT INTO " + GuildsConfig.getAllianceTable() + "(alliance, founded, tag, color) VALUES (?, ?, ?, ?)");
                        statement.setString(1, name);
                        statement.setLong(2, new Date().getTime());
                        statement.setString(3, tag);
                        statement.setString(4, color.name());
                        statement.executeUpdate();


                        statement = connection.prepareStatement(
                                "Select allianceId from " + GuildsConfig.getAllianceTable() + " where alliance = ?");
                        statement.setString(1, name);
                        ResultSet resultSet = statement.executeQuery();
                        resultSet.first();
                        id = resultSet.getInt("allianceId");

                        for (Guild guild : guilds) {
                            statement = connection.prepareStatement(
                                    "UPDATE " + GuildsConfig.getGuildsTable() + " SET allianceId = ? " +
                                            "where guildId = ?");
                            statement.setInt(1, id);
                            statement.setInt(2, guild.getId());
                            statement.executeUpdate();
                        }
                        connection.close();

                        BungeeConnection.forceGuildSync(guilds.get(0).getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * deletes the alliance
     */
    public void delete() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() + " SET allianceId = null " +
                                        "WHERE allianceId = ? ");
                        statement.setInt(1, getId());
                        statement.executeUpdate();

                        statement = connection.prepareStatement("DELETE FROM " + GuildsConfig.getAllianceInviteTable() +
                                " WHERE allianceId = ? ");
                        statement.setInt(1, getId());
                        statement.executeUpdate();

                        statement = connection.prepareStatement("DELETE FROM " + GuildsConfig.getAllianceTable() +
                                " WHERE allianceId = ? ");
                        statement.setInt(1, getId());
                        statement.executeUpdate();

                        BungeeConnection.forceGuildsSync();
                        BungeeConnection.forceAlliancesSync();

                        connection.close();
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
                                "UPDATE " + GuildsConfig.getAllianceTable() +
                                        " SET color = ? where allianceId = ? ");
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
     * removes a guild from the alliance
     *
     * @param guild the guild
     */
    public void removeMember(final Guild guild) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getGuildsTable() +
                                        " SET allianceId = null " +
                                        "where guildId = ? ");
                        statement.setInt(1, guild.getId());
                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        BungeeConnection.forceGuildSync(guild.getId());
                        BungeeConnection.forceAllianceSync(getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * sends an invite to the given guild
     *
     * @param guild the guild the invite is sent to
     */
    public void sendAllianceInviteTo(Guild guild) {
        new AllianceInvite(this, guild).save();
    }

    /**
     * adds an invite to the list
     *
     * @param invite the invite
     */
    public void putPendingAllianceInvite(AllianceInvite invite) {
        allianceInvites.add(invite);
    }


    /**
     * returns the list of invites
     *
     * @return the list of pending invites
     */
    public List<AllianceInvite> getPendingAllianceInvites() {
        return allianceInvites;
    }

    /**
     * returns the alliance id
     *
     * @return the alliance id
     */
    public Integer getId() {
        return id;
    }

    /**
     * returns the name of the alliance
     *
     * @return the alliance name
     */
    public String getName() {
        return GuildsUtil.replaceChatColors(name);
    }

    /**
     * returns the tag of the alliance
     *
     * @return the alliance tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * returns the color
     *
     * @return the color
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * returns the list of guilds
     *
     * @return the list of guilds
     */
    public List<Guild> getGuilds() {
        return guilds;
    }

    /**
     * returns a member of the alliance
     *
     * @param uuid the player uuid
     * @return the member object
     */
    public GuildMember getMember(UUID uuid) {
        for (Guild guild : guilds) {
            for (GuildMember member : guild.getMembers()) {
                if (member.getUuid().equals(uuid)) {
                    return member;
                }
            }
        }
        return null;
    }

    /**
     * checks if a guild is part of the alliance
     *
     * @param guild the guild
     * @return true or false
     */
    public boolean containsGuild(Guild guild) {
        for (Guild g : guilds) {
            if (g.getName().equalsIgnoreCase(guild.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Alliance{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", color=" + color +
                ", founded=" + founded +
                ", guilds=" + guilds +
                ", allianceInvites=" + allianceInvites +
                '}';
    }

    /**
     * compares the guild to another guild
     *
     * @param that the other guild
     * @return 1, 0 or -1
     */
    public int compareTo(Alliance that) {
        if (this.tag.compareTo(that.getTag()) < 0) {
            return -1;
        } else if (this.tag.compareTo(that.getTag()) > 0) {
            return 1;
        }
        if (this.name.compareTo(that.getName()) < 0) {
            return -1;
        } else if (this.name.compareTo(that.getName()) > 0) {
            return 1;
        }
        return 0;
    }
}
