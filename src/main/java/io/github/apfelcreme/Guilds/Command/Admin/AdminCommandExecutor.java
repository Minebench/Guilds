package io.github.apfelcreme.Guilds.Command.Admin;

import io.github.apfelcreme.Guilds.Command.Admin.Command.*;
import io.github.apfelcreme.Guilds.Command.Guild.Command.ConfirmRequestCommand;
import io.github.apfelcreme.Guilds.Command.PluginCommandExecutor;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
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
public class AdminCommandExecutor extends PluginCommandExecutor {

    public AdminCommandExecutor(Guilds plugin) {
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
            if (sender.hasPermission("Guilds.mod")) {
                SubCommand subCommand = null;
                if (strings.length > 0) {
                    AdminOperation operation = AdminOperation.getOperation(strings[0]);
                    if (operation != null) {
                        switch (operation) {
                            case ADD:
                                subCommand = new AdminAddCommand(plugin);
                                break;
                            case CHANGENAME:
                                subCommand = new AdminChangeGuildNameCommand(plugin);
                                break;
                            case CHANGETAG:
                                subCommand = new AdminChangeGuildTagCommand(plugin);
                                break;
                            case CONFIRM:
                                subCommand = new ConfirmRequestCommand(plugin);
                                break;
                            case KICKGUILD:
                                subCommand = new AdminKickFromAllianceCommand(plugin);
                                break;
                            case RELOAD:
                                subCommand = new AdminConfigReloadCommand(plugin);
                                break;
                            case SYNC:
                                subCommand = new AdminSyncCommand(plugin);
                                break;
                        }
                    } else {
                        subCommand = new AdminMenuCommand(plugin);
                    }
                } else {
                    subCommand = new AdminMenuCommand(plugin);
                }
                subCommand.execute(sender, strings);
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
            }
        }
        return false;
    }

    /**
     * all possible sub-commands for /guildadmin
     */
    public enum AdminOperation {
        ADD, CHANGENAME, CHANGETAG, CONFIRM, KICKGUILD, RELOAD, SYNC;

        /**
         * returns the matching operation
         *
         * @param operationString
         * @return the matching enum constant or null
         */
        public static AdminOperation getOperation(String operationString) {
            for (AdminOperation operation : AdminOperation.values()) {
                if (operation.name().equalsIgnoreCase(operationString)) {
                    return operation;
                }
            }
            return null;
        }

    }
}
