package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
 * @author Lord36 aka Apfelcreme on 22.05.2015.
 */
public class LookupCommand extends SubCommand {

    public LookupCommand(Guilds plugin) {
        super(plugin);
    }

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.lookup")) {
            if (strings.length >= 2) {
                UUID uuid = plugin.getUUID(strings[1]);
                if (uuid != null) {
                    Guild guild = plugin.getGuildManager().getGuild(uuid);
                    if (guild != null) {
                        GuildMember guildMember = plugin.getGuildManager().getGuildMember(uuid);
                        if (guildMember != null) {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getColoredText("info.guild.lookup.head", guild.getColor())
                                    .replace("{0}", strings[1]));
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getColoredText("info.guild.lookup.guild", guild.getColor())
                                    .replace("{0}", guild.getName()));
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getColoredText("info.guild.lookup.prefix", guild.getColor())
                                    .replace("{0}", guildMember.getPrefix() != null ? guildMember.getPrefix() : ""));
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getColoredText("info.guild.lookup.joined", guild.getColor())
                                    .replace("{0}",
                                            new SimpleDateFormat("dd.MM.YY HH:mm").format(
                                                    new Date(guildMember.getJoined()))));
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getColoredText("info.guild.lookup.lastSeen", guild.getColor())
                                    .replace("{0}",
                                            new SimpleDateFormat("dd.MM.YY HH:mm").format(
                                                    new Date(guildMember.getLastSeen()))));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getColoredText("info.guild.lookup.head", ChatColor.DARK_GREEN)
                                .replace("{0}", strings[1])
                                .replace("&gc", "")
                                .replace("&gcD", ""));
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getColoredText("info.guild.lookup.guild", ChatColor.DARK_GREEN)
                                .replace("{0}", "keine")
                                .replace("&gc", "")
                                .replace("&gcD", ""));
                    }

                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.playerDoesntExist")
                            .replace("{0}", strings[1]));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.lookup"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
