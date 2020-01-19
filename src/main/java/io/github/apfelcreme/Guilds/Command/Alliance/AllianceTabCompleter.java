package io.github.apfelcreme.Guilds.Command.Alliance;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
 * @author Lord36 aka Apfelcreme on 23.06.2015.
 */
public class AllianceTabCompleter implements TabCompleter {

    private final Guilds plugin;

    public AllianceTabCompleter(Guilds plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1) {
            for (AllianceCommandExecutor.AllianceOperation operation : AllianceCommandExecutor.AllianceOperation.values()) {
                list.add(operation.name().toLowerCase());
            }
        } else if (strings.length == 2) {
            AllianceCommandExecutor.AllianceOperation guildOperation = AllianceCommandExecutor.AllianceOperation.getOperation(strings[0]);
            if (guildOperation != null) {
                switch (guildOperation) {
                    case ACCEPT:
                        break;
                    case CONFIRM:
                        break;
                    case CREATE:
                        break;
                    case DENY:
                        break;
                    case INFO:
                        for (Alliance alliance1 : plugin.getAllianceManager().getAlliances()) {
                            list.add(alliance1.getName());
                        }
                        break;
                    case INVITE:
                        for (Guild guild1 : plugin.getGuildManager().getGuilds()) {
                            list.add(guild1.getName());
                        }
                        break;
                    case LEAVE:
                        break;
                    case LIST:
                        break;
                }
            }
        }
        return list.stream()
                .filter(s -> strings.length == 0 || s.toLowerCase().startsWith(strings[strings.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
