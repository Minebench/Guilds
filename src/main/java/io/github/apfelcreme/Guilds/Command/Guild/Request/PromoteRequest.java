package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.entity.Player;

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
 * @author Lord36 aka Apfelcreme on 15.05.2015.
 */
public class PromoteRequest extends GuildRequest {

    private GuildMember targetPlayer;
    private Rank rank;

    public PromoteRequest(Guilds plugin, Player sender, GuildMember targetPlayer, Guild guild, Rank rank) {
        super(plugin, sender, guild);
        this.targetPlayer = targetPlayer;
        this.rank = rank;
    }

    @Override
    public void execute() {
        plugin.getGuildManager().setPlayerRank(guild, targetPlayer, rank);
        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                .getColoredText("info.guild.promote.promotedPlayer", guild.getColor())
                .replace("{0}", targetPlayer.getName())
                .replace("{1}", rank.getName()));
        plugin.getChat().sendGuildChannelBroadcast(
                guild,
                plugin.getGuildsConfig().getText("info.chat.playerPromoted")
                        .replace("{0}", targetPlayer.getName())
                        .replace("{1}", rank.getName()));
        //send a message to the kicked player
        plugin.getChat().sendBungeeMessage(targetPlayer.getUuid(), plugin.getGuildsConfig()
                .getColoredText("info.guild.promote.youGotPromoted", guild.getColor())
                .replace("{0}", rank.getName()));

        plugin.getLogger().info(sender.getName() + " has promoted " + targetPlayer.getName()
                + " into rank '" + rank.getName() + "' in guild '" + guild.getName() + "'");
    }

    public void sendInfoMessage() {
        sendInfoMessage(targetPlayer.getName(), rank.getName(), targetPlayer.getRank().getName());
    }
}
