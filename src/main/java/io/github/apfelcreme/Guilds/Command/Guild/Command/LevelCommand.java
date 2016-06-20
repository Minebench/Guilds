package io.github.apfelcreme.Guilds.Command.Guild.Command;

import io.github.apfelcreme.Guilds.Command.SubCommand;
import io.github.apfelcreme.Guilds.Guild.GuildLevel;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

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
public class LevelCommand extends SubCommand {

    public LevelCommand(Guilds plugin) {
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
        if (sender.hasPermission("Guilds.level")) {
            if (strings.length > 1) {
                if (GuildsUtil.isNumeric(strings[1])) {
                    GuildLevel level = plugin.getGuildsConfig().getLevelData(Integer.parseInt(strings[1]));
                    if (level != null) {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.head")
                                .replace("{0}", level.getName()));
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.cost")
                                .replace("{0}", level.getCost().toString()));
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.expCost")
                                .replace("{0}", level.getExpCost().toString()));
                        for (Map.Entry<Material, Integer> entry : level.getMaterialRequirements().entrySet()) {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.materialElement")
                                    .replace("{0}", WordUtils.capitalize(entry.getKey().name().toLowerCase().replace("_", " ")))
                                    .replace("{1}", entry.getValue().toString()));
                        }
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.head2"));
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.playerLimit")
                                .replace("{0}", level.getPlayerLimit().toString()));
                        if (plugin.getGuildsConfig().isEnchantmentBonusActivated()) {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.enchantmentCost")
                                    .replace("{0}", Double.toString(level.getEnchantmentCost() * 100)));
                        }
                        if (plugin.getGuildsConfig().isDoubleCraftingBonusActivated()) {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.doubleCraftProbability")
                                    .replace("{0}", Double.toString(level.getDoubleCraftProbability() * 100)));
                        }
                        if (plugin.getGuildsConfig().isSpecialDropBonusActivated()) {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.specialDropChance")
                                    .replace("{0}", Double.toString(level.getSpecialDropChance() * 100)));
                        }
                        if (plugin.getGuildsConfig().isMoreFurnaceExpBonusActivated()) {
                            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("info.guild.level.furnaceExpGainRatio")
                                    .replace("{0}", Double.toString(Math.ceil((level.getFurnaceExpGainRatio() - 1) * 100))));
                        }
                    } else {
                        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.unknownLevel"));
                    }
                } else {
                    plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noNumber")
                            .replace("{0}", strings[1]));
                }
            } else {
                plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.wrongUsage.level"));
            }
        } else {
            plugin.getChat().sendMessage(sender, plugin.getGuildsConfig().getText("error.noPermission"));
        }
    }
}
