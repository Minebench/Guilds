package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.KickRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.Manager.RequestController;
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
 * @author Lord36 aka Apfelcreme on 26.04.2015.
 */
public class KickCommand extends SubCommand {

    public KickCommand(Guilds plugin) {
        super(plugin);
    }

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(final CommandSender commandSender, final String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.kickFromGuild")) {
            if (strings.length >= 2) {
                UUID uuid = plugin.getUUID(strings[1]);
                if (uuid != null) {
                    if (uuid != sender.getUniqueId()) {
                        Guild guild = plugin.getGuildManager().getGuild(sender);
                        Guild targetGuild = plugin.getGuildManager().getGuild(uuid);
                        if (guild != null) {
                            if (targetGuild != null && guild.equals(targetGuild)) {
                                if (guild.getMember(sender.getUniqueId()).getRank().canKick()) {
                                    plugin.getRequestController().addRequest(
                                            new KickRequest(plugin, sender, guild.getMember(uuid), guild));
                                } else {
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getText("error.rank.noPermission").replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.kick")));
                                }
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.notInThisGuild")
                                        .replace("{0}", strings[1]).replace("{1}", guild.getName()));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noSelfKick"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.playerDoesntExist")
                            .replace("{0}", strings[1]));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.kick"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
