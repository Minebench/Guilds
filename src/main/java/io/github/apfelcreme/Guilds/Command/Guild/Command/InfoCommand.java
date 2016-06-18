package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;

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
 * @author Lord36 aka Apfelcreme on 13.05.2015.
 */
public class InfoCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.guildInfo")) {
            Guild guild;
            if (strings.length > 1 && strings[1] != null) {
                guild = Guilds.getInstance().getGuild(strings[1]);
                if (guild == null) {
                    guild = Guilds.getInstance().getGuildByTag(strings[1]);
                    if (guild == null) {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.guildDoesntExist"));
                        return;
                    }
                }
            } else {
                guild = Guilds.getInstance().getGuild(sender.getUniqueId());
            }
            if (guild != null) {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getColoredText("info.guild.info.head", guild.getColor()));
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getColoredText("info.guild.info.name", guild.getColor())
                        .replace("{0}", guild.getName()));
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getColoredText("info.guild.info.limit", guild.getColor())
                        .replace("{0}", Integer.toString(guild.getMembers().size()))
                        .replace("{1}", Integer.toString(guild.getCurrentLevel().getPlayerLimit())));
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getColoredText("info.guild.info.currentBalance", guild.getColor())
                        .replace("{0}", guild.getBalance().toString())
                        .replace("{1}", guild.getCurrentLevel().hasNextLevel() ?
                                guild.getCurrentLevel().nextLevel().getCost().toString() : "X"));
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getColoredText("info.guild.info.currentExpBalance", guild.getColor())
                        .replace("{0}", guild.getExp().toString())
                        .replace("{1}", guild.getCurrentLevel().hasNextLevel() ?
                                guild.getCurrentLevel().nextLevel().getExpCost().toString() : "X"));
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getColoredText("info.guild.info.currentLevel", guild.getColor())
                        .replace("{0}", guild.getCurrentLevel().getName()));
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getColoredText("info.guild.info.founded", guild.getColor())
                        .replace("{0}", new SimpleDateFormat("dd.MM.yy HH:mm").format(guild.getFounded())));
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noCurrentGuild"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
        }
    }
}
