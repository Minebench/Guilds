package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

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
 * @author Lord36 aka Apfelcreme on 27.04.2015.
 */
public class RosterCommand extends SubCommand {

    public RosterCommand(Guilds plugin) {
        super(plugin);
    }

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        String guildName;
        boolean wantsToSeeHisOwnGuild = true;
        Integer page = 0;
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.roster")) {
            if (strings.length >= 2 && !GuildsUtil.isNumeric(strings[1])) {
                guildName = strings[1];
                wantsToSeeHisOwnGuild = false;
            } else {
                if (plugin.getGuildManager().getGuild(sender) != null) {
                    guildName = plugin.getGuildManager().getGuild(sender).getName();
                } else {
                    guildName = "";
                }
            }
            for (int i = 0; i < strings.length; i++) {
                if (GuildsUtil.isNumeric(strings[i])) {
                    page = Integer.parseInt(strings[i]) - 1;
                }
            }
            Guild guild = plugin.getGuildManager().getGuild(guildName);
            if (guild == null) {
                guild = plugin.getGuildManager().getGuildByTag(guildName);
            }
            if (guild != null) {
                if (page >= 0) {
                    Integer pageSize = plugin.getGuildsConfig().getListsPageSize();
                    Integer maxPages = (int) Math.ceil((float) guild.getMembers().size() / pageSize);
                    if (page >= maxPages - 1) {
                        page = maxPages - 1;
                    }
                    if (wantsToSeeHisOwnGuild) {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getColoredText("info.guild.roster.headYou", guild.getColor())
                                .replace("{0}", Integer.toString(page + 1))
                                .replace("{1}", maxPages.toString()));
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getColoredText("info.guild.roster.head", guild.getColor())
                                .replace("{0}", guild.getName())
                                .replace("{1}", Integer.toString(page + 1))
                                .replace("{2}", maxPages.toString()));
                    }
                    List<GuildMember> members = new ArrayList<GuildMember>(guild.getMembers());
                    Collections.sort(members, Collections.<GuildMember>reverseOrder());
                    for (int i = page * pageSize; i < (page * pageSize) + pageSize; i++) {
                        if (i < members.size()) {
                            if (members.get(i).isOnline()) {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getColoredText("info.guild.roster.elementOnline", guild.getColor())
                                        .replace("{0}", members.get(i).getRank().getName()
                                                + (members.get(i).getPrefix() != null ?
                                                " " + members.get(i).getPrefix()
                                                : "")
                                        )
                                        .replace("{1}", members.get(i).getName()));
                            } else {
                                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                        .getColoredText("info.guild.roster.elementOffline", guild.getColor())
                                        .replace("{0}", members.get(i).getRank().getName()
                                                + (members.get(i).getPrefix() != null ?
                                                " " + members.get(i).getPrefix()
                                                : "")
                                        )
                                        .replace("{1}", members.get(i).getName()));
                            }
                        }
                    }
                    if (members.size() > pageSize) {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getColoredText("info.guild.roster.bottom", guild.getColor()));
                    }
                }
            } else {
                if (strings.length >= 2 && guildName.equals(strings[1])) {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.guildDoesntExist"));
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
                }
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
