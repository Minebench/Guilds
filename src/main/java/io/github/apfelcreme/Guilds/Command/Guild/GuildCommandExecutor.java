package io.github.apfelcreme.Guilds.Command.Guild;

import io.github.apfelcreme.Guilds.Command.Guild.Command.*;
import io.github.apfelcreme.Guilds.Command.PluginCommandExecutor;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guilds;
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
public class GuildCommandExecutor extends PluginCommandExecutor {

    public GuildCommandExecutor(Guilds plugin) {
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
            if (sender.hasPermission("Guilds.user")) {
                SubCommand subCommand = null;
                if (strings.length > 0) {
                    GuildOperation guildOperation = GuildOperation.getOperation(strings[0]);
                    if (guildOperation != null) {
                        switch (guildOperation) {
                            case ACCEPT:
                                subCommand = new InviteAcceptCommand(plugin);
                                break;
                            case BB:
                            case BLACKBOARD:
                                subCommand = new BlackboardCommand(plugin);
                                break;
                            case COLOR:
                                subCommand = new ColorCommand(plugin);
                                break;
                            case CONFIRM:
                                subCommand = new ConfirmRequestCommand(plugin);
                                break;
                            case CREATE:
                                subCommand = new CreateCommand(plugin);
                                break;
                            case CREATERANK:
                                subCommand = new CreateRankCommand(plugin);
                                break;
                            case DELETEPREFIX:
                                subCommand = new DeletePrefixCommand(plugin);
                                break;
                            case DELETERANK:
                                subCommand = new DeleteRankCommand(plugin);
                                break;
                            case DENY:
                                subCommand = new InviteDenyCommand(plugin);
                                break;
                            case DISBAND:
                                subCommand = new DisbandCommand(plugin);
                                break;
                            case EDITRANK:
                                subCommand = new EditRankCommand(plugin);
                                break;
                            case EXP:
                                subCommand = new GiveExpCommand(plugin);
                                break;
                            case HOME:
                                subCommand = new HomeCommand(plugin);
                                break;
                            case INFO:
                                subCommand = new InfoCommand(plugin);
                                break;
                            case INVITE:
                                subCommand = new InviteCommand(plugin);
                                break;
                            case KICK:
                                subCommand = new KickCommand(plugin);
                                break;
                            case LEAVE:
                                subCommand = new LeaveCommand(plugin);
                                break;
                            case LOOKUP:
                                subCommand = new LookupCommand(plugin);
                                break;
                            case LEVEL:
                                subCommand = new LevelCommand(plugin);
                                break;
                            case LEVELS:
                                subCommand = new LevelsCommand(plugin);
                                break;
                            case LIST:
                                subCommand = new ListCommand(plugin);
                                break;
                            case PAY:
                                subCommand = new PayCommand(plugin);
                                break;
                            case PREFIX:
                                subCommand = new PrefixCommand(plugin);
                                break;
                            case PROMOTE:
                                subCommand = new PromoteCommand(plugin);
                                break;
                            case RANK:
                                subCommand = new RankInfoCommand(plugin);
                                break;
                            case RANKS:
                                subCommand = new RankListCommand(plugin);
                                break;
                            case ROSTER:
                                subCommand = new RosterCommand(plugin);
                                break;
                            case SETHOME:
                                subCommand = new SetHomeCommand(plugin);
                                break;
                            case TAG:
                                subCommand = new TagChangeCommand(plugin);
                                break;
                            case UPGRADE:
                                subCommand = new UpgradeCommand(plugin);
                                break;
                            case WITHDRAW:
                                subCommand = new WithdrawCommand(plugin);
                                break;
                        }
                    } else {
                        subCommand = new MenuCommand(plugin);
                    }
                } else {
                    subCommand = new MenuCommand(plugin);
                }
                subCommand.execute(sender, strings);
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
            }
        }
        return false;
    }

    /**
     * all possible sub-commands for /guild
     */
    public enum GuildOperation {
        ACCEPT, BB, BLACKBOARD, COLOR, CONFIRM, CREATE, CREATERANK, DELETEPREFIX, DELETERANK, DENY, DISBAND, EDITRANK, EXP, HOME, INFO, INVITE,
        KICK, LEAVE, LEVEL, LEVELS, LIST, LOOKUP, PAY, PREFIX, PROMOTE, RANK, RANKS, ROSTER, SETHOME, TAG, UPGRADE, WITHDRAW;

        /**
         * returns the matching operation
         *
         * @param operationString
         * @return the matching enum constant or null
         */
        public static GuildOperation getOperation(String operationString) {
            for (GuildOperation guildOperation : GuildOperation.values()) {
                if (guildOperation.name().equalsIgnoreCase(operationString)) {
                    return guildOperation;
                }
            }
            return null;
        }

    }

}
