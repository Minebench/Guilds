package io.github.apfelcreme.Guilds.Guild;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

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
 * @author Lord36 aka Apfelcreme on 25.04.2015.
 */
public class Invite {

    private Guild guild;
    private UUID targetPlayer;
    private GuildMember sender;
    private String targetName;

    public Invite(Guild guild, UUID targetPlayer, GuildMember sender, String targetName) {
        this.guild = guild;
        this.targetPlayer = targetPlayer;
        this.sender = sender;
        this.targetName = targetName;
    }

    /**
     * sets the invite status to 1 (=Accepted)
     */
    public void setAccepted() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                try {
                    Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                    if (connection != null) {
                        PreparedStatement statement;
                        statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getInvitesTable() + " SET status = 1 " +
                                        "WHERE player = ? " +
                                        "AND targetplayer = ? " +
                                        "AND guildId = ?");
                        statement.setString(1, sender.getUuid().toString());
                        statement.setString(2, targetPlayer.toString());
                        statement.setInt(3, guild.getId());
                        statement.executeUpdate();
                        statement.close();

                        guild.addMember(targetPlayer);

                        connection.close();
                        BungeeConnection.forceGuildSync(guild.getId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * sets the invite status to 2 (=Denied)
     */
    public void setDenied() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                try {
                    Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                    if (connection != null) {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + GuildsConfig.getInvitesTable() + " SET status = 2 " +
                                        "WHERE player = ? " +
                                        "AND targetplayer = ? " +
                                        "AND guildId = ?");
                        statement.setString(1, sender.getUuid().toString());
                        statement.setString(2, targetPlayer.toString());
                        statement.setInt(3, guild.getId());
                        statement.executeUpdate();
                        statement.close();

                        connection.close();
                        BungeeConnection.forceGuildSync(guild.getId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * creates a new invite
     */
    public void save() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                try {
                    Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                    if (connection != null) {

                        PreparedStatement statement;
                        statement = connection.prepareStatement(
                                "INSERT IGNORE INTO "
                                        + GuildsConfig.getPlayerTable() + " (playerName, uuid) " +
                                        "VALUES (?, ?) ");
                        statement.setString(1, targetName);
                        statement.setString(2, targetPlayer.toString());
                        statement.executeUpdate();
                        statement.close();

                        statement = connection.prepareStatement(
                                "INSERT INTO " + GuildsConfig.getInvitesTable() + " (player, targetPlayer, guildId) " +
                                        "VALUES (?, ?, ?); ");
                        statement.setString(1, sender.getUuid().toString());
                        statement.setString(2, targetPlayer.toString());
                        statement.setInt(3, getGuild().getId());
                        statement.executeUpdate();
                        statement.close();

                        connection.close();
                        BungeeConnection.forceGuildSync(getGuild().getId());

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
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
     * @param guild the guild to be set
     */
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    /**
     * returns the player the invite was sent to
     *
     * @return the player the invite was sent to
     */
    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    /**
     * returns the sender of the invite
     *
     * @return the sender of the invite
     */
    public GuildMember getSender() {
        return sender;
    }

    /**
     * sets the sender of the invite
     *
     * @param sender
     */
    public void setSender(GuildMember sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "guild=" + guild +
                ", targetPlayer=" + targetPlayer +
                ", sender=" + sender +
                '}';
    }
}
