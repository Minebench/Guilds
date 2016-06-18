package io.github.apfelcreme.Guilds.Command.Admin.Request;

import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import io.github.apfelcreme.Guilds.GuildsConfig;
import io.github.apfelcreme.Guilds.GuildsUtil;
import org.bukkit.entity.Player;

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
public class ChangeGuildTagRequest extends Request {

    private Guild guild;
    private String newTag;

    public ChangeGuildTagRequest(Player sender, Guild guild, String newTag) {
        super(sender);
        this.guild = guild;
        this.newTag = newTag;
    }

    /**
     * executes the Request
     */
    @Override
    public void execute() {
        guild.setTag(newTag);
        Guilds.getInstance().getLogger().info(guild.getName() + "'s tag was changed to '"
                + newTag + "' by '" + sender.getName() + "'");
        Guilds.getInstance().getChat().sendMessage(sender,
                GuildsConfig.getText("info.guildadmin.changeTag.changedTag")
                        .replace("{0}", guild.getName())
                        .replace("{1}", GuildsUtil.replaceChatColors(newTag)));

    }
}
