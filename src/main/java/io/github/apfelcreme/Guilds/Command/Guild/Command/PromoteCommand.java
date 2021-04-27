package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.PromoteRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.Guild.Rank;
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
 * @author Lord36 aka Apfelcreme on 01.05.2015.
 */
public class PromoteCommand extends SubCommand {

    public PromoteCommand(Guilds plugin) {
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
        if (!sender.hasPermission("Guilds.promoteGuildMember")) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
            return;
        }

        if (strings.length < 3) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.promote"));
            return;
        }

        GuildMember member = plugin.getGuildManager().getGuildMember(sender.getUniqueId());
        if (member == null) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noCurrentGuild"));
            return;
        }

        Guild guild = member.getGuild();

        GuildMember targetMember = guild.getMember(strings[1]);
        if (targetMember == null) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.notInThisGuild", strings[1], guild.getName()));
            return;
        }

        if (!member.getRank().isLeader() && !member.getRank().canPromote()) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getText("error.rank.noPermission", plugin.getGuildsConfig().getText("info.guild.rank.info.promote")));
            return;
        }

        if (targetMember.getRank().isLeader()) {
            if (!member.getRank().isLeader()) {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getText("error.rank.cantChangeLeader", targetMember.getRank().getName()));
                return;
            }

            if (guild.getMembers(targetMember.getRank()).size() == 1) {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getText("error.rank.needsOneLeader", targetMember.getRank().getName()));
                return;
            }
        }

        Rank rank = guild.getRank(strings[2]);
        if (rank == null) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.unknownRank"));
            return;
        }

        if (rank.isLeader() && !member.getRank().isLeader()) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getText("error.rank.cantPromoteToLeader", targetMember.getRank().getName()));
            return;
        }

        if (member == targetMember && !rank.canPromote()) {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getText("error.rank.newCantPromote", rank.getName()));
        }

        plugin.getRequestController().addRequest(
                new PromoteRequest(plugin,
                        sender, targetMember,
                        guild, rank));

    }
}
