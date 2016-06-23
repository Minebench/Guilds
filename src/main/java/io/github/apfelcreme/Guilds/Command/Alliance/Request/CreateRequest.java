package io.github.apfelcreme.Guilds.Command.Alliance.Request;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Date;

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
public class CreateRequest extends AllianceRequest {

    private ChatColor color;

    public CreateRequest(Guilds plugin, Guild guild, Player sender, String name, String tag, ChatColor color) {
        super(plugin, sender, guild, new Alliance(name, tag, color, new Date().getTime(), guild));
        this.color = color;
    }

    @Override
    public void execute() {
        plugin.getAllianceManager().create(alliance);
        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                .getColoredText("info.alliance.create.allianceCreated", color)
                .replace("{0}", alliance.getName()));
        plugin.getChat().sendAllianceChannelBroadcast(alliance,
                plugin.getGuildsConfig().getColoredText("info.chat.allianceCreated", alliance.getColor())
                        .replace("{0}", guild.getName())
                        .replace("{1}", alliance.getName()));
        plugin.getLogger().info(guild.getName() + " has created alliance '"
                + alliance.getName() + "'");
    }
}
