package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.Guild.Request.UpgradeRequest;
import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import io.github.apfelcreme.Guilds.Manager.RequestController;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
public class UpgradeCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param commandSender the sender
     * @param strings       the command args
     */
    public void execute(CommandSender commandSender, String[] strings) {
        Player sender = (Player) commandSender;
        if ((Guilds.getInstance().getServer().getDefaultGameMode() != GameMode.CREATIVE)
                && (sender.getGameMode() != GameMode.CREATIVE)) {
            if (sender.hasPermission("Guilds.upgradeGuild")) {
                Guild guild = Guilds.getInstance().getGuild(sender);
                if (guild != null) {
                    if (guild.getMember(sender.getUniqueId()).getRank().canUpgrade()) {
                        if (guild.getCurrentLevel().hasNextLevel()) {
                            if (guild.canBeUpgraded(sender)) {
                                Guilds.getInstance().getChat().sendMessage(sender,
                                        GuildsConfig.getColoredText("info.guild.upgrade.ready", guild.getColor())
                                                .replace("{0}", guild.getCurrentLevel().nextLevel().getName()));
                            } else {
                                Guilds.getInstance().getChat().sendMessage(sender,
                                        GuildsConfig.getColoredText("info.guild.upgrade.requirementsMissing", guild.getColor())
                                                .replace("{0}", guild.getCurrentLevel().nextLevel().getName()));
                            }
                            if (guild.getBalance() >= guild.getCurrentLevel().nextLevel().getCost()) {
                                Guilds.getInstance().getChat().sendMessage(sender,
                                        GuildsConfig.getColoredText("info.guild.upgrade.enoughMoney", guild.getColor())
                                                .replace("{0}", guild.getBalance().toString())
                                                .replace("{1}", guild.getCurrentLevel().nextLevel().getCost().toString()));
                            } else {
                                Guilds.getInstance().getChat().sendMessage(sender,
                                        GuildsConfig.getColoredText("info.guild.upgrade.notEnoughMoney", guild.getColor())
                                                .replace("{0}", guild.getBalance().toString())
                                                .replace("{1}", guild.getCurrentLevel().nextLevel().getCost().toString()));
                            }
                            if (guild.getExp() >= guild.getCurrentLevel().nextLevel().getExpCost()) {
                                Guilds.getInstance().getChat().sendMessage(sender,
                                        GuildsConfig.getColoredText("info.guild.upgrade.enoughExp", guild.getColor())
                                                .replace("{0}", guild.getExp().toString())
                                                .replace("{1}", guild.getCurrentLevel().nextLevel().getExpCost().toString()));
                            } else {
                                Guilds.getInstance().getChat().sendMessage(sender,
                                        GuildsConfig.getColoredText("info.guild.upgrade.notEnoughExp", guild.getColor())
                                                .replace("{0}", guild.getExp().toString())
                                                .replace("{1}", guild.getCurrentLevel().nextLevel().getExpCost().toString()));
                            }
                            for (Map.Entry<Material, Integer> entry : guild.getCurrentLevel().nextLevel().getMaterialRequirements().entrySet()) {
                                if (sender.getInventory().contains(entry.getKey(), entry.getValue())) {
                                    Guilds.getInstance().getChat().sendMessage(sender,
                                            GuildsConfig.getColoredText("info.guild.upgrade.enoughMaterialElement", guild.getColor())
                                                    .replace("{0}", WordUtils.capitalize(entry.getKey().name().toLowerCase().replace("_", " ")))
                                                    .replace("{1}", GuildsUtil.countItems(sender.getInventory(), entry.getKey()).toString())
                                                    .replace("{2}", guild.getCurrentLevel().nextLevel().getMaterialRequirements().get(entry.getKey()).toString()));
                                } else {
                                    Guilds.getInstance().getChat().sendMessage(sender,
                                            GuildsConfig.getColoredText("info.guild.upgrade.notEnoughMaterialElement", guild.getColor())
                                                    .replace("{0}", WordUtils.capitalize(entry.getKey().name().toLowerCase().replace("_", " ")))
                                                    .replace("{1}", GuildsUtil.countItems(sender.getInventory(), entry.getKey()).toString())
                                                    .replace("{2}", guild.getCurrentLevel().nextLevel().getMaterialRequirements().get(entry.getKey()).toString()));

                                }
                            }
                            if (guild.canBeUpgraded(sender)) {
                                // guild is ready to be upgraded
                                RequestController.getInstance().addRequest(new UpgradeRequest(sender, guild));
                                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                        .getColoredText("info.guild.confirm.confirm", guild.getColor()));
                            }
                        } else {
                            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                    .getText("error.guildFullyUpgraded"));
                        }
                    } else {
                        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                                .getText("error.rank.noPermission")
                                .replace("{0}", GuildsConfig.getText("info.guild.rank.info.upgrade")));
                    }
                } else {
                    Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                            .getText("error.noCurrentGuild"));
                }
            } else {
                Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                        .getText("error.noPermission"));
            }
        } else {
            Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                    .getText("error.noSurvival"));
        }
    }
}
