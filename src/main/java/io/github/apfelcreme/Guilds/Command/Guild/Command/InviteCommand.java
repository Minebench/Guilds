package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class InviteCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(final CommandSender commandSender, final String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.guildInvite")) {
            if (strings.length >= 2) {
                UUID uuid = Guilds.getUUID(strings[1]);
                if (uuid != null) {
                    if (!uuid.equals(sender.getUniqueId())) {
                        Guild guild = Guilds.getInstance().getGuild(sender);
                        Guild targetGuild = Guilds.getInstance().getGuild(uuid);
                        if (guild != null) {
                            if (!guild.getPendingInvites().containsKey(uuid)) {
                                if (guild.getMember(sender.getUniqueId()).getRank().canInvite()) {
                                    if (guild.getMembers().size() < guild.getCurrentLevel().getPlayerLimit()) {
                                        if (targetGuild == null) {
                                            guild.sendInviteTo(guild.getMember(sender.getUniqueId()), uuid, strings[1]);
                                            Guilds.getInstance().getChat().sendMessage(sender,
                                                    GuildsConfig.getColoredText("info.guild.invite.invitedPlayer",
                                                            guild.getColor()).replace("{0}", strings[1]));
                                            Guilds.getInstance().getChat().sendBungeeMessage(
                                                    uuid, GuildsConfig.getColoredText("info.guild.invite.youGotInvited",
                                                            guild.getColor()).replace("{0}", guild.getName()));
                                            Guilds.getInstance().getChat().sendBungeeMessage(
                                                    uuid, GuildsConfig.getColoredText("info.guild.invite.accept",
                                                            guild.getColor()));
                                        } else {
                                            if (targetGuild.equals(guild)) {
                                                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                        .getText("error.isInThisGuildAlready")
                                                        .replace("{0}", strings[1])
                                                        .replace("{1}", guild.getName()));
                                            } else {
                                                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                        .getText("error.isInAGuildAlready")
                                                        .replace("{0}", strings[1]));
                                            }
                                        }
                                    } else {
                                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                .getText("error.guildFull"));
                                    }
                                } else {
                                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                            .getText("error.rank.noPermission")
                                            .replace("{0}", GuildsConfig.getText("info.guild.rank.info.invite")));

                                }
                            } else {
                                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.hasPendingInvite")
                                        .replace("{0}", strings[1]));
                            }
                        } else {
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noCurrentGuild"));
                        }
                    } else {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noSelfInvite"));
                    }
                } else {
                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.playerDoesntExist")
                            .replace("{0}", strings[1]));
                }
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.wrongUsage.invite"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
        }

    }

}
