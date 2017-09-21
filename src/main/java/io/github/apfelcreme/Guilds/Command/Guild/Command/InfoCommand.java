package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
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
public class InfoCommand extends SubCommand {

    public InfoCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.guildInfo")) {
            Guild guild;
            if (strings.length > 1 && strings[1] != null) {
                guild = plugin.getGuildManager().getGuild(strings[1]);
                if (guild == null) {
                    guild = plugin.getGuildManager().getGuildByTag(strings[1]);
                    if (guild == null) {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.guildDoesntExist"));
                        return;
                    }
                }
            } else {
                guild = plugin.getGuildManager().getGuild(sender.getUniqueId());
            }
            if (guild != null) {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.info.head", guild.getColor()));
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.info.name", guild.getColor())
                        .replace("{0}", guild.getName())
                        .replace("{1}", guild.getTag()));
                Alliance alliance = plugin.getAllianceManager().getAlliance(guild);
                if (alliance != null) {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                            .getColoredText("info.guild.info.alliance", guild.getColor())
                            .replace("{0}", alliance.getColor() + alliance.getName())
                            .replace("{1}", alliance.getTag()));
                }
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.info.limit", guild.getColor())
                        .replace("{0}", Integer.toString(guild.getMembers().size()))
                        .replace("{1}", Integer.toString(guild.getCurrentLevel().getPlayerLimit())));
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.info.currentBalance", guild.getColor())
                        .replace("{0}", Double.toString(guild.getBalance()))
                        .replace("{1}", plugin.getGuildManager().hasNextLevel(guild) ?
                                Double.toString(plugin.getGuildManager().getNextLevel(guild).getCost()) : "X"));
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.info.currentExpBalance", guild.getColor())
                        .replace("{0}", Integer.toString(guild.getExp()))
                        .replace("{1}", plugin.getGuildManager().hasNextLevel(guild) ?
                                Integer.toString(plugin.getGuildManager().getNextLevel(guild).getExpCost()) : "X"));
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.info.currentLevel", guild.getColor())
                        .replace("{0}", guild.getCurrentLevel().getName()));
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.info.founded", guild.getColor())
                        .replace("{0}", new SimpleDateFormat("dd.MM.yy HH:mm").format(guild.getFounded())));
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
