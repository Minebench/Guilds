package io.github.apfelcreme.Guilds.Command.Guild.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guild.GuildLevel;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
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

    public UpgradeRequest(Player sender, Guild guild) {
        super(sender);
        this.guild = guild;
    }

    @Override
    public void execute() {
        GuildLevel level = guild.getCurrentLevel();
        guild.upgrade();
        Guilds.getInstance().getChat().sendMessage(sender, GuildsConfig
                .getColoredText("info.guild.upgrade.upgradedGuild", guild.getColor()));
        Guilds.getInstance().getChat().sendGuildChannelBroadcast(
                guild,
                GuildsConfig.getText("info.chat.guildUpgraded")
                        .replace("{0}", sender.getName())
                        .replace("{1}", guild.getCurrentLevel().nextLevel().getName()));

        for (Map.Entry<Material, Integer> entry : level.nextLevel().getMaterialRequirements().entrySet()) {
            sender.getInventory().removeItem(new ItemStack(entry.getKey(), entry.getValue()));
        }

        Guilds.getInstance().getLogger().info(sender.getName() + " has upgraded guild '" + guild.getName() + "'" +
                " to level "+Integer.toString(guild.getCurrentLevel().nextLevel().getLevel()));
    }
}
