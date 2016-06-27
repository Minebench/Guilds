package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

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
 * @author Lord36 aka Apfelcreme on 14.05.2015.
 */
public class CraftItemListener implements Listener {

    private Guilds plugin;

    public CraftItemListener(Guilds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemCraftEvent(CraftItemEvent e) {
        Guild guild = plugin.getGuildManager().getGuild(e.getWhoClicked().getUniqueId());
        if (guild != null) {
            double probability = Math.random();
            if (probability <= guild.getCurrentLevel().getDoubleCraftProbability()) {
                ItemStack doubledItem = e.getCurrentItem();
                e.getWhoClicked().getInventory().addItem(doubledItem);

                plugin.getChat().sendMessage((Player) e.getWhoClicked(),
                        plugin.getGuildsConfig().getColoredText("info.guild.craftItemDoubled", guild.getColor()));
                plugin.debug(e.getWhoClicked().getName() + "/" + guild.getName() + " double crafted  a " + doubledItem.getType() + "!");
            }
        }
    }
}
