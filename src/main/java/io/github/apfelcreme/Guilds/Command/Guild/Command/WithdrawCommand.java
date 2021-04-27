package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.WithdrawRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.apache.commons.lang.math.NumberUtils;
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
 * @author Lord36 aka Apfelcreme on 13.05.2015.
 */
public class WithdrawCommand extends SubCommand {

    public WithdrawCommand(Guilds plugin) {
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
        if (plugin.hasVault()) {
            if (sender.hasPermission("Guilds.withdrawFromGuild")) {
                if (strings.length >= 2) {
                    if (NumberUtils.isNumber(strings[1])) {
                        Double amount = Double.parseDouble(strings[1]);
                        Guild guild = plugin.getGuildManager().getGuild(sender);
                        if (guild != null) {
                            Rank rank = guild.getMember(sender.getUniqueId()).getRank();
                            if (rank.isLeader() || rank.canWithdrawMoney()) {
                                if (guild.getBalance() >= amount) {
                                    long bankLimit = guild.getCurrentLevel().getBankLimit();
                                    if (bankLimit < 0 || guild.getBalance() - amount < bankLimit) {
                                        plugin.getRequestController().addRequest(new WithdrawRequest(plugin, sender, guild, amount));
                                    } else {
                                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                                .getText("error.guildBankLimit", String.valueOf(bankLimit), String.valueOf(guild.getBalance() - bankLimit)));
                                    }
                                } else {
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getText("error.guildNotEnoughMoney").replace("{0}", String.valueOf(amount)).replace("{1}", String.valueOf(guild.getBalance())));
                                }
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getText("error.rank.noPermission").replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.withdrawMoney")));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noNumber")
                                .replace("{0}", strings[1]));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.withdrawMoney"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noVault"));
        }
    }
}
