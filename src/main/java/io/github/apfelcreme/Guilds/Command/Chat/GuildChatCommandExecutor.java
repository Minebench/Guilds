package io.github.apfelcreme.Guilds.Command.Chat;

import io.github.apfelcreme.Guilds.Command.PluginCommandExecutor;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
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
 * @author Lord36 aka Apfelcreme on 23.04.2015.
 */
public class GuildChatCommandExecutor extends PluginCommandExecutor {

    public GuildChatCommandExecutor(Guilds plugin) {
        super(plugin);
    }

    /**
     * @param commandSender the sender
     * @param command       the command
     * @param label         the used alias
     * @param strings       the command args
     * @return <tt>true</tt> if the command succeeded; <tt>false</tt> if not
     */
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            Guild guild = plugin.getGuildManager().getGuild(sender);
            if (guild != null) {
                GuildMember member = guild.getMember(sender.getUniqueId());
                String message = "";
                for (String string : strings) {
                    message += ChatColor.GRAY + string + " ";
                }
                message = message.trim();
                plugin.getChat().sendGuildChannelBroadcast(guild,
                        (member.getPrefix() != null ? " " + member.getPrefix() : "") +
                        " " + ChatColor.WHITE + sender.getName()
                                + ": " + message);
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
            }
        }
        return false;
    }
}
