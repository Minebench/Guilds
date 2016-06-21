package io.github.apfelcreme.Guilds.Listener;

import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

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
 * @author Lord36 aka Apfelcreme on 14.08.2015.
 */
public class PlayerQuitListener implements Listener {

    private Guilds plugin;

    public PlayerQuitListener(Guilds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        plugin.getRequestController().removeRequest(e.getPlayer());
        plugin.runAsync(new Runnable() {

            public void run() {

                try {
                    Connection connection = plugin.getDatabaseConnection();

                    PreparedStatement statement = connection.prepareStatement("UPDATE " +
                            plugin.getGuildsConfig().getPlayerTable() + " SET lastseen = ? where uuid = ?");
                    statement.setLong(1, new Date().getTime());
                    statement.setString(2, e.getPlayer().getUniqueId().toString());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
