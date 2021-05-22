package io.github.apfelcreme.Guilds.Command.Alliance;

import io.github.apfelcreme.Guilds.Command.Alliance.Command.*;
import io.github.apfelcreme.Guilds.Command.Guild.Command.ConfirmRequestCommand;
import io.github.apfelcreme.Guilds.Command.PluginCommandExecutor;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
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
public class AllianceCommandExecutor extends PluginCommandExecutor {

    public AllianceCommandExecutor(Guilds plugin) {
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
            SubCommand subCommand = null;
            if (strings.length > 0) {
                AllianceOperation operation = AllianceOperation.getOperation(strings[0]);
                if (operation != null) {
                    switch (operation) {
                        case ACCEPT:
                            subCommand = new InviteAcceptCommand(plugin);
                            break;
                        case CONFIRM:
                            subCommand = new ConfirmRequestCommand(plugin);
                            break;
                        case CREATE:
                            subCommand = new CreateCommand(plugin);
                            break;
                        case DENY:
                            subCommand = new InviteDenyCommand(plugin);
                            break;
                        case INVITE:
                            subCommand = new InviteCommand(plugin);
                            break;
                        case LEAVE:
                            subCommand = new LeaveCommand(plugin);
                            break;
                        case LIST:
                            subCommand = new ListCommand(plugin);
                            break;
                        case INFO:
                            subCommand = new InfoCommand(plugin);
                            break;
                    }
                } else {
                    subCommand = new MenuCommand(plugin);
                }
            } else {
                subCommand = new MenuCommand(plugin);
            }
            subCommand.execute(sender, strings);
        }
        return false;
    }

    /**
     * all possible sub-commands for /alliance
     */
    public enum AllianceOperation {
        ACCEPT, CONFIRM, CREATE, DENY, INVITE, LEAVE, LIST, INFO;

        /**
         * returns the matching operation
         *
         * @param operationString the string
         * @return the matching enum constant or null
         */
        public static AllianceOperation getOperation(String operationString) {
            for (AllianceOperation operation : AllianceOperation.values()) {
                if (operation.name().equalsIgnoreCase(operationString)) {
                    return operation;
                }
            }
            return null;
        }

    }
}
