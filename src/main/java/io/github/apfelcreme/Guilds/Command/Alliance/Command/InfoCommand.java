package io.github.apfelcreme.Guilds.Command.Alliance.Command;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
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
 * @author Lord36 aka Apfelcreme on 18.06.2015.
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
        if (sender.hasPermission("Guilds.allianceInfo")) {
            Alliance alliance;
            if (strings.length > 1) {
                alliance = plugin.getAllianceManager().getAlliance(strings[1]);
            } else {
                alliance = plugin.getAllianceManager().getAlliance(sender);
            }
            if (alliance != null) {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getColoredText("info.alliance.info.head", alliance.getColor())
                        .replace("{0}", alliance.getName()));
                for (Guild guild : alliance.getGuilds()) {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                            .getColoredText("info.alliance.info.element", guild.getColor())
                            .replace("{0}", guild.getTag())
                            .replace("{1}", guild.getName()));
                }

            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.allianceDoesntExist"));
            }

        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
