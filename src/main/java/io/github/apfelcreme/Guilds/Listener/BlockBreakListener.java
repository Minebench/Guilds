package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
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
 * @author Lord36 aka Apfelcreme
 */
public class BlockBreakListener implements Listener {

    private Guilds plugin;

    public BlockBreakListener(Guilds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getGuildsConfig().isSpecialDropBonusActivated()) {
            return;
        }

        Guild guild = plugin.getGuildManager().getGuild(event.getPlayer().getUniqueId());
        if (guild == null) {
            return;
        }

        for (ItemStack drop : GuildsUtil.getDrops(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand())) {
            if (!plugin.getGuildsConfig().isSpecialDrop(drop)) {
                continue;
            }

            if (guild.getCurrentLevel().getSpecialDropChance() < 1.0 && Math.random() > guild.getCurrentLevel().getSpecialDropChance()) {
                continue;
            }

            double matChance = plugin.getGuildsConfig().getSpecialDropChance(drop);
            if (matChance < 1.0 && Math.random() > matChance) {
                continue;
            }

            event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(drop.getType(), 1));
            plugin.getChat().sendMessage(event.getPlayer(),
                    plugin.getGuildsConfig().getColoredText("info.guild.specialDropCreated", guild.getColor())
                            .replace("{0}", GuildsUtil.humanize(drop.getType())));
            plugin.debug(event.getPlayer().getName() + "/" + guild.getName() + " generated a special drop (" + drop.getType() + ")");
        }

    }

}
