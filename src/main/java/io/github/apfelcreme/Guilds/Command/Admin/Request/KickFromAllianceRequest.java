package io.github.apfelcreme.Guilds.Command.Admin.Request;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.entity.Player;

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
public class KickFromAllianceRequest extends AdminRequest {

    private Guild guild;
    private Alliance alliance;

    public KickFromAllianceRequest(Guilds plugin, Player sender, Guild guild, Alliance alliance) {
        super(plugin, sender);
        this.guild = guild;
        this.alliance = alliance;
    }

    /**
     * executes the Request
     */
    @Override
    public void execute() {
        if (sender.hasPermission("Guilds.admin.kickFromAlliance")) {
            if (alliance.getGuilds().size() > 1) {
                plugin.getAllianceManager().removeMember(alliance, guild);
                plugin.getLogger().info(guild.getName() + " was kicked from alliance '"
                        + guild.getName() + "' by '" + sender.getName() + "'");
            } else {
                plugin.getAllianceManager().delete(alliance);
                plugin.getLogger().info(guild.getName() + " was kicked from alliance '"
                        + guild.getName() + "' by '" + sender.getName() + "'. The alliance was disbanded as " +
                        "they were the last members!");
            }
            plugin.getChat().sendMessage(sender,
                    plugin.getGuildsConfig().getText("info.guildadmin.kickguild.kickedGuildFromAlliance")
                            .replace("{0}", guild.getName())
                            .replace("{1}", alliance.getName()));
            plugin.getChat().sendGuildChannelBroadcast(
                    guild, plugin.getGuildsConfig().getText("info.chat.youGotKickedFromAlliance")
                            .replace("{0}", alliance.getName()));
            plugin.getChat().sendAllianceChannelBroadcast(
                    alliance, plugin.getGuildsConfig().getText("info.chat.guildKickedFromAlliance")
                            .replace("{0}", guild.getName()));
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }

    public void sendInfoMessage() {
        sendInfoMessage(guild.getName(), guild.getTag(), alliance.getName(), alliance.getTag());
    }

}
