package io.github.apfelcreme.Guilds.Command.Guild;

import io.github.apfelcreme.Guilds.Command.Guild.Command.*;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
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
public class GuildCommandExecutor implements CommandExecutor {

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
            if (sender.hasPermission("Guilds.user")) {
                SubCommand subCommand = null;
                if (strings.length > 0) {
                    GuildOperation guildOperation = GuildOperation.getOperation(strings[0]);
                    if (guildOperation != null) {
                        switch (guildOperation) {
                            case ACCEPT:
                                subCommand = new InviteAcceptCommand();
                                break;
                            case BB:
                            case BLACKBOARD:
                                subCommand = new BlackboardCommand();
                                break;
                            case COLOR:
                                subCommand = new ColorCommand();
                                break;
                            case CONFIRM:
                                subCommand = new ConfirmRequestCommand();
                                break;
                            case CREATE:
                                subCommand = new CreateCommand();
                                break;
                            case CREATERANK:
                                subCommand = new CreateRankCommand();
                                break;
                            case DELETEPREFIX:
                                subCommand = new DeletePrefixCommand();
                                break;
                            case DELETERANK:
                                subCommand = new DeleteRankCommand();
                                break;
                            case DENY:
                                subCommand = new InviteDenyCommand();
                                break;
                            case DISBAND:
                                subCommand = new DisbandCommand();
                                break;
                            case EDITRANK:
                                subCommand = new EditRankCommand();
                                break;
                            case EXP:
                                subCommand = new GiveExpCommand();
                                break;
                            case HOME:
                                subCommand = new HomeCommand();
                                break;
                            case INFO:
                                subCommand = new InfoCommand();
                                break;
                            case INVITE:
                                subCommand = new InviteCommand();
                                break;
                            case KICK:
                                subCommand = new KickCommand();
                                break;
                            case LEAVE:
                                subCommand = new LeaveCommand();
                                break;
                            case LOOKUP:
                                subCommand = new LookupCommand();
                                break;
                            case LEVEL:
                                subCommand = new LevelCommand();
                                break;
                            case LEVELS:
                                subCommand = new LevelsCommand();
                                break;
                            case LIST:
                                subCommand = new ListCommand();
                                break;
                            case PAY:
                                subCommand = new PayCommand();
                                break;
                            case PREFIX:
                                subCommand = new PrefixCommand();
                                break;
                            case PROMOTE:
                                subCommand = new PromoteCommand();
                                break;
                            case RANK:
                                subCommand = new RankInfoCommand();
                                break;
                            case RANKS:
                                subCommand = new RankListCommand();
                                break;
                            case ROSTER:
                                subCommand = new RosterCommand();
                                break;
                            case SETHOME:
                                subCommand = new SetHomeCommand();
                                break;
                            case UPGRADE:
                                subCommand = new UpgradeCommand();
                                break;
                            case WITHDRAW:
                                subCommand = new WithdrawCommand();
                                break;
                        }
                    } else {
                        subCommand = new MenuCommand();
                    }
                } else {
                    subCommand = new MenuCommand();
                }
                subCommand.execute(sender, strings);
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig.getText("error.noPermission"));
            }
        }
        return false;
    }

    /**
     * all possible sub-commands for /guild
     */
    public enum GuildOperation {
        ACCEPT, BB, BLACKBOARD, COLOR, CONFIRM, CREATE, CREATERANK, DELETEPREFIX, DELETERANK, DENY, DISBAND, EDITRANK, EXP, HOME, INFO, INVITE,
        KICK, LEAVE, LEVEL, LEVELS, LIST, LOOKUP, PAY, PREFIX, PROMOTE, RANK, RANKS, ROSTER, SETHOME, UPGRADE, WITHDRAW;

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
