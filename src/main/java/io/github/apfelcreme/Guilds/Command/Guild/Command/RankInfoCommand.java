package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.ChatColor;
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
public class RankInfoCommand implements SubCommand {

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
                Guild guild = Guilds.getInstance().getGuild(sender);
                if (guild != null) {
                    Rank rank = guild.getRank(strings[1]);
                    if (rank != null) {
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.head", guild.getColor())
                                        .replace("{0}", rank.getName()));

                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.invite"))
                                        .replace("{1}", (rank.canInvite() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.kick"))
                                        .replace("{1}", (rank.canKick() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.promote"))
                                        .replace("{1}", (rank.canPromote() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.disband"))
                                        .replace("{1}", (rank.canDisband() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.upgrade"))
                                        .replace("{1}", (rank.canUpgrade() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.withdrawMoney"))
                                        .replace("{1}", (rank.canWithdrawMoney() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.useBlackboard"))
                                        .replace("{1}", (rank.canUseBlackboard() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.doDiplomacy"))
                                        .replace("{1}", (rank.canDoDiplomacy() ? GuildsConfig.getText("info.guild.rank.info.allowed") :
                                                GuildsConfig.getText("info.guild.rank.info.notAllowed"))));
                        Guilds.getInstance().getChat().sendMessage(sender,
                                GuildsConfig.getColoredText("info.guild.rank.element", guild.getColor())
                                        .replace("{0}", GuildsConfig.getText("info.guild.rank.info.baseRank"))
                                        .replace("{1}", (rank.isBaseRank() ? GuildsConfig.getText("info.guild.rank.info.bYes") :
                                                GuildsConfig.getText("info.guild.rank.info.bNo"))));

                    } else {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.unknownRank"));
                    }
                } else {
                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noCurrentGuild"));
                }
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.wrongUsage.rank"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
        }
    }
}
