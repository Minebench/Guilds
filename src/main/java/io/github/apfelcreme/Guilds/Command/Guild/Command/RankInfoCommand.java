package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
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
 * @author Lord36 aka Apfelcreme on 15.05.2015.
 */
public class RankInfoCommand extends SubCommand {

    public RankInfoCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.rankInfo")) {
            if (strings.length >= 2) {
                Guild guild;
                int rankIndex = 1;
                if (strings.length > 2 && sender.hasPermission("Guilds.admin.viewOtherRanks")) {
                    guild = plugin.getGuildManager().getGuild(strings[1]);
                    rankIndex = 2;
                } else {
                    guild = plugin.getGuildManager().getGuild(sender);
                }
                if (guild != null) {
                    Rank rank = guild.getRank(strings[rankIndex]);
                    if (rank != null) {
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.head", guild.getColor())
                                        .replace("{0}", rank.getName()));

                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.invite"))
                                        .replace("{1}", (rank.canInvite() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.kick"))
                                        .replace("{1}", (rank.canKick() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.promote"))
                                        .replace("{1}", (rank.canPromote() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.disband"))
                                        .replace("{1}", (rank.canDisband() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.upgrade"))
                                        .replace("{1}", (rank.canUpgrade() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.withdrawMoney"))
                                        .replace("{1}", (rank.canWithdrawMoney() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.useBlackboard"))
                                        .replace("{1}", (rank.canUseBlackboard() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.doDiplomacy"))
                                        .replace("{1}", (rank.canDoDiplomacy() ? plugin.getGuildsConfig().getText("info.guild.rank.info.allowed") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.notAllowed"))));
                        plugin.getChat().sendMessage(sender,
                                plugin.getGuildsConfig().getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.baseRank"))
                                        .replace("{1}", (rank.isBaseRank() ? plugin.getGuildsConfig().getText("info.guild.rank.info.bYes") :
                                                plugin.getGuildsConfig().getText("info.guild.rank.info.bNo"))));

                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.unknownRank"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.rank"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
