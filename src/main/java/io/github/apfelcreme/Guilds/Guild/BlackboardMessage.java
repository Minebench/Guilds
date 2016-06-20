package io.github.apfelcreme.Guilds.Guild;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

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
 * @author Lord36 aka Apfelcreme on 14.05.2015.
 */
public class BlackboardMessage {

    private Integer id;

    private UUID sender;
    private Timestamp timestamp;
    private String message;

    private Guild guild;

    public BlackboardMessage(Integer id, UUID sender, Timestamp timestamp, String message, Guild guild) {
        this.id = id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.message = message;
        this.guild = guild;
    }

    /**
     * returns the message id
     * @return the message id
     */
    public Integer getId() {
        return id;
    }

    /**
     * returns the sender
     *
     * @return the sender
     */
    public UUID getSender() {
        return sender;
    }

    /**
     * returns the timestamp
     *
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * returns the message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

}
