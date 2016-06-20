package io.github.apfelcreme.Guilds.Command.Admin.Command;

import io.github.apfelcreme.Guilds.Command.Admin.Request.AddToGuildRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
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
 * @author Lord36 aka Apfelcreme
 */
public class AdminAddCommand extends SubCommand {
    public AdminAddCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.addCommand")) {
            if (strings.length >= 2) {
                UUID uuid = plugin.getUUID(strings[1]);
                if (uuid != null) {
                    Guild guild = plugin.getGuildManager().getGuild(uuid);
                    if (guild == null) {
                        Guild targetGuild = plugin.getGuildManager().getGuild(strings[2]);
                        if (targetGuild != null) {
                            if (targetGuild.getMembers().size() < targetGuild.getCurrentLevel().getPlayerLimit()) {
                                plugin.getRequestController().addRequest(new AddToGuildRequest(plugin, sender, uuid, targetGuild));
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getText("info.guildadmin.confirm.confirm"));
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getText("error.guildFull"));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.guildDoesntExist"));

                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.isInAGuildAlready"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.playerDoesntExist")
                            .replace("{0}", strings[1]));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.addAdmin"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
