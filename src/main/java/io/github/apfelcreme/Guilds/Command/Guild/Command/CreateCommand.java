package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.CreateRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
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
public class CreateCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(final CommandSender commandSender, final String[] strings) {
        Player sender = (Player) commandSender;
        if (Guilds.getInstance().hasVault()) {
            if (sender.hasPermission("Guilds.createGuild")) {
                if (strings.length >= 4) {
                    String name = strings[1];
                    String tag = strings[2];
                    ChatColor color = GuildsConfig.parseColor(strings[3]);
                    if (GuildsUtil.strip(name).length() <= GuildsConfig.getGuildNameLength()) {
                        if (GuildsUtil.strip(tag).length() <= GuildsConfig.getGuildTagLength()) {
                            if (color != null) {
                                if (Guilds.getInstance().getGuild(sender) == null) {
                                    if (Guilds.getInstance().getGuild(name) == null) {
                                        if (Guilds.getInstance().getEconomy().has(sender, GuildsConfig.getLevelData(1).getCost())) {
                                            RequestController.getInstance().addRequest(
                                                    new CreateRequest(sender, name, tag, color));
                                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                    .getColoredText("info.guild.create.name", color)
                                                    .replace("{0}", GuildsUtil.replaceChatColors(name)));
                                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                    .getColoredText("info.guild.create.tag", color)
                                                    .replace("{0}", GuildsUtil.replaceChatColors(tag)));
                                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                    .getColoredText("info.guild.create.color", color)
                                                    .replace("{0}", WordUtils.capitalize(color.name()
                                                            .replace("_", " ").toLowerCase())));
                                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                    .getColoredText("info.guild.create.price", color)
                                                    .replace("{0}", new DecimalFormat("#.##").format(
                                                            GuildsConfig.getLevelData(1).getCost())));
                                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                                    .getColoredText("info.guild.confirm.confirm", color));
                                        } else {
                                            Guilds.getInstance().getChat().sendMessage(sender,
                                                    GuildsConfig.getText("error.notEnoughMoneyFounding")
                                                            .replace("{0}", GuildsConfig.getLevelData(1).getCost().toString()));
                                        }
                                    } else {
                                        Guilds.getInstance().getChat().sendMessage(sender,
                                                GuildsConfig.getText("error.guildAlreadyExists"));
                                    }
                                } else {
                                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.stillInGuild"));
                                }
                            } else {
                                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noValidColor")
                                        .replace("{0}", strings[3]));
                            }
                        } else {
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.tagTooLong"));
                        }
                    } else {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.nameTooLong"));
                    }
                } else {
                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.wrongUsage.create"));
                }
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noVault"));
        }
    }

}


