package io.github.apfelcreme.Guilds.Bungee;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
     * sends a request to the bungee to sync the list of guilds on all servers
     */
    public void forceGuildsSync() {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            return;
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncGuilds");
            Player player =  plugin.getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(plugin, "Guilds", b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a request to the bungee to sync a single guild on all servers
     */
    public void forceGuildSync(Integer guildId) {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            return;
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncGuild");
            out.writeInt(guildId);
            Player player =  plugin.getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(plugin, "Guilds", b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a request to the bungee to sync the list of alliances on all servers
     */
    public void forceAlliancesSync() {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            return;
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncAlliances");
            Player player = plugin.getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(plugin, "Guilds", b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a request to the bungee to sync a single alliance on all servers
     */
    public void forceAllianceSync(Integer allianceId) {
        if (!plugin.getGuildsConfig().useBungeeCord()) {
            return;
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncAlliance");
            out.writeInt(allianceId);
            Player player = plugin.getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(plugin, "Guilds", b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a player to the guild home
     *
     * @param player the player that shall be teleported
     * @param guild  the guild
     */
    public void sendPlayerToGuildHome(Player player, Guild guild) {

        if (plugin.getGuildsConfig().useBungeeCord() && plugin.getGuildsConfig().isCrossServerTeleportAllowed()) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            try {
                out.writeUTF("SendPlayerToGuildHome");
                out.writeUTF(player.getUniqueId().toString());
                out.writeUTF(guild.getName());
                out.writeUTF(guild.getHomeServer());
                player.sendPluginMessage(plugin,
                        "Guilds", b.toByteArray());
                out.close();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String serverAddress = plugin.getServer().getIp()
                    + ":" + Integer.toString(plugin.getServer().getPort());
            if (serverAddress.equals(guild.getHomeServer())) {
                player.teleport(plugin.getGuildManager().getHome(guild));
                plugin.getChat().sendMessage(player,
                        plugin.getGuildsConfig().getColoredText(
                                "info.guild.home.teleportedToHome", guild.getColor()));
            } else {
                plugin.getChat().sendMessage(player,
                        plugin.getGuildsConfig().getText("error.wrongHomeServer"));
            }
        }
    }
}
