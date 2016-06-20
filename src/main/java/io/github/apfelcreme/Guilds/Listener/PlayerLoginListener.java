package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Alliance.AllianceInvite;
import io.github.apfelcreme.Guilds.Guild.BlackboardMessage;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Invite;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

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
 * @author Lord36 aka Apfelcreme on 14.05.2015.
 */
public class PlayerLoginListener implements Listener {

    private Guilds plugin;

    public PlayerLoginListener(Guilds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent e) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin,
                new Runnable() {
                    public void run() {
                        // check for invites
                        Invite invite = plugin.getGuildManager().getInvite(e.getPlayer().getUniqueId());
                        if (invite != null) {
                            plugin.getChat().sendMessage(e.getPlayer(),
                                    plugin.getGuildsConfig().getColoredText(
                                            "info.guild.invite.youGotInvited", invite.getGuild().getColor()
                                    ).replace("{0}", invite.getGuild().getName()));
                            plugin.getChat().sendMessage(e.getPlayer(),
                                    plugin.getGuildsConfig().getColoredText(
                                            "info.guild.invite.accept", invite.getGuild().getColor()));
                        }
                        //check for blackboard messages & alliance invites
                        Guild guild = plugin.getGuildManager().getGuild(e.getPlayer());
                        if (guild != null) {
                            AllianceInvite allianceInvite = plugin.getAllianceManager().getInvite(guild);
                            if (allianceInvite != null) {
                                if (guild.getMember(e.getPlayer().getUniqueId()) != null
                                        && guild.getMember(e.getPlayer().getUniqueId()).getRank().canDoDiplomacy()) {
                                    plugin.getChat().sendMessage(e.getPlayer(),
                                            plugin.getGuildsConfig().getText("info.chat.allianceGotInvited")
                                            .replace("{0}", allianceInvite.getAlliance().getName()));
                                    plugin.getChat().sendMessage(e.getPlayer(),
                                            plugin.getGuildsConfig().getText("info.chat.allianceAccept"));
                                }
                            }

                            plugin.getChat().sendMessage(e.getPlayer(),
                                    plugin.getGuildsConfig().getColoredText("info.guild.blackboard.head", guild.getColor()));
                            for (BlackboardMessage message : guild.getBlackboardMessages()) {
                                plugin.getChat().sendMessage(e.getPlayer(), plugin.getGuildManager().formatMessage(guild, message));
                            }
                        }

                        //add the user if he wasn't there yet
                        try {
                            Connection connection = plugin.getDatabaseConnection();

                            PreparedStatement statement = connection.prepareStatement("INSERT INTO "
                                    + plugin.getGuildsConfig().getPlayerTable() + " (playerName, uuid, joined, lastSeen) " +
                                    "VALUES (?, ?, ?, ?) " +
                                    "ON DUPLICATE KEY UPDATE playerName = ?, lastSeen = ?;");
                            statement.setString(1, e.getPlayer().getName());
                            statement.setString(2, e.getPlayer().getUniqueId().toString());
                            statement.setLong(3, new Date().getTime());
                            statement.setLong(4, new Date().getTime());
                            statement.setString(5, e.getPlayer().getName());
                            statement.setLong(6, new Date().getTime());
                            statement.executeUpdate();
                            statement.close();
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }, 20L);


    }

}
