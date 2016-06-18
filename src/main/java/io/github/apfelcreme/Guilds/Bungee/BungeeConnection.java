package io.github.apfelcreme.Guilds.Bungee;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

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

    /**
     * sends a request to the bungee to sync the list of guilds on all servers
     */
    public static void forceGuildsSync() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncGuilds");
            Player player =
                    Guilds.getInstance().getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(Guilds.getInstance(),
                    "Guilds", b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a request to the bungee to sync a single guild on all servers
     */
    public static void forceGuildSync(Integer guildId) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncGuild");
            out.writeInt(guildId);
            Player player =
                    Guilds.getInstance().getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(Guilds.getInstance(),
                    "Guilds", b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a request to the bungee to sync the list of alliances on all servers
     */
    public static void forceAlliancesSync() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncAlliances");
            Player player =
                    Guilds.getInstance().getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(Guilds.getInstance(),
                    "Guilds", b.toByteArray());
            out.close();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a request to the bungee to sync a single alliance on all servers
     */
    public static void forceAllianceSync(Integer allianceId) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("SyncAlliance");
            out.writeInt(allianceId);
            Player player =
                    Guilds.getInstance().getServer().getOnlinePlayers().iterator().next();
            player.sendPluginMessage(Guilds.getInstance(),
                    "Guilds", b.toByteArray());
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
    public static void sendPlayerToGuildHome(Player player, Guild guild) {

        if (GuildsConfig.isCrossServerTeleportAllowed()) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            try {
                out.writeUTF("SendPlayerToGuildHome");
                out.writeUTF(player.getUniqueId().toString());
                out.writeUTF(guild.getName());
                out.writeUTF(guild.getGuildHomeServer());
                player.sendPluginMessage(Guilds.getInstance(),
                        "Guilds", b.toByteArray());
                out.close();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String serverAddress = Guilds.getInstance().getServer().getIp()
                    + ":" + Integer.toString(Guilds.getInstance().getServer().getPort());
            if (serverAddress.equals(guild.getGuildHomeServer())) {
                player.teleport(guild.getGuildHome());
                Guilds.getInstance().getChat().sendMessage(player, GuildsConfig
                        .getColoredText("info.guild.home.teleportedToHome", guild.getColor()));
            } else {
                Guilds.getInstance().getChat().sendMessage(player, GuildsConfig
                        .getText("error.wrongHomeServer"));
            }
        }
    }
}
