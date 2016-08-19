package io.github.apfelcreme.Guilds.Guild;

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
 * @author Lord36 aka Apfelcreme on 25.04.2015.
 */
public class Invite {

    private Guild guild;
    private UUID targetPlayer;
    private GuildMember sender;
    private String targetName;

    public Invite(Guild guild, UUID targetPlayer, GuildMember sender, String targetName) {
        this.guild = guild;
        this.targetPlayer = targetPlayer;
        this.sender = sender;
        this.targetName = targetName;
    }

    /**
     * returns the guild
     *
     * @return the guild
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * sets the guild
     *
     * @param guild the guild to be set
     */
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    /**
     * returns the uuid of the player the invite was sent to
     *
     * @return the uuid of the player the invite was sent to
     */
    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    /**
     * returns the name of the player the invite was sent to
     *
     * @return the name of player the invite was sent to
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * returns the sender of the invite
     *
     * @return the sender of the invite
     */
    public GuildMember getSender() {
        return sender;
    }

    /**
     * sets the sender of the invite
     *
     * @param sender
     */
    public void setSender(GuildMember sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "guild=" + guild +
                ", targetPlayer=" + targetPlayer +
                ", sender=" + sender +
                '}';
    }
}
