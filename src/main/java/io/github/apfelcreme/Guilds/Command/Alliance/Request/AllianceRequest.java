package io.github.apfelcreme.Guilds.Command.Alliance.Request;

import io.github.apfelcreme.Guilds.Alliance.Alliance;
import io.github.apfelcreme.Guilds.Command.Request;
import io.github.apfelcreme.Guilds.Guild.Guild;
import io.github.apfelcreme.Guilds.Guilds;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
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
 */
public abstract class AllianceRequest extends Request {

    protected Guild guild;
    protected Alliance alliance;

    public AllianceRequest(Guilds plugin, Player sender, Guild guild, Alliance alliance) {
        super(plugin, sender);
        this.guild = guild;
        this.alliance = alliance;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void sendInfoMessage() {
        sendInfoMessage(new String[0]);
    }

    protected void sendInfoMessage(String... replacements) {
        sendInfoMessage("alliance", alliance.getColor(),
                (String[]) ArrayUtils.addAll(new String[]{alliance.getName(), alliance.getTag(), guild.getName()}, replacements));
    }
}
