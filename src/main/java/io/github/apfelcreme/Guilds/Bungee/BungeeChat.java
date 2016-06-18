package io.github.apfelcreme.Guilds.Bungee;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import org.bukkit.entity.Player;

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
 * @author Lord36 aka Apfelcreme on 02.05.2015.
 */
public interface BungeeChat {


    /**
     * sends a message to the given player
     *
     * @param player  the player the message shall be sent to
     * @param message the message that shall be sent
     */
    void sendMessage(Player player, String message);

    /**
     * sends a message to the given player
     *
     * @param guildMember the player the message shall be sent to
     * @param message     the message that shall be sent
     */
    void sendMessage(GuildMember guildMember, String message);

    /**
     * returns the prefix used for chat messages
     *
     * @return the prefix used for chat messages
     */
    String getPrefix();

    /**
     * sends a broadcast message eto all members of an guild
     *
     * @param guild   the guild
     * @param message the broadcast message
     */
    void sendGuildChannelBroadcast(Guild guild, String message);

    /**
     * sends a broadcast message eto all members of an guild
     *
     * @param alliance the alliance
     * @param message  the broadcast message
     */
    void sendAllianceChannelBroadcast(Alliance alliance, String message);

    /**
     * sends a broadcast messag eto all members of an guild
     *
     * @param uuid    the player
     * @param message the broadcast message
     */
    void sendBungeeMessage(UUID uuid, String message);
}

