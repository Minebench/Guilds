package io.github.apfelcreme.Guilds.Command.Alliance.Command;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
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
 * @author Lord36 aka Apfelcreme on 29.05.2015.
 */
public class InviteCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if (sender.hasPermission("Guilds.allianceInvite")) {
            if (strings.length >= 2) {
                if (Guilds.getInstance().getGuild(sender) != null) {
                    Guild guild = Guilds.getInstance().getGuild(sender);
                    Guild targetGuild = Guilds.getInstance().getGuild(strings[1]);
                    if (guild.getMember(sender.getUniqueId()).getRank().canDoDiplomacy()) {
                        if (targetGuild != null) {
                            Alliance alliance = Guilds.getInstance().getAlliance(sender);
                            alliance.sendAllianceInviteTo(targetGuild);
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                    .getColoredText("info.alliance.invite.invitedGuild", alliance.getColor())
                                    .replace("{0}", strings[1]));
                            Guilds.getInstance().getChat().sendGuildChannelBroadcast(
                                    targetGuild, GuildsConfig
                                            .getText("info.chat.allianceGotInvited")
                                            .replace("{0}", alliance.getName()));
                            Guilds.getInstance().getChat().sendGuildChannelBroadcast(
                                    targetGuild, GuildsConfig.getText("info.chat.allianceAccept"));
                        } else {
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                    .getText("error.guildDoesntExist")
                                    .replace("{0}", strings[1]));
                        }
                    } else {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                .getText("error.rank.noPermission").replace("{0}", GuildsConfig.getText("info.guild.rank.info.doDiplomacy")));
                    }
                } else {
                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noCurrentGuild"));
                }
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.wrongUsage.inviteAlliance"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
        }
    }
}
