package io.github.apfelcreme.Guilds.Command;

import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Guilds
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
 * @author Lord36 aka Apfelcreme on 10.05.2015.
 */
public abstract class Request {

    protected Guilds plugin;
    protected Player sender;

    public Request(Guilds plugin, Player sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    public Player getSender() {
        return sender;
    }

    /**
     * executes the Request
     */
    public abstract void execute();

    /**
     * Send an info message about this request to the sender
     */
    public abstract void sendInfoMessage();

    protected void sendInfoMessage(String path, ChatColor color, String... replacements) {
        String key = "request."
                + (path == null || path.isEmpty() ? "" : path + ".")
                + getClass().getSimpleName().toLowerCase().replace("request", "");
        if (plugin.getGuildsConfig().hasText(key)) {
            plugin.getChat().sendMessage(getSender(), plugin.getGuildsConfig().getColoredText(key, color, replacements));
        }
    }
}
