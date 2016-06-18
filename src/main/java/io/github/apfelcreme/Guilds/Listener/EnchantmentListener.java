package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.Arrays;
import java.util.Map;

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

    @EventHandler
    public void onEnchantmentPrepare(PrepareItemEnchantEvent event) {
        Guild guild = Guilds.getInstance().getGuild(event.getEnchanter());
        if (guild != null) {
            int[] enchantmentCosts = event.getExpLevelCostsOffered();
            for (int i = 0; i < enchantmentCosts.length; i++) {
//                enchantmentCosts[i] = (int) (enchantmentCosts[i] * guild.getCurrentLevel().getEnchantmentCost());
                enchantmentCosts[i] = i;
            }
            //TODO
        }
    }

    @EventHandler
    public void onEnchantment(EnchantItemEvent event) {
        Guild guild = Guilds.getInstance().getGuild(event.getEnchanter());
        if (guild != null) {
            System.out.println(event.getExpLevelCost());

            int enchantmentCost = (int) (event.getExpLevelCost() * guild.getCurrentLevel().getEnchantmentCost());
            event.setExpLevelCost(enchantmentCost);
            System.out.println(enchantmentCost);
            System.out.println(event.getExpLevelCost());
            Guilds.getInstance().getChat().sendMessage(event.getEnchanter(),
                    GuildsConfig.getColoredText("info.guild.enchantmentGotCheaper", guild.getColor())
                            .replace("{0}", Double.toString(guild.getCurrentLevel().getEnchantmentCost() * 100)));
        }
    }
}
