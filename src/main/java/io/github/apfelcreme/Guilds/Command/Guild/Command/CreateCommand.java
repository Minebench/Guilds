package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.CreateRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

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
public class CreateCommand extends SubCommand {

    public CreateCommand(Guilds plugin) {
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
        if (plugin.hasVault()) {
            if (sender.hasPermission("Guilds.createGuild")) {
                if (strings.length >= 4) {
                    String name = strings[1];
                    String tag = strings[2];
                    ChatColor color = plugin.getGuildsConfig().parseColor(strings[3]);
                    if (GuildsUtil.strip(name).length() <= plugin.getGuildsConfig().getGuildNameLength()) {
                        if (GuildsUtil.strip(tag).length() <= plugin.getGuildsConfig().getGuildTagLength()) {
                            if (color != null) {
                                if (plugin.getGuildManager().getGuild(sender) == null) {
                                    if (plugin.getGuildManager().getGuild(name) == null) {
                                        if (plugin.getEconomy().has(sender, plugin.getGuildsConfig().getLevelData(1).getCost())) {
                                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                                    .getColoredText("info.guild.create.name", color)
                                                    .replace("{0}", GuildsUtil.replaceChatColors(name)));
                                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                                    .getColoredText("info.guild.create.tag", color)
                                                    .replace("{0}", GuildsUtil.replaceChatColors(tag)));
                                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                                    .getColoredText("info.guild.create.color", color)
                                                    .replace("{0}", WordUtils.capitalize(color.name()
                                                            .replace("_", " ").toLowerCase())));
                                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                                    .getColoredText("info.guild.create.price", color)
                                                    .replace("{0}", new DecimalFormat("#.##").format(
                                                            plugin.getGuildsConfig().getLevelData(1).getCost())));
                                            plugin.getRequestController().addRequest(
                                                    new CreateRequest(plugin, sender, name, tag, color));
                                        } else {
                                            plugin.getChat().sendMessage(sender,
                                                    plugin.getGuildsConfig().getText("error.notEnoughMoneyFounding")
                                                            .replace("{0}", Double.toString(plugin.getGuildsConfig().getLevelData(1).getCost())));
                                        }
                                    } else {
                                        plugin.getChat().sendMessage(sender,
                                                plugin.getGuildsConfig().getText("error.guildAlreadyExists"));
                                    }
                                } else {
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.stillInGuild"));
                                }
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noValidColor")
                                        .replace("{0}", strings[3]));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.tagTooLong"));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.nameTooLong"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.create"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noVault"));
        }
    }

}


