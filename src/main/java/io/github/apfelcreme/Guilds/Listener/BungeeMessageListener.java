package io.github.apfelcreme.Guilds.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

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
 * @author Lord36 aka Apfelcreme on 26.04.2015.
 */
public class BungeeMessageListener implements PluginMessageListener {

    private Guilds plugin;

    public BungeeMessageListener(Guilds plugin) {
        this.plugin = plugin;

        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "guilds:sync", this);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "guilds:player", this);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "guilds:error", this);
    }

    public void onPluginMessageReceived(String s, final Player player, byte[] bytes) {
        if (!s.startsWith("guilds:") || !plugin.getGuildsConfig().useBungeeCord()) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String operation = s.split(":")[1];
        String[] args = in.readUTF().split(" ");
        switch (operation) {
            case "sync":
                switch (args[0]) {
                    case "guilds":
                        String[] guildsPlayers = in.readUTF().split(" ");
                        plugin.getGuildManager().loadGuilds();
                        setPlayerStatus(guildsPlayers);
                        return;
                    case "alliances":
                        plugin.getAllianceManager().loadAlliances();
                        return;
                    case "guild":
                        Integer guildId = in.readInt();
                        String[] guildPlayers = in.readUTF().split(" ");
                        plugin.getGuildManager().reloadGuild(guildId);
                        setPlayerStatus(guildPlayers);
                        return;
                    case "alliance":
                        Integer allianceId = in.readInt();
                        plugin.getAllianceManager().reload(allianceId);
                        return;
                }
                return;
            case "player":
                String uuid = in.readUTF();
                GuildMember guildMember = plugin.getGuildManager().getGuildMember(UUID.fromString(uuid));
                switch (args[0]) {
                    case "home":
                        String guildName = in.readUTF();
                        Guild guild = plugin.getGuildManager().getGuild(guildName);
                        sendPlayerToGuildHome(uuid, guild);
                        return;
                    case "joined":
                        if (guildMember != null) {
                            guildMember.setOnline(true);
                        }
                        return;
                    case "disconnected":
                        if (guildMember != null) {
                            guildMember.setOnline(false);
                        }
                        return;
                }
                return;
            case "error":
                uuid = in.readUTF();
                guildMember = plugin.getGuildManager().getGuildMember(UUID.fromString(uuid));
                if (guildMember != null) {
                    plugin.getChat().sendMessage(guildMember, plugin.getGuildsConfig().getText("error." + args[0]));
                }
        }
    }

    /**
     * sets the players online statuses
     * @param uuids an array of uuids
     */
    private void setPlayerStatus(final String[] uuids) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            public void run() {
                for (String uuidString : uuids) {
                    UUID uuid = UUID.fromString(uuidString);
                    GuildMember guildMember = plugin.getGuildManager().getGuildMember(uuid);
                    if (guildMember != null) {
                        guildMember.setOnline(true);
                    }
                }
            }
        }, 20L);
    }

    /**
     * sends a player to his guild home
     * @param uuid the player
     * @param guild the guild he is part of
     */
    private void sendPlayerToGuildHome(final String uuid, final Guild guild) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            public void run() {
                Location home = plugin.getGuildManager().getHome(guild);
                Player target = plugin.getServer().getPlayer(UUID.fromString(uuid));
                target.teleport(home);

                plugin.getChat().sendMessage(target,
                        plugin.getGuildsConfig().getColoredText(
                                "info.guild.home.teleportedToHome", guild.getColor()));
            }
        }, 40L);

    }
}