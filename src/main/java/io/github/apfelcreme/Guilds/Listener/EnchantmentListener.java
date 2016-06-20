package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
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
            System.out.println(event.getExpLevelCost());

            final Player player = event.getEnchanter();
            final int enchantmentRefund = (int) (event.getExpLevelCost() * (1 - guild.getCurrentLevel().getEnchantmentCost()));
            System.out.println(enchantmentRefund);
            new BukkitRunnable() {
                public void run() {
                    if (player.isOnline()) {
                        player.giveExp(enchantmentRefund);
                        plugin.getChat().sendMessage(player,
                                plugin.getGuildsConfig().getColoredText("info.guild.enchantmentGotCheaper", guild.getColor())
                                        .replace("{0}", Double.toString(guild.getCurrentLevel().getEnchantmentCost() * 100)));
                    }
                }
            }.runTaskLater(plugin, 1);
        }
    }
}
