package io.github.apfelcreme.Guilds.Command.Chat;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Command.PluginCommandExecutor;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
 * @author Lord36 aka Apfelcreme on 30.05.2015.
 */
public class AllianceChatCommandExecutor extends PluginCommandExecutor {

    public AllianceChatCommandExecutor(Guilds plugin) {
        super(plugin);
    }

    /**
     * @param commandSender the sender
     * @param command       the command
     * @param s             ???
     * @param strings       the command args
     * @return ??
     */
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            Alliance alliance = plugin.getAllianceManager().getAlliance(sender);
            if (alliance != null) {
                GuildMember member = alliance.getMember(sender.getUniqueId());
                String message = "";
                for (String string : strings) {
                    message += ChatColor.GRAY + string + " ";
                }
                message = message.trim();
                plugin.getChat().sendAllianceChannelBroadcast(alliance,
                        plugin.getGuildsConfig().getText("prefix.chat").replace("{0}", member.getGuild().getColor() + member.getGuild().getTag()) +
                                (member.getPrefix() != null ? " " + member.getPrefix() : "") +
                                " " + ChatColor.WHITE + sender.getName()
                                + ": " + message);

            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getText("error.noCurrentAlliance"));
            }
        }
        return false;
    }
}
