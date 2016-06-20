package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
 * @author Lord36 aka Apfelcreme on 27.04.2015.
 */
public class ListCommand extends SubCommand {

    public ListCommand(Guilds plugin) {
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
        Integer page = 0;

        if (strings.length >= 2 && GuildsUtil.isNumeric(strings[1])) {
            page = Integer.parseInt(strings[1]) - 1;
        }
        if (sender.hasPermission("Guilds.listGuild")) {
            if (page >= 0) {
                List<Guild> guilds = new ArrayList<Guild>(plugin.getGuildManager().getGuilds());
                Collections.sort(guilds, Collections.<Guild>reverseOrder());
                Integer pageSize = plugin.getGuildsConfig().getListsPageSize();
                Integer maxPages = (int) Math.ceil((float) guilds.size() / pageSize);
                if (page >= maxPages - 1) {
                    page = maxPages - 1;
                }
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.list.head")
                        .replace("{0}", Integer.toString(page + 1))
                        .replace("{1}", maxPages.toString()));
                if (guilds.size() > 0) {
                    for (int i = page * pageSize; i < (page * pageSize) + pageSize; i++) {
                        if (i < guilds.size()) {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getColoredText("info.guild.list.element", guilds.get(i).getColor())
                                    .replace("{0}", guilds.get(i).getTag())
                                    .replace("{1}", guilds.get(i).getName())
                                    .replace("{2}", Integer.toString(guilds.get(i).getMembers().size()))
                                    .replace("{3}", guilds.get(i).getCurrentLevel().getName()));
                        }
                    }
                }
                if (guilds.size() > pageSize) {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.list.bottom"));
                }
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
