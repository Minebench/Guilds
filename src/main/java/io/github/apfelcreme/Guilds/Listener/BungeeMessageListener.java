package io.github.apfelcreme.Guilds.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Locale;
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

    public void onPluginMessageReceived(String s, final Player player, byte[] bytes) {
        if (!s.equals("Guilds")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();
        if (subChannel.equals("syncGuilds")) {
            String[] uuids = in.readUTF().split(" ");
            Guilds.getInstance().loadGuilds();
            setPlayerStatus(uuids);
        } else if (subChannel.equals("syncAlliances")) {
            Guilds.getInstance().loadAlliances();
        } else if (subChannel.equals("syncGuild")) {
            Integer guildId = in.readInt();
            String[] uuids = in.readUTF().split(" ");
            Guilds.getInstance().reloadGuild(guildId);
            setPlayerStatus(uuids);
        } else if (subChannel.equals("syncAlliance")) {
            Integer allianceId = in.readInt();
            Guilds.getInstance().reloadAlliance(allianceId);
        } else if (subChannel.equals("SendPlayerHome")) {
            String uuid = in.readUTF();
            String guildName = in.readUTF();
            Guild guild = Guilds.getInstance().getGuild(guildName);
            sendPlayerToGuildHome(uuid, guild);
        } else if (subChannel.equals("HomeServerUnreachable")) {
            String uuid = in.readUTF();
            GuildMember guildMember = Guilds.getInstance().getGuildMember(UUID.fromString(uuid));
            if (guildMember != null) {
                guildMember.sendMessage(GuildsConfig.getText("error.homeServerUnreachable"));
            }
        } else if (subChannel.equals("PlayerJoined")) {
            String uuid = in.readUTF();
            GuildMember guildMember = Guilds.getInstance().getGuildMember(UUID.fromString(uuid));
            if (guildMember != null) {
                guildMember.setOnline(true);
            }
        } else if (subChannel.equals("PlayerDisconnected")) {
            String uuid = in.readUTF();
            GuildMember guildMember = Guilds.getInstance().getGuildMember(UUID.fromString(uuid));
            if (guildMember != null) {
                guildMember.setOnline(false);
            }
        }
    }

    /**
     * sets the players online statuses
     * @param uuids an array of uuids
     */
    private void setPlayerStatus(final String[] uuids) {
        Guilds.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Guilds.getInstance(), new Runnable() {
            public void run() {
                for (String uuidString : uuids) {
                    UUID uuid = UUID.fromString(uuidString);
                    GuildMember guildMember = Guilds.getInstance().getGuildMember(uuid);
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
        Guilds.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Guilds.getInstance(), new Runnable() {

            public void run() {
                Location home = guild.getGuildHome();
                Player target = Guilds.getInstance().getServer().getPlayer(UUID.fromString(uuid));
                target.teleport(home);

                Guilds.getInstance().getChat().sendMessage(target, GuildsConfig
                        .getColoredText("info.guild.home.teleportedToHome", guild.getColor()));
            }
        }, 40L);

    }
}