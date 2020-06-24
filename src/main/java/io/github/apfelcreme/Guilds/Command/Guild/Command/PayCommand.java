package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.PayRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
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
 * @author Lord36 aka Apfelcreme on 12.05.2015.
 */
public class PayCommand extends SubCommand {

    public PayCommand(Guilds plugin) {
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
            if (sender.hasPermission("Guilds.pay")) {
                if (strings.length >= 2) {
                    if (GuildsUtil.isNumeric(strings[1])) {
                        Double amount = Double.parseDouble(strings[1]);
                        Guild guild = plugin.getGuildManager().getGuild(sender);
                        if (guild != null) {
                            if (plugin.getEconomy().getBalance(sender) >= amount) {
                                long bankLimit = guild.getCurrentLevel().getBankLimit();
                                if (bankLimit < 0 || guild.getBalance() >= bankLimit) {
                                    if (bankLimit > -1 && guild.getBalance() + amount > bankLimit) {
                                        amount = guild.getBalance() + amount - bankLimit;
                                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                                .getText("pay.partial", String.valueOf(amount)));
                                    }
                                    plugin.getRequestController().addRequest(new PayRequest(plugin, sender, guild, amount));
                                } else {
                                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                            .getText("error.guildBankFull", String.valueOf(bankLimit)));
                                }
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.notEnoughMoney"));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noNumber")
                                .replace("{0}", strings[1]));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.pay"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noVault"));
        }
    }
}
