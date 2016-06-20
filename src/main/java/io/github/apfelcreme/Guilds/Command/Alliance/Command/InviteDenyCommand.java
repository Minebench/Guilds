package io.github.apfelcreme.Guilds.Command.Alliance.Command;

import io.github.apfelcreme.Guilds.Alliance.AllianceInvite;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Alliances
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
 * @author Lord36 aka Apfelcreme on 30.05.2015.
 */
public class InviteDenyCommand extends SubCommand {

    public InviteDenyCommand(Guilds plugin) {
        super(plugin);
    }

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.denyAllianceInvite")) {
            Guild guild = plugin.getGuildManager().getGuild(sender);
            if (guild != null) {
                AllianceInvite invite = plugin.getAllianceManager().getInvite(guild);
                if (invite != null) {
                    plugin.getAllianceManager().denyInvite(invite);
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                            .getColoredText("info.alliance.invite.youDeniedInvite", invite.getAlliance().getColor())
                            .replace("{0}", invite.getAlliance().getName()));
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPendingInvites"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
