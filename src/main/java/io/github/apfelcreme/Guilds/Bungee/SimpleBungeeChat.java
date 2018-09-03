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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final Guilds plugin;

    public SimpleBungeeChat(Guilds plugin) {
        this.plugin = plugin;
    }

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
        Player player = plugin.getServer().getPlayer(guildMember.getUuid());
        if (player != null && player.isOnline()) {
            sendMessage(player, message);
        }
    }

    /**
     * returns the prefix used for chat messages
     *
     * @return the prefix used for chat messages
     */
    public String getPrefix() {
        return plugin.getGuildsConfig().getMessagePrefix();
    }

    /**
     * sends a broadcast messag eto all members of an guild
     *
     * @param guild   the guild
     * @param message the broadcast message
     */
    public void sendGuildChannelBroadcast(Guild guild, String message) {
        plugin.getBungeeConnection().send("broadcast", "guild",
                guild.getMembers().stream().map(Object::toString).collect(Collectors.joining(",")),
                plugin.getGuildsConfig()
                        .getText("prefix.chat")
                        .replace("{0}", guild.getColor() + GuildsUtil.replaceChatColors(guild.getTag())) + ChatColor.GRAY + message);
    }

    /**
     * sends a broadcast messag eto all members of an guild
     *
     * @param alliance the alliance
     * @param message  the broadcast message
     */
    public void sendAllianceChannelBroadcast(Alliance alliance, String message) {
        plugin.getBungeeConnection().send("broadcast", "alliance",
                alliance.getGuilds().stream().flatMap(g -> g.getMembers().stream()).map(Object::toString).collect(Collectors.joining(",")),
                plugin.getGuildsConfig()
                        .getText("prefix.chat")
                        .replace("{0}", alliance.getColor() + GuildsUtil.replaceChatColors(alliance.getTag())) + ChatColor.GRAY + message);
    }

    /**
     * sends a broadcast messag eto all members of an guild
     *
     * @param uuid    the player
     * @param message the broadcast message
     */
    public void sendBungeeMessage(UUID uuid, String message) {
        plugin.getBungeeConnection().send("player", "message", uuid.toString(), getPrefix() + message);
    }
}
