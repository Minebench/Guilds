package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

/**
 * Alliances
 * Copyright (C) 2015 Lord36 aka Apfelcreme
 * <p/>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme on 13.05.2015.
 */
public class WithdrawRequest extends GuildRequest {

    private double amount;

    public WithdrawRequest(Guilds plugin, Player sender, Guild guild, Double amount) {
        super(plugin, sender, guild);
        this.amount = amount;
    }

    @Override
    public void execute() {
        if (guild.getBalance() < amount) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getText("error.guildNotEnoughMoney").replace("{0}", String.valueOf(amount)).replace("{1}", String.valueOf(guild.getBalance())));
            return;
        }
        EconomyResponse economyResponse = plugin.getEconomy().depositPlayer(sender.getPlayer(), amount);
        if (economyResponse.transactionSuccess()) {
            plugin.getGuildManager().modifyBalance(guild, -amount);
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getColoredText("info.guild.withdrawMoney.withdrewMoney", guild.getColor())
                    .replace("{0}", Double.toString(amount)));
            plugin.getChat().sendGuildChannelBroadcast(guild,
                    plugin.getGuildsConfig().getText("info.chat.playerWithdrew")
                            .replace("{0}", sender.getName())
                            .replace("{1}", Double.toString(amount)));
            plugin.getLogger().info(sender.getName() + " has withdrew " + amount + " from" +
                    " guild '" + guild.getName() + "'");
            plugin.getGuildManager().logMoneyOperation(guild, sender.getUniqueId(), -amount);
        }
    }

    public void sendInfoMessage() {
        sendInfoMessage(String.valueOf(amount));
    }
}
