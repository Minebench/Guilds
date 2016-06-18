package io.github.apfelcreme.Guilds.Guild;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

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
 * @author Lord36 aka Apfelcreme on 19.05.2015.
 */
public class GuildMember implements Comparable<GuildMember> {

    private UUID uuid;
    private String name;
    private Long lastSeen;
    private Long joined;
    private String prefix;
    private Rank rank;

    private Guild guild;

    private OfflinePlayer player;

    private Boolean isOnline = false;

    public GuildMember(UUID uuid, String name, Long lastSeen, Long joined, String prefix, Rank rank) {
        this.uuid = uuid;
        this.name = name;
        this.lastSeen = lastSeen;
        this.joined = joined;
        this.prefix = prefix;
        this.rank = rank;
        player = Guilds.getInstance().getServer().getOfflinePlayer(uuid);
        this.isOnline = player.isOnline();
    }

    public GuildMember(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.prefix = null;
        this.rank = null;
        this.player = player.getPlayer();
        this.isOnline = player.isOnline();
    }

    /**
     * sends a message to the guild member
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        Guilds.getInstance().getChat().sendMessage(this, message);
    }

    /**
     * returns the uuid
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * returns the player name
     *
     * @return the player name
     */
    public String getName() {
        return name;
    }

    /**
     * returns the prefix
     *
     * @return the prefix
     */
    public String getPrefix() {
        return GuildsUtil.replaceChatColors(prefix);
    }

    /**
     * returns the date the player joined
     *
     * @return the date the player joined as a unix timestamp
     */
    public Long getJoined() {
        return joined;
    }

    /**
     * returns the date the player was seen last
     *
     * @return the date the player was seen last as a unix timestamp
     */
    public Long getLastSeen() {
        return lastSeen;
    }

    /**
     * returns the players rank
     *
     * @return the players rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * returns a matching player Object
     *
     * @return the player
     */
    public OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * returns the guild
     *
     * @return the guild
     */
    public Guild getGuild() {
        return guild;
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
     * checks if the player is online
     *
     * @return true or false
     */
    public Boolean isOnline() {
        return isOnline;
    }

    /**
     * sets the online status. This is bungee wide as on every bungee login event a message is sent to the servers
     *
     * @param online true or false
     */
    public void setOnline(Boolean online) {
        isOnline = online;
    }

    /**
     * sets a players prefix
     *
     * @param prefix the new prefix
     */
    public void setPrefix(final String prefix) {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {

            public void run() {
                Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                if (connection != null) {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getPlayerTable() +
                                        " SET prefix = ?" +
                                        " WHERE uuid = ?");
                        statement.setString(1, prefix);
                        statement.setString(2, getUuid().toString());
                        statement.executeUpdate();
                        statement.close();

                        BungeeConnection.forceGuildSync(Guilds.getInstance().getGuild(uuid).getId());

                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * compareto
     *
     * @param that
     * @return
     */
    public int compareTo(GuildMember that) {
        if (this.rank.compareTo(that.getRank()) < 0) {
            return -1;
        } else if (this.rank.compareTo(that.getRank()) > 0) {
            return 1;
        }

        if (this.name.compareTo(that.name) < 0) {
            return -1;
        } else if (this.name.compareTo(that.name) > 0) {
            return 1;
        }
        return 0;
    }
}
