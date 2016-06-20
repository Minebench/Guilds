package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
 * @author Lord36 aka Apfelcreme on 21.05.2015.
 */
public class MenuCommand extends SubCommand {

    public MenuCommand(Guilds plugin) {
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

        if (strings.length >= 1 && GuildsUtil.isNumeric(strings[0])) {
            page = Integer.parseInt(strings[0]) - 1;
        }
        if (page >= 0) {
            List<String> commands = plugin.getGuildsConfig().getGuildCommandStrings(plugin.getGuildManager().getGuild(sender) != null);
            Integer pageSize = plugin.getGuildsConfig().getListsPageSize();
            Integer maxPages = (int) Math.ceil((float) commands.size() / pageSize);
            if (page >= maxPages - 1) {
                page = maxPages - 1;
            }
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.command.head")
                    .replace("{0}", Integer.toString(page + 1))
                    .replace("{1}", maxPages.toString()));
            for (int i = page * pageSize; i < (page * pageSize) + pageSize; i++) {
                if (i < commands.size()) {
                    if (!commands.get(i).startsWith("MemorySection")) {
                        plugin.getChat().sendMessage(sender, ChatColor.DARK_GRAY + " - " + GuildsUtil
                                .replaceChatColors(commands.get(i)));
                    }
                }
            }
            if (commands.size() > pageSize) {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.command.bottom"));
            }
        }
    }
}
