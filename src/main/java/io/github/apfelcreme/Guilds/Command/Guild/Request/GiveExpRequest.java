package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
 * @author Lord36 aka Apfelcreme on 22.05.2015.
 */
public class GiveExpRequest extends GuildRequest {

    private int exp;

    public GiveExpRequest(Guilds plugin, Player sender, Guild guild, Integer exp) {
        super(plugin, sender, guild);
        this.exp = exp;
    }

    /**
     * executes the GuildRequest
     */
    @Override
    public void execute() {
        int total = GuildsUtil.getTotalExperience(sender);
        if (total < exp) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getText("error.notEnoughExp"));
            return;
        }
        plugin.getGuildManager().setExp(guild, guild.getExp() + exp);
        sender.setTotalExperience(0);
        sender.setLevel(0);
        sender.setExp(0.0f);
        sender.giveExp(total - exp);
        if(sender.getLevel() < 1) {
            sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3.0f, 5.0f);
        }
        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                .getColoredText("info.guild.exp.paidExp", guild.getColor())
                .replace("{0}", Integer.toString(exp)));
        plugin.getChat().sendGuildChannelBroadcast(guild,
                plugin.getGuildsConfig().getText("info.chat.playerPaidExp")
                        .replace("{0}", sender.getName())
                        .replace("{1}", Integer.toString(exp))
        );
        plugin.getLogger().info(sender.getName() + " has payed " + exp + " exp to guild '" + guild.getName() + "'");
    }
}
