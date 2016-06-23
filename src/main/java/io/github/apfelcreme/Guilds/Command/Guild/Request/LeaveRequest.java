package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
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
public class LeaveRequest extends GuildRequest {

    private Guild guild;

    public LeaveRequest(Guilds plugin, Player sender, Guild guild) {
        super(plugin, sender, guild);
        this.guild = guild;
    }

    @Override
    public void execute() {
        plugin.getGuildManager().removeMember(guild, guild.getMember(sender.getUniqueId()));
        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                .getColoredText("info.guild.leave.youLeft", guild.getColor()));
        plugin.getChat().sendGuildChannelBroadcast(guild,
                plugin.getGuildsConfig().getText("info.chat.playerLeft")
                        .replace("{0}", sender.getName()));

        plugin.getLogger().info(sender.getName() + " has left guild '" + guild.getName() + "'");
    }
}
