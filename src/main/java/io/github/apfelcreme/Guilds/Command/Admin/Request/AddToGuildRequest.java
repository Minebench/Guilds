package io.github.apfelcreme.Guilds.Command.Admin.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.Rank;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.entity.Player;

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
 * @author Lord36 aka Apfelcreme on 10.05.2015.
 */
public class AddToGuildRequest extends Request {

    private Guild guild;
    private UUID target;

    public AddToGuildRequest(Player sender, UUID target, Guild guild) {
        super(sender);
        this.target = target;
        this.guild = guild;
    }

    @Override
    public void execute() {
        guild.addMember(target);
        Guilds.getInstance().getChat().sendMessage(sender,
                GuildsConfig.getText("info.guildadmin.add.addedPlayer")
                        .replace("{0}",
                                Guilds.getInstance().getServer().getOfflinePlayer(target) != null ?
                                        Guilds.getInstance().getServer().getOfflinePlayer(target).getName() :
                                        GuildsConfig.getText("info.chat.playerGotAddedUnknownName"))
                        .replace("{1}", guild.getName()));
        Guilds.getInstance().getChat().sendGuildChannelBroadcast(
                guild,
                GuildsConfig.getText("info.chat.playerGotAdded")
                        .replace("{0}", Guilds.getInstance().getServer().getOfflinePlayer(target) != null ?
                                Guilds.getInstance().getServer().getOfflinePlayer(target).getName() :
                                GuildsConfig.getText("info.chat.playerGotAddedUnknownName")));
        Player messageReceiver = Guilds.getInstance().getServer().getPlayer(target);
        if (messageReceiver != null) {
            Guilds.getInstance().getChat().sendMessage(messageReceiver,
                    GuildsConfig.getText("info.guildadmin.add.youGotAdded")
                            .replace("{0}", sender.getName())
                            .replace("{1}", guild.getName()));
        }
        Guilds.getInstance().getLogger().info("Player '" + target + "' was added to '" + guild.getName() + "' by force!");
    }
}
