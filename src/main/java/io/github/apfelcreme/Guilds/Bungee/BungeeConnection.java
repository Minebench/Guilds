package io.github.apfelcreme.Guilds.Bungee;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
public class BungeeConnection {

    private Guilds plugin;

    public BungeeConnection(Guilds plugin) {
        this.plugin = plugin;
    }

    /**
     * Send a command over the Bungee connection
     * @param operation The operation to do
     * @param arguments Some arguments
     * @param data
     */
    public void send(String operation, String arguments, Object... data) {
        if (!plugin.getServer().getMessenger().isOutgoingChannelRegistered(plugin, "guilds:" + operation)) {
            plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "guilds:" + operation);
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF(arguments);

            for (Object w : data) {
                if (w instanceof String) {
                    out.writeUTF((String) w);
                } else if (w instanceof Integer) {
                    out.writeInt((Integer) w);
                } else {
                    try (ByteArrayOutputStream bos = new ByteArrayOutputStream ();
                         ObjectOutputStream oOut = new ObjectOutputStream(bos)){
                        oOut.writeObject(w);
                        out.write(bos.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Player player =  plugin.getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(plugin, "guilds:" + operation, b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a request to the bungee to sync the list of guilds on all servers
     */
    public void forceGuildsSync() {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            plugin.getGuildManager().loadGuilds();
            return;
        }
        send("sync", "guilds");
    }

    /**
     * sends a request to the bungee to sync a single guild on all servers
     */
    public void forceGuildSync(int guildId) {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            plugin.getGuildManager().reloadGuild(guildId);
            return;
        }
        send("sync", "guild", guildId);
    }

    /**
     * sends a request to the bungee to sync the list of alliances on all servers
     */
    public void forceAlliancesSync() {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            plugin.getAllianceManager().loadAlliances();
            return;
        }
        send("sync", "alliances");
    }

    /**
     * sends a request to the bungee to sync a single alliance on all servers
     */
    public void forceAllianceSync(int allianceId) {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            plugin.getAllianceManager().reload(allianceId);
            return;
        }
        send("sync", "alliance", allianceId);
    }

    /**
     * sends a player to the guild home
     *
     * @param player the player that shall be teleported
     * @param guild  the guild
     */
    public void sendPlayerToGuildHome(Player player, Guild guild) {

        if (plugin.getGuildsConfig().useBungeeCord() && plugin.getGuildsConfig().isCrossServerTeleportAllowed()) {

            send("player", "guildhome", player.getUniqueId().toString(), guild.getName(), guild.getHomeServer());
        } else {
            String serverAddress = plugin.getServer().getIp()
                    + ":" + plugin.getServer().getPort();
            if (serverAddress.equals(guild.getHomeServer())) {
                player.teleportAsync(plugin.getGuildManager().getHome(guild)).thenAccept(success -> {
                    plugin.getChat().sendMessage(player,
                            plugin.getGuildsConfig().getColoredText(
                                    "info.guild.home.teleportedToHome", guild.getColor()));
                });
            } else {
                plugin.getChat().sendMessage(player,
                        plugin.getGuildsConfig().getText("error.wrongHomeServer"));
            }
        }
    }
}
