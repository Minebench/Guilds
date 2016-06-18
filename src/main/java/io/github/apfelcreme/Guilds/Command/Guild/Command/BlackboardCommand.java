package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.BlackboardMessage;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
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
public class BlackboardCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.guildBlackboard")) {
            Guild guild = Guilds.getInstance().getGuild(sender);
            if (guild != null) {
                if (strings.length >= 2) {
                    // /guild bb [message]
                    if (guild.getMember(sender.getUniqueId()).getRank().canUseBlackboard()) {
                        if ((strings.length == 2) && strings[1].equalsIgnoreCase("clear")) {
                            guild.clearMessages();
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                    .getColoredText("info.guild.blackboard.cleared", guild.getColor()));
                        } else {
                            BlackboardMessage message = new BlackboardMessage(null,
                                    sender.getUniqueId(),
                                    new Timestamp(new Date().getTime()),
                                    concatMessageString(strings), guild);
                            message.save();
                            Guilds.getInstance().getChat().sendGuildChannelBroadcast(guild, message.toMessage());
                        }
                    } else {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                .getText("error.rank.noPermission")
                                .replace("{0}", GuildsConfig.getText("info.guild.rank.info.useBlackboard")));
                    }
                } else {
                    // only /guild bb
                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                            .getColoredText("info.guild.blackboard.head", guild.getColor()));
                    for (BlackboardMessage message : guild.getBlackboardMessages()) {
                        Guilds.getInstance().getChat().sendMessage(sender, message.toMessage());
                    }
                }
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noCurrentGuild"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
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
