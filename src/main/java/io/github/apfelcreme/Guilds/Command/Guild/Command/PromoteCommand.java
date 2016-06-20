package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.PromoteRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
public class PromoteCommand extends SubCommand {

    public PromoteCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.promoteGuildMember")) {
            if (strings.length >= 3) {
                UUID uuid = plugin.getUUID(strings[1]);
                if (uuid != null) {
                    Guild guild = plugin.getGuildManager().getGuild(sender);
                    Guild targetGuild = plugin.getGuildManager().getGuild(uuid);
                    if (guild != null) {
                        if (targetGuild != null && guild.equals(targetGuild)) {
                            if (guild.getMember(sender.getUniqueId()).getRank().canPromote()) {
                                Rank rank = guild.getRank(strings[2]);
                                if (rank != null) {
                                    plugin.getRequestController().addRequest(
                                            new PromoteRequest(plugin,
                                                    sender, guild.getMember(uuid),
                                                    guild, rank));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.confirm.confirm", guild.getColor()));
                                } else {
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.unknownRank"));
                                }
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getText("error.rank.noPermission").replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.promote")));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.notInThisGuild")
                                    .replace("{0}", strings[1]).replace("{1}", guild.getName()));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.playerDoesntExist")
                            .replace("{0}", strings[1]));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.promote"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }

    }
}
