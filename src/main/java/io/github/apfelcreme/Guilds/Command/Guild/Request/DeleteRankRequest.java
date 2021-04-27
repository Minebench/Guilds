package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.entity.Player;

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
 * @author Lord36 aka Apfelcreme on 10.05.2015.
 */
public class DeleteRankRequest extends GuildRequest {

    private Rank rank;

    public DeleteRankRequest(Guilds plugin, Player sender, Rank rank, Guild guild) {
        super(plugin, sender, guild);
        this.rank = rank;
        this.guild = guild;
    }

    @Override
    public void execute() {
        Rank rank = guild.getMember(sender.getUniqueId()).getRank();
        if (!rank.isLeader() && !rank.canPromote()) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getText("error.rank.noPermission", plugin.getGuildsConfig().getText("info.guild.rank.info.promote")));
            return;
        }
        plugin.getGuildManager().deleteRank(rank);
        plugin.getChat().sendMessage(sender,
                plugin.getGuildsConfig().getColoredText("info.guild.deleteRank.rankDeleted", guild.getColor()).replace("{0}", rank.getName()));
        plugin.getLogger().info(sender.getName() + " has deleted rank '" + rank.getName() + "' in guild '"
                + guild.getName() + "'");
    }

    public void sendInfoMessage() {
        sendInfoMessage(rank.getName());
    }
}
