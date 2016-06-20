package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
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
 * @author Lord36 aka Apfelcreme
 */
public class LevelsCommand extends SubCommand {

    public LevelsCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.levels")) {
            ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("level");

            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.levels.head"));
            for (Object o : configurationSection.getKeys(false)) {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.levels.element")
                        .replace("{0}", o.toString())
                        .replace("{1}", plugin.getGuildsConfig().getLevelData(Integer.parseInt(o.toString())).getName()));
            }
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.levels.bottom"));
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
