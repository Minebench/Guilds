package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Session.EditRankSession;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.SessionController;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
 * @author Lord36 aka Apfelcreme on 03.05.2015.
 */
public class EditRankCommand extends SubCommand {

    public EditRankCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.editRank")) {
            if (strings.length >= 2) {
                Guild guild = plugin.getGuildManager().getGuild(sender);
                if (guild != null) {
                    if (guild.getMember(sender.getUniqueId()).getRank().canPromote()) {
                        EditRankSession editRankSession = SessionController.getInstance().getEditRankSession(
                                guild.getMember(sender.getUniqueId()));
                        if (editRankSession == null) {
                            Rank rank = guild.getRank(strings[1]);
                            if (rank != null) {
                                editRankSession = new EditRankSession(rank);
                                SessionController.getInstance().addEditRankSession(
                                        guild.getMember(sender.getUniqueId()), editRankSession);
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getColoredText("info.guild.editRank.name", guild.getColor())
                                        .replace("{0}", rank.getName()));
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.unknownRank"));
                            }
                        } else {
                            if (strings[1].equalsIgnoreCase("cancel")) {
                                SessionController.getInstance().removeEditRankSession(
                                        guild.getMember(sender.getUniqueId()));
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getColoredText("info.guild.editRank.cancelled", guild.getColor()));
                                return;
                            }
                            switch (editRankSession.getCurrentState()) {
                                case ENTERNAME:
                                    editRankSession.setName(strings[1]);
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.invite", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canInvite() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANINVITE:
                                    editRankSession.setCanInvite(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.kick", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canKick() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANKICK:
                                    editRankSession.setCanKick(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.promote", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canPromote() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANPROMOTE:
                                    editRankSession.setCanPromote(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.disband", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canDisband() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANDISBAND:
                                    editRankSession.setCanDisband(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.upgrade", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canUpgrade() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANUPGRADE:
                                    editRankSession.setCanUpgrade(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.withdrawMoney", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canWithdrawMoney() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANWITHDRAWMONEY:
                                    editRankSession.setCanWithdrawMoney(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.useBlackboard", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canUseBlackboard() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANUSEBLACKBOARD:
                                    editRankSession.setCanUseBlackboard(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.doDiplomacy", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().canDoDiplomacy() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERCANDODIPLOMACY:
                                    editRankSession.setCanDoDiplomacy(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.baseRank", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().isBaseRank() ?
                                                    ChatColor.GREEN + "ja" : ChatColor.RED + "nein"));
                                    editRankSession.nextStep();
                                    break;
                                case ENTERISBASERANK:
                                    editRankSession.setIsBaseRank(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.finish", guild.getColor()));
                                    editRankSession.nextStep();
                                    break;
                                case FINISH:
                                    plugin.getGuildManager().saveEditedRank(editRankSession);
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.editRank.rankEdited", guild.getColor())
                                            .replace("{0}", editRankSession.getRank().getName()));
                                    SessionController.getInstance().removeEditRankSession(
                                            guild.getMember(sender.getUniqueId()));
                                    break;
                            }
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getText("error.rank.noPermission").replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.promote")));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.editRank"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
