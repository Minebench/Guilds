package io.github.apfelcreme.Guilds.Bungee;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
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
 * @author Lord36 aka Apfelcreme
 */
public class SimpleBungeeChat implements BungeeChat {


    /**
     * sends a message to the given player
     *
     * @param player  the player the message shall be sent to
     * @param message the message that shall be sent
     */
    public void sendMessage(Player player, String message) {
        player.sendMessage(getPrefix() + message);
    }

    /**
     * sends a message to the given player
     *
     * @param guildMember the player the message shall be sent to
     * @param message     the message that shall be sent
     */
    public void sendMessage(GuildMember guildMember, String message) {
        Player player = Guilds.getInstance().getServer().getPlayer(guildMember.getUuid());
        if (player != null) {
            sendMessage(player, message);
        }
    }

    /**
     * returns the prefix used for chat messages
     *
     * @return the prefix used for chat messages
     */
    public String getPrefix() {
        return GuildsConfig.getMessagePrefix();
    }

    /**
     * sends a broadcast messag eto all members of an guild
     *
     * @param guild   the guild
     * @param message the broadcast message
     */
    public void sendGuildChannelBroadcast(Guild guild, String message) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("SendGuildChannelBroadcast");
            List<UUID> uuids = new ArrayList<UUID>();
            for (GuildMember guildMember : guild.getMembers()) {
                uuids.add(guildMember.getUuid());
            }
            out.writeUTF(GuildsUtil.join(uuids.toArray(), ","));
            out.writeUTF(
                    GuildsConfig.getText("prefix.chat").replace("{0}", guild.getColor() + guild.getTag()) + ChatColor.GRAY + message);
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
     * sends a broadcast messag eto all members of an guild
     *
     * @param alliance the alliance
     * @param message  the broadcast message
     */
    public void sendAllianceChannelBroadcast(Alliance alliance, String message) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("SendAllianceChannelBroadcast");
            List<UUID> uuids = new ArrayList<UUID>();
            for (Guild guild : alliance.getGuilds()) {
                for (GuildMember guildMember : guild.getMembers()) {
                    uuids.add(guildMember.getUuid());
                }
            }
            out.writeUTF(GuildsUtil.join(uuids.toArray(), ","));
            out.writeUTF(GuildsConfig.getText("prefix.chat").replace("{0}", alliance.getColor() + alliance.getTag()) + ChatColor.GRAY + message);
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
     * sends a broadcast messag eto all members of an guild
     *
     * @param uuid    the player
     * @param message the broadcast message
     */
    public void sendBungeeMessage(UUID uuid, String message) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("SendMessage");
            out.writeUTF(uuid.toString());
            out.writeUTF(getPrefix() + message);
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
}
