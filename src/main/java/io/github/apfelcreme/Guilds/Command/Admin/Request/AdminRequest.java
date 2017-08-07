package io.github.apfelcreme.Guilds.Command.Admin.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
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
 */
public abstract class AdminRequest extends Request {

    public AdminRequest(Guilds plugin, Player sender) {
        super(plugin, sender);
    }

    public void sendInfoMessage() {
        sendInfoMessage(new String[0]);
    }

    protected void sendInfoMessage(String... replacements) {
        sendInfoMessage("guildadmin", ChatColor.GREEN, replacements);
    }
}
