package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
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
 * @author Lord36 aka Apfelcreme on 12.05.2015.
 */
public class PayRequest extends Request {

    private Guild guild;
    private Double amount;

    public PayRequest(Player sender, Guild guild, Double amount) {
        super(sender);
        this.guild = guild;
        this.amount = amount;
    }

    @Override
    public void execute() {
        EconomyResponse economyResponse = Guilds.getInstance().getEconomy().withdrawPlayer(sender.getPlayer(), amount);
        if (economyResponse.transactionSuccess()) {
            guild.setBalance(guild.getBalance() + amount);
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                    .getColoredText("info.guild.pay.paidMoney", guild.getColor())
                    .replace("{0}", amount.toString()));
            Guilds.getInstance().getChat().sendGuildChannelBroadcast(guild,
                    GuildsConfig.getText("info.chat.playerPaid")
                            .replace("{0}", sender.getName())
                            .replace("{1}", amount.toString()));
            Guilds.getInstance().getLogger().info(sender.getName() + " has payed " + amount.toString() + " to guild '"
                    + guild.getName() + "'");
        }
    }
}
