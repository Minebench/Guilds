package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

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
 * @author Lord36 aka Apfelcreme on 01.05.2015.
 */
public class RankListCommand extends SubCommand {

    public RankListCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.rankList")) {
            Guild guild;
            if (strings.length > 1 && sender.hasPermission("Guilds.admin.viewOtherRanks")) {
                guild = plugin.getGuildManager().getGuild(strings[1]);
            } else {
                guild = plugin.getGuildManager().getGuild(sender);
            }
            if (guild != null) {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.ranks.head", guild.getColor()));
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.guild.ranks.head2", guild.getColor()));
                List<Rank> ranks = guild.getRanks();
                Collections.sort(ranks, Collections.<Rank>reverseOrder());
                for (Rank rank : ranks) {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                            .getColoredText("info.guild.ranks.simpleElement", guild.getColor())
                            .replace("{0}", rank.getName()));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }

    }
}
