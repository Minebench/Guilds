package io.github.apfelcreme.Guilds.Manager;

import io.github.apfelcreme.Guilds.Command.Guild.Session.CreateRankSession;
import io.github.apfelcreme.Guilds.Command.Guild.Session.EditRankSession;
import io.github.apfelcreme.Guilds.Guild.GuildMember;
import io.github.apfelcreme.Guilds.Guilds;

import java.util.HashMap;
import java.util.Map;

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
 * @author Lord36 aka Apfelcreme on 12.05.2015.
 */
public class SessionController {

    /**
     * the SessionController instance
     */
    private static SessionController instance = null;

    /**
     * the map of CreateRankSessions
     */
    private Map<GuildMember, CreateRankSession> createRankSessions;

    /**
     * the map of EditRankSessions
     */
    private Map<GuildMember, EditRankSession> editRankSessions;

    /**
     * the plugin instance
     */
    Guilds plugin;

    private SessionController() {
        plugin = Guilds.getInstance();
        createRankSessions = new HashMap<GuildMember, CreateRankSession>();
        editRankSessions = new HashMap<GuildMember, EditRankSession>();
    }

    /**
     * returns the current CreateRankSession for the given player
     *
     * @param creator the creator of the rank
     * @return the session
     */
    public CreateRankSession getCreateRankSession(GuildMember creator) {
        return createRankSessions.get(creator);
    }

    /**
     * adds a session
     *
     * @param creator           the creator
     * @param createRankSession the new session
     */
    public void addCreateRankSession(GuildMember creator, CreateRankSession createRankSession) {
        createRankSessions.put(creator, createRankSession);
    }

    /**
     * removes the CreateRankSession
     *
     * @param creator the creator
     */
    public void removeCreateRankSession(GuildMember creator) {
        createRankSessions.remove(creator);
    }

    /**
     * returns the current EditRankSession for the given player
     *
     * @param creator the creator of the rank
     * @return the session
     */
    public EditRankSession getEditRankSession(GuildMember creator) {
        return editRankSessions.get(creator);
    }

    /**
     * adds a session
     *
     * @param creator         the creator
     * @param editRankSession the new session
     */
    public void addEditRankSession(GuildMember creator, EditRankSession editRankSession) {
        editRankSessions.put(creator, editRankSession);
    }

    /**
     * removes the EditRankSession
     *
     * @param creator the creator
     */
    public void removeEditRankSession(GuildMember creator) {
        editRankSessions.remove(creator);
    }


    /**
     * returns the SessionController instance
     *
     * @return the SessionController instance
     */
    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }
}
