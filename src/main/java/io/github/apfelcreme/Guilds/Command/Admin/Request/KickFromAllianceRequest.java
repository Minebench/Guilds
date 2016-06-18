package io.github.apfelcreme.Guilds.Command.Admin.Request;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
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
public class KickFromAllianceRequest extends Request {

    private Guild guild;
    private Alliance alliance;

    public KickFromAllianceRequest(Player sender, Guild guild, Alliance alliance) {
        super(sender);
        this.guild = guild;
        this.alliance = alliance;
    }

    /**
     * executes the Request
     */
    @Override
    public void execute() {
        if (alliance.getGuilds().size() > 1) {
            alliance.removeMember(guild);
            Guilds.getInstance().getLogger().info(guild.getName() + " was kicked from alliance '"
                    + guild.getName() + "' by '" + sender.getName() + "'");
        } else {
            alliance.delete();
            Guilds.getInstance().getLogger().info(guild.getName() + " was kicked from alliance '"
                    + guild.getName() + "' by '" + sender.getName() + "'. The alliance was disbanded as " +
                    "they were the last members!");
        }
        Guilds.getInstance().getChat().sendMessage(sender,
                GuildsConfig.getText("info.guildadmin.kickguild.kickedGuildFromAlliance")
                        .replace("{0}", guild.getName())
                        .replace("{1}", alliance.getName()));
        Guilds.getInstance().getChat().sendGuildChannelBroadcast(
                guild, GuildsConfig.getText("info.chat.youGotKickedFromAlliance")
                        .replace("{0}", alliance.getName()));
        Guilds.getInstance().getChat().sendAllianceChannelBroadcast(
                alliance, GuildsConfig.getText("info.chat.guildKickedFromAlliance")
                        .replace("{0}", guild.getName()));
    }

}
