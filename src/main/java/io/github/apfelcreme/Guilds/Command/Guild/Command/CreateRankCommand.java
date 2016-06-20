package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Session.CreateRankSession;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.SessionController;
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
 * @author Lord36 aka Apfelcreme on 28.04.2015.
 */
public class CreateRankCommand extends SubCommand {

    public CreateRankCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.createRank")) {
            Guild guild = plugin.getGuildManager().getGuild(sender);
            if (guild != null) {
                if (guild.getMember(sender.getUniqueId()).getRank().isLeader()) {
                    CreateRankSession createRankSession = SessionController.getInstance().getCreateRankSession(
                            guild.getMember(sender.getUniqueId()));
                    if (createRankSession == null) {
                        createRankSession = new CreateRankSession(sender);
                        SessionController.getInstance().addCreateRankSession(guild.getMember(sender.getUniqueId())
                                , createRankSession);
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getColoredText("info.guild.createRank.name", guild.getColor()));
                    } else {
                        if (strings.length >= 2) {
                            if (strings[1].equalsIgnoreCase("cancel")) {
                                SessionController.getInstance().removeCreateRankSession(
                                        guild.getMember(sender.getUniqueId()));
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getText("info.guild.createRank.cancelled"));
                                return;
                            }
                            switch (createRankSession.getCurrentState()) {
                                case ENTERNAME:
                                    createRankSession.setRankName(strings[1]);
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.invite", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANINVITE:
                                    createRankSession.setCanInvite(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.kick", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANKICK:
                                    createRankSession.setCanKick(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.promote", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANPROMOTE:
                                    createRankSession.setCanPromote(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.disband", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANDISBAND:
                                    createRankSession.setCanDisband(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.upgrade", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANUPGRADE:
                                    createRankSession.setCanUpgrade(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.withdrawMoney", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANWITHDRAWMONEY:
                                    createRankSession.setCanWithdrawMoney(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.useBlackboard", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANUSEBLACKBOARD:
                                    createRankSession.setCanUseBlackboard(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.doDiplomacy", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERCANDODIPLOMACY:
                                    createRankSession.setCanDoDiplomacy(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.baseRank", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case ENTERISBASERANK:
                                    createRankSession.setIsBaseRank(strings[1].equalsIgnoreCase("ja"));
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.finish", guild.getColor()));
                                    createRankSession.nextStep();
                                    break;
                                case FINISH:
                                    Rank rank = new Rank(
                                            null,
                                            createRankSession.getRankName(),
                                            createRankSession.isCanInvite(),
                                            createRankSession.isCanKick(),
                                            createRankSession.isCanPromote(),
                                            createRankSession.isCanDisband(),
                                            createRankSession.isCanUpgrade(),
                                            createRankSession.isCanWithdrawMoney(),
                                            createRankSession.isCanUseBlackboard(),
                                            createRankSession.isCanDoDiplomacy(),
                                            createRankSession.isBaseRank(),
                                            false
                                    );
                                    rank.setGuild(guild);
                                    plugin.getGuildManager().addRank(rank);
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getColoredText("info.guild.createRank.rankCreated", guild.getColor())
                                            .replace("{0}", createRankSession.getRankName()));
                                    SessionController.getInstance().removeCreateRankSession(
                                            guild.getMember(sender.getUniqueId()));
                                    break;
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.createRank"));
                        }

                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                            .getText("error.rank.noPermission").replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.leader")));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }

    }
}
