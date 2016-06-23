package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildLevel;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.Material;
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
public class UpgradeRequest extends Request {

    private Guild guild;

    public UpgradeRequest(Guilds plugin, Player sender, Guild guild) {
        super(plugin, sender);
        this.guild = guild;
    }

    @Override
    public void execute() {
        plugin.getGuildManager().upgrade(guild);
        plugin.getChat().sendMessage(sender, plugin.getGuildsConfig()
                .getColoredText("info.guild.upgrade.upgradedGuild", guild.getColor()));
        plugin.getChat().sendGuildChannelBroadcast(
                guild,
                plugin.getGuildsConfig().getText("info.chat.guildUpgraded")
                        .replace("{0}", sender.getName())
                        .replace("{1}", plugin.getGuildManager().getNextLevel(guild).getName()));

        GuildLevel nextLevel = plugin.getGuildManager().getNextLevel(guild);
        if (nextLevel != null && plugin.getGuildsConfig().requireMaterialForUpgrade()) {
            for (Map.Entry<Material, Integer> entry : nextLevel.getMaterialRequirements().entrySet()) {
                GuildsUtil.removeItems(sender.getInventory(), entry.getKey(), entry.getValue(), true);
            }
        }

        plugin.getLogger().info(sender.getName() + " has upgraded guild '" + guild.getName() + "'" +
                " to level "+(nextLevel != null ? Integer.toString(nextLevel.getLevel()) : "no next level?"));
    }
}
