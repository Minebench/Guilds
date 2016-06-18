package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.ColorChangeRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
 * @author Lord36 aka Apfelcreme on 16.07.2015.
 */
public class ColorCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.color")) {
            if (strings.length >= 2) {
                Guild guild = Guilds.getInstance().getGuild(sender.getUniqueId());
                if (guild != null) {
                    if (guild.getMember(sender.getUniqueId()).getRank().isLeader()) {
                        ChatColor color = GuildsConfig.parseColor(strings[1]);
                        if (color != null) {
                            RequestController.getInstance().addRequest(new ColorChangeRequest(sender, guild, color));
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                    .getColoredText("info.guild.confirm.confirm", guild.getColor()));
                        } else {
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noValidColor")
                                    .replace("{0}", strings[1]));
                        }
                    } else {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                .getText("error.rank.noPermission").replace("{0}", GuildsConfig.getText("info.guild.rank.info.leader")));
                    }
                } else {
                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noCurrentGuild"));
                }
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.wrongUsage.color"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
        }
    }
}
