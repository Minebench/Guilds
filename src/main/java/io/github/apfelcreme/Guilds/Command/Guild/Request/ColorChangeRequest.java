package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
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
 * @author Lord36 aka Apfelcreme on 16.07.2015.
 */
public class ColorChangeRequest extends GuildRequest {

    private final Guild guild;
    private final ChatColor color;

    public ColorChangeRequest(Guilds plugin, Player sender, Guild guild, ChatColor color) {
        super(plugin, sender, guild);
        this.guild = guild;
        this.color = color;
    }

    /**
     * executes the GuildRequest
     */
    @Override
    public void execute() {
        plugin.getGuildManager().setColor(guild, color);
        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                .getColoredText("info.guild.color.colorSet", color)
                .replace("{0}", WordUtils.capitalize(color.name().replace("_", " ").toLowerCase())));
    }
}
