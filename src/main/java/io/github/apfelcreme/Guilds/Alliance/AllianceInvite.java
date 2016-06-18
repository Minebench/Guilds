package io.github.apfelcreme.Guilds.Alliance;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Alliances
 * Copyright (C) 2015 Lord36 aka Apfelcreme
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
 *
 * @author Lord36 aka Apfelcreme on 30.05.2015.
 */
public class AllianceInvite {

    private Alliance alliance;
    private Guild guild;

    public AllianceInvite(Alliance alliance, Guild guild) {
        this.alliance = alliance;
        this.guild = guild;
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
                        statement= connection.prepareStatement(
                                "UPDATE "+ GuildsConfig.getAllianceInviteTable()+" SET status = 1 " +
                                        "WHERE guildId = ? " +
                                        "AND allianceId = ? ");
                        statement.setInt(1, guild.getId());
                        statement.setInt(2, alliance.getId());
                        statement.executeUpdate();
                        statement.close();

                        statement = connection.prepareStatement(
                                "UPDATE "+ GuildsConfig.getGuildsTable()+
                                        " SET allianceId = ? where guildId = ?;");
                        statement.setInt(1, alliance.getId());
                        statement.setInt(2, guild.getId());
                        statement.executeUpdate();
                        statement.close();

                        connection.close();
                        BungeeConnection.forceAllianceSync(alliance.getId());
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
                                "UPDATE "+ GuildsConfig.getAllianceInviteTable()+" SET status = 2 " +
                                        "WHERE guildId = ? " +
                                        "AND allianceId = ? ");
                        statement.setInt(1, guild.getId());
                        statement.setInt(2, alliance.getId());
                        statement.executeUpdate();
                        statement.close();

                        connection.close();
                        BungeeConnection.forceAllianceSync(alliance.getId());
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
     *
     */
    public void save() {
        Guilds.getInstance().getServer().getScheduler().runTaskAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                try {
                    Connection connection = DatabaseConnectionManager.getInstance().getConnection();
                    if (connection != null) {

                        PreparedStatement statement = connection.prepareStatement(
                                "INSERT INTO " + GuildsConfig.getAllianceInviteTable() + " (allianceId, guildId) " +
                                        "VALUES (?, ?); ");
                        statement.setInt(1, alliance.getId());
                        statement.setInt(2, guild.getId());
                        statement.executeUpdate();
                        statement.close();

                        connection.close();
                        BungeeConnection.forceAllianceSync(alliance.getId());
                        BungeeConnection.forceGuildSync(guild.getId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public String toString() {
        return "AllianceInvite{" +
                "alliance=" + alliance.getName() +
                ", guild=" + guild.getName() +
                '}';
    }

    /**
     * returns the alliance
     *
     * @return the alliance
     */
    public Alliance getAlliance() {
        return alliance;
    }

    /**
     * returns the guild guild
     *
     * @return the guild guild
     */
    public Guild getGuild() {
        return guild;
    }
}
