package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.entity.Player;

/**
 * Guilds
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
 * @author Lord36 aka Apfelcreme on 10.05.2015.
 */
public class KickRequest extends Request {

    private GuildMember targetPlayer;
    private Guild guild;

    public KickRequest(Player sender, GuildMember targetPlayer, Guild guild) {
        super(sender);
        this.targetPlayer = targetPlayer;
        this.guild = guild;
    }

    @Override
    public void execute() {
        guild.removeMember(targetPlayer);
        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                .getColoredText("info.guild.kick.kickedPlayer", guild.getColor())
                .replace("{0}", targetPlayer.getName()));
        Guilds.getInstance().getChat().sendGuildChannelBroadcast(
                guild, GuildsConfig
                        .getColoredText("info.chat.playerKicked", guild.getColor())
                        .replace("{0}", targetPlayer.getName()));
        //send a message to the kicked player
        Guilds.getInstance().getChat().sendBungeeMessage(targetPlayer.getUuid(), GuildsConfig
                        .getColoredText("info.guild.kick.youGotKicked", guild.getColor())
                        .replace("{0}", sender.getName()));

        Guilds.getInstance().getLogger().info(sender.getName() + " has kicked " + targetPlayer.getName()
                + " from guild '" + guild.getName() + "'");
    }
}
