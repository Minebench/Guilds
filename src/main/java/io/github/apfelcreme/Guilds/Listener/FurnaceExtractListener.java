package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

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
 * @author Lord36 aka Apfelcreme on 21.05.2015.
 */
public class FurnaceExtractListener implements Listener {

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent e) {
        Guild guild = Guilds.getInstance().getGuild(e.getPlayer());
        if (guild != null) {
            int oldGain = e.getExpToDrop();
            int newGain = (int) (oldGain * guild.getCurrentLevel().getFurnaceExpGainRatio());
            e.setExpToDrop(newGain);
            Guilds.getInstance().getChat().sendMessage(e.getPlayer(),
                    GuildsConfig.getColoredText("info.guild.furnaceExtract", guild.getColor())
                            .replace("{0}", Double.toString(Math.ceil((guild.getCurrentLevel().getFurnaceExpGainRatio() - 1) * 100))));

        }
    }
}
