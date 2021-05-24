package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.TagChangeRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.command.CommandSender;
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
public class TagChangeCommand extends SubCommand {

    public TagChangeCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.tag")) {
            if (strings.length >= 2) {
                Guild guild = plugin.getGuildManager().getGuild(sender.getUniqueId());
                if (guild != null) {
                    String newTag = strings[1];
                    if (GuildsUtil.strip(newTag).length() <= plugin.getGuildsConfig().getGuildTagLength()) {
                        if (GuildsUtil.strip(newTag).equalsIgnoreCase(GuildsUtil.strip(guild.getTag())) || sender.hasPermission("Guilds.tag.full")) {
                            plugin.getRequestController().addRequest(new TagChangeRequest(plugin, sender, guild, newTag));
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.tagNotSame", GuildsUtil.replaceChatColors(newTag), GuildsUtil.replaceChatColors(guild.getTag())));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.tagTooLong"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.changeTag"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
