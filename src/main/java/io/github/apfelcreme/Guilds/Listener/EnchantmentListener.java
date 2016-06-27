package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
public class EnchantmentListener implements Listener {

    private Guilds plugin;

    public EnchantmentListener(Guilds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantment(EnchantItemEvent event) {
        final Guild guild = plugin.getGuildManager().getGuild(event.getEnchanter());
        if (guild != null) {
            event.setExpLevelCost((int) Math.round(event.getExpLevelCost() * guild.getCurrentLevel().getEnchantmentCost()));
            // This is the number of levels a player needs to at least have for this enchantment to be available
            // It is displayed on the right of the enchantment entry

            final Player player = event.getEnchanter();
            final int currentExp = GuildsUtil.getTotalExperience(player);
            new BukkitRunnable() {
                public void run() {
                    if (player.isOnline()) {
                        int enchantmentRefund = (int) Math.round((currentExp - GuildsUtil.getTotalExperience(player)) * (1.0 - guild.getCurrentLevel().getEnchantmentCost()));
                        player.giveExp(enchantmentRefund);
                        plugin.getChat().sendMessage(player,
                                plugin.getGuildsConfig().getColoredText("info.guild.enchantmentGotCheaper", guild.getColor())
                                        .replace("{0}", Integer.toString(enchantmentRefund)));
                        plugin.debug(player.getName() + "/" + guild.getTag() + " reduced enchant by " + (1.0 - guild.getCurrentLevel().getEnchantmentCost()) + "/" + enchantmentRefund);
                    }
                }
            }.runTaskLater(plugin, 1);
        }
    }
}
