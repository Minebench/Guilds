package io.github.apfelcreme.Guilds.Manager;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

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
public class RequestController {

    /**
     * the RequestController instance
     */
    private static RequestController instance = null;

    /**
     * the plugin instance
     */
    private Guilds plugin;

    private Map<Player, Request> requests;

    public RequestController(Guilds plugin) {
        this.plugin = plugin;
        requests = new HashMap<Player, Request>();
    }

    /**
     * returns the a players current request
     *
     * @param player the player
     * @return the players request
     */
    public Request getRequest(Player player) {
        return requests.get(player);
    }

    /**
     * adds a request
     *
     * @param request the request
     */
    public void addRequest(Request request) {
        requests.put(request.getSender(), request);
    }

    /**
     * removes the given players request
     *
     * @param requestor the request sender
     */
    public void removeRequest(Player requestor) {
        requests.remove(requestor);
    }

    @Override
    public String toString() {
        return "RequestController{" +
                "plugin=" + plugin +
                ", requests=" + requests +
                '}';
    }
}
