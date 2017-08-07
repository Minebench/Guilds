package io.github.apfelcreme.Guilds.Command.Admin.Request;

import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
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
public class AddToGuildRequest extends AdminRequest {

    private Guild guild;
    private UUID target;

    public AddToGuildRequest(Guilds plugin, Player sender, UUID target, Guild guild) {
        super(plugin, sender);
        this.target = target;
        this.guild = guild;
    }

    @Override
    public void execute() {
        String targetName = plugin.getPlayerName(target);
        plugin.getGuildManager().addMember(guild, target);
        plugin.getChat().sendMessage(sender,
                plugin.getGuildsConfig().getText("info.guildadmin.add.addedPlayer")
                        .replace("{0}", targetName != null ? targetName :
                                plugin.getGuildsConfig().getText("info.chat.playerGotAddedUnknownName"))
                        .replace("{1}", guild.getName()));
        plugin.getChat().sendGuildChannelBroadcast(
                guild,
                plugin.getGuildsConfig().getText("info.chat.playerGotAdded")
                        .replace("{0}", targetName != null ? targetName :
                                plugin.getGuildsConfig().getText("info.chat.playerGotAddedUnknownName")));
        Player messageReceiver = plugin.getServer().getPlayer(target);
        if (messageReceiver != null) {
            plugin.getChat().sendMessage(messageReceiver,
                    plugin.getGuildsConfig().getText("info.guildadmin.add.youGotAdded")
                            .replace("{0}", sender.getName())
                            .replace("{1}", guild.getName()));
        }
        plugin.getLogger().info("Player '" + (targetName != null ? targetName + "/" : "") + target + "' was added to '" + guild.getName() + "' by force!");
    }

    public void sendInfoMessage() {
        String targetName = plugin.getPlayerName(target);
        sendInfoMessage(guild.getName(), guild.getTag(), targetName, String.valueOf(target));
    }
}
