package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.BlackboardMessage;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
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
public class BlackboardCommand extends SubCommand {

    public BlackboardCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.guildBlackboard")) {
            Guild guild = plugin.getGuildManager().getGuild(sender);
            if (guild != null) {
                if (strings.length >= 2) {
                    // /guild bb [message]
                    Rank rank = guild.getMember(sender.getUniqueId()).getRank();
                    if (rank.isLeader() || rank.canUseBlackboard()) {
                        if ((strings.length == 2) && strings[1].equalsIgnoreCase("clear")) {
                            plugin.getGuildManager().clearMessages(guild);
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getColoredText("info.guild.blackboard.cleared", guild.getColor()));
                        } else {
                            BlackboardMessage message = new BlackboardMessage(null,
                                    sender.getUniqueId(),
                                    new Timestamp(new Date().getTime()),
                                    concatMessageString(strings), guild);
                            plugin.getGuildManager().addBlackboardMessage(guild, message);
                            plugin.getChat().sendGuildChannelBroadcast(guild, plugin.getGuildManager().formatMessage(guild, message));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getText("error.rank.noPermission")
                                .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.useBlackboard")));
                    }
                } else {
                    // only /guild bb
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                            .getColoredText("info.guild.blackboard.head", guild.getColor()));
                    for (BlackboardMessage message : guild.getBlackboardMessages()) {
                        plugin.getChat().sendMessage(sender, plugin.getGuildManager().formatMessage(guild, message));
                    }
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }

    /**
     * concats a message string from the input array
     *
     * @param strings the input array
     * @return a message string
     */
    private String concatMessageString(String[] strings) {
        String message = "";
        for (int i = 1; i < strings.length; i++) {
            message += strings[i] + " ";
        }
        return message;
    }
}
