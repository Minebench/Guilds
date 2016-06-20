package io.github.apfelcreme.Guilds.Alliance;

import io.github.apfelcreme.Guilds.Bungee.BungeeConnection;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.Manager.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Alliances
 * Copyright (C) 2015 Lord36 aka Apfelcreme
 * <p/>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme on 30.05.2015.
 */
public class AllianceInvite {

    private Alliance alliance;
    private Guild guild;

    public AllianceInvite(Alliance alliance, Guild guild) {
        this.alliance = alliance;
        this.guild = guild;
    }

    @Override
    public String toString() {
        return "AllianceInvite{" +
                "alliance=" + alliance.getName() +
                ", guild=" + guild.getName() +
                '}';
    }

    /**
     * returns the alliance
     *
     * @return the alliance
     */
    public Alliance getAlliance() {
        return alliance;
    }

    /**
     * returns the guild guild
     *
     * @return the guild guild
     */
    public Guild getGuild() {
        return guild;
    }
}
