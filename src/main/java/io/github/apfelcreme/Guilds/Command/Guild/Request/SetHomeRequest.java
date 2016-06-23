package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.Location;
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
 * @author Lord36 aka Apfelcreme on 11.08.2015.
 */
public class SetHomeRequest extends GuildRequest {

    private Guild guild;
    private Location location;

    public SetHomeRequest(Guilds plugin, Player sender, Guild guild, Location location) {
        super(plugin, sender, guild);
        this.guild = guild;
        this.location = location;
    }

    /**
     * executes the GuildRequest
     */
    @Override
    public void execute() {
        if (location != null) {
            plugin.getGuildManager().setHome(guild, location);
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getColoredText("info.guild.setHome.guildHomeSet", guild.getColor()));
            plugin.getLogger().info("Player " + sender.getName() + " set the guild home of "
                    + guild.getName() + " to Location (" + location.getX() + "/" + location.getY() + "/"
                    + location.getZ() + ") in World " + location.getWorld().getName() + " on server "
                    + plugin.getServer().getName());
        }
    }
}
