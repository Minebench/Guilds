package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Invite;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;

import org.bukkit.command.CommandSender;
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
 * @author Lord36 aka Apfelcreme on 25.04.2015.
 */
public class InviteDenyCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.denyGuildInvite")) {
            Invite invite = Guilds.getInstance().getInvite(sender.getUniqueId());
            if (invite != null) {
                invite.setDenied();
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("info.guild.invite.youDeniedInvite"));
                Guilds.getInstance().getChat().sendGuildChannelBroadcast(
                        invite.getGuild(),
                        GuildsConfig.getText("info.chat.playerDenied").replace("{0}", sender.getName()));
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPendingInvites"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
        }

    }
}
