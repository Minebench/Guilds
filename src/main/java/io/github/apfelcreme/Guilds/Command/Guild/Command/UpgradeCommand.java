package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.UpgradeRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
 * @author Lord36 aka Apfelcreme on 11.05.2015.
 */
public class UpgradeCommand extends SubCommand {

    public UpgradeCommand(Guilds plugin) {
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
        if ((plugin.getServer().getDefaultGameMode() != GameMode.CREATIVE && sender.getGameMode() != GameMode.CREATIVE)
                || sender.hasPermission("Guilds.upgradeGuildCreative")) {
            if (sender.hasPermission("Guilds.upgradeGuild")) {
                Guild guild = plugin.getGuildManager().getGuild(sender);
                if (guild != null) {
                    if (guild.getMember(sender.getUniqueId()).getRank().canUpgrade()) {
                        if (plugin.getGuildManager().hasNextLevel(guild)) {
                            if (plugin.getGuildManager().canBeUpgraded(guild, sender)) {
                                plugin.getChat().sendMessage(sender,
                                        plugin.getGuildsConfig().getColoredText("info.guild.upgrade.ready", guild.getColor())
                                                .replace("{0}", plugin.getGuildManager().getNextLevel(guild).getName()));
                            } else {
                                plugin.getChat().sendMessage(sender,
                                        plugin.getGuildsConfig().getColoredText("info.guild.upgrade.requirementsMissing", guild.getColor())
                                                .replace("{0}", plugin.getGuildManager().getNextLevel(guild).getName()));
                            }
                            if (plugin.getGuildsConfig().requireMoneyForUpgrade()) {
                                if (guild.getBalance() >= plugin.getGuildManager().getNextLevel(guild).getCost()) {
                                    plugin.getChat().sendMessage(sender,
                                            plugin.getGuildsConfig().getColoredText("info.guild.upgrade.enoughMoney", guild.getColor())
                                                    .replace("{0}", Double.toString(guild.getBalance()))
                                                    .replace("{1}", Double.toString(plugin.getGuildManager().getNextLevel(guild).getCost())));
                                } else {
                                    plugin.getChat().sendMessage(sender,
                                            plugin.getGuildsConfig().getColoredText("info.guild.upgrade.notEnoughMoney", guild.getColor())
                                                    .replace("{0}", Double.toString(guild.getBalance()))
                                                    .replace("{1}", Double.toString(plugin.getGuildManager().getNextLevel(guild).getCost())));
                                }
                            }
                            if (plugin.getGuildsConfig().requireExpForUpgrade()) {
                                if (guild.getExp() >= plugin.getGuildManager().getNextLevel(guild).getExpCost()) {
                                    plugin.getChat().sendMessage(sender,
                                            plugin.getGuildsConfig().getColoredText("info.guild.upgrade.enoughExp", guild.getColor())
                                                    .replace("{0}", Integer.toString(guild.getExp()))
                                                    .replace("{1}", Integer.toString(plugin.getGuildManager().getNextLevel(guild).getExpCost())));
                                } else {
                                    plugin.getChat().sendMessage(sender,
                                            plugin.getGuildsConfig().getColoredText("info.guild.upgrade.notEnoughExp", guild.getColor())
                                                    .replace("{0}", Integer.toString(guild.getExp()))
                                                    .replace("{1}", Integer.toString(plugin.getGuildManager().getNextLevel(guild).getExpCost())));
                                }
                            }
                            if (plugin.getGuildsConfig().requireMaterialForUpgrade()) {
                                for (Map.Entry<Material, Integer> entry : plugin.getGuildManager().getNextLevel(guild).getMaterialRequirements().entrySet()) {
                                    if (sender.getInventory().contains(new ItemStack(entry.getKey(), entry.getValue()))) {
                                        plugin.getChat().sendMessage(sender,
                                                plugin.getGuildsConfig().getColoredText("info.guild.upgrade.enoughMaterialElement", guild.getColor())
                                                        .replace("{0}", WordUtils.capitalize(entry.getKey().name().toLowerCase().replace("_", " ")))
                                                        .replace("{1}", Integer.toString(GuildsUtil.countItems(sender.getInventory(), entry.getKey(), false)))
                                                        .replace("{2}", plugin.getGuildManager().getNextLevel(guild).getMaterialRequirements().get(entry.getKey()).toString()));
                                    } else {
                                        plugin.getChat().sendMessage(sender,
                                                plugin.getGuildsConfig().getColoredText("info.guild.upgrade.notEnoughMaterialElement", guild.getColor())
                                                        .replace("{0}", WordUtils.capitalize(entry.getKey().name().toLowerCase().replace("_", " ")))
                                                        .replace("{1}", Integer.toString(GuildsUtil.countItems(sender.getInventory(), entry.getKey(), false)))
                                                        .replace("{2}", plugin.getGuildManager().getNextLevel(guild).getMaterialRequirements().get(entry.getKey()).toString()));

                                    }
                                }
                            }
                            if (plugin.getGuildManager().canBeUpgraded(guild, sender)) {
                                // guild is ready to be upgraded
                                plugin.getRequestController().addRequest(new UpgradeRequest(plugin, sender, guild));
                            }
                        } else {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                    .getText("error.guildFullyUpgraded"));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                                .getText("error.rank.noPermission")
                                .replace("{0}", plugin.getGuildsConfig().getText("info.guild.rank.info.upgrade")));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                            .getText("error.noCurrentGuild"));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                        .getText("error.noPermission"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                    .getText("error.noSurvival"));
        }
    }
}
