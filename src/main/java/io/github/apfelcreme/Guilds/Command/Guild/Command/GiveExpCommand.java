package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.GiveExpRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.bukkit.GameMode;
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
 * @author Lord36 aka Apfelcreme on 22.05.2015.
 */
public class GiveExpCommand extends SubCommand {

    public GiveExpCommand(Guilds plugin) {
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
        Guild guild = plugin.getGuildManager().getGuild(sender);
        if (guild != null) {
            if ((plugin.getServer().getDefaultGameMode() != GameMode.CREATIVE && sender.getGameMode() != GameMode.CREATIVE)
                    || sender.hasPermission("Guilds.giveExpCreative")) {
                if (sender.hasPermission("Guilds.giveExp") && plugin.getGuildsConfig().requireExpForUpgrade()) {
                    if (strings.length >= 2) {
                        String input = strings[1];
                        boolean inputIsLevel = false;
                        if (input.toUpperCase().endsWith("L")) {
                            inputIsLevel = true;
                            input = input.substring(0, input.length() - 1);
                        }
                        if (GuildsUtil.isNumeric(input)) {
                            int inputNumber = Integer.parseInt(input);

                            int exp = inputIsLevel
                                    ? GuildsUtil.getExpAtLevel(sender.getLevel()) - GuildsUtil.getExpAtLevel(sender.getLevel() - inputNumber)
                                    : inputNumber;

                            if (GuildsUtil.getTotalExperience(sender) >= exp && exp >= 0) {
                                plugin.getRequestController().addRequest(new GiveExpRequest(plugin, sender, guild, exp));
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getText("error.notEnoughExp"));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noNumber")
                                    .replace("{0}", input));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.giveExp"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getText("error.noSurvival"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
        }
    }
}
