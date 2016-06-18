package io.github.apfelcreme.Guilds.Command.Guild.Session;

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
 * @author Lord36 aka Apfelcreme on 12.05.2015.
 */
public class CreateRankSession {

    private Player creator;
    private CurrentCreationState currentState;

    private String name;

    private boolean canInvite = false;
    private boolean canKick = false;
    private boolean canPromote = false;
    private boolean canDisband = false;
    private boolean canUpgrade = false;
    private boolean canWithdrawMoney = false;
    private boolean canUseBlackboard = false;
    private boolean canDoDiplomacy = false;

    private boolean isBaseRank = false;

    public CreateRankSession(Player creator) {
        this.creator = creator;
        this.currentState = CurrentCreationState.ENTERNAME;
    }

    /**
     * moves the current session one step forward
     */
    public void nextStep() {
        switch (currentState) {
            case ENTERNAME:
                currentState = CurrentCreationState.ENTERCANINVITE;
                break;
            case ENTERCANINVITE:
                currentState = CurrentCreationState.ENTERCANKICK;
                break;
            case ENTERCANKICK:
                currentState = CurrentCreationState.ENTERCANPROMOTE;
                break;
            case ENTERCANPROMOTE:
                currentState = CurrentCreationState.ENTERCANDISBAND;
                break;
            case ENTERCANDISBAND:
                currentState = CurrentCreationState.ENTERCANUPGRADE;
                break;
            case ENTERCANUPGRADE:
                currentState = CurrentCreationState.ENTERCANWITHDRAWMONEY;
                break;
            case ENTERCANWITHDRAWMONEY:
                currentState = CurrentCreationState.ENTERCANUSEBLACKBOARD;
                break;
            case ENTERCANUSEBLACKBOARD:
                currentState = CurrentCreationState.ENTERCANDODIPLOMACY;
                break;
            case ENTERCANDODIPLOMACY:
                currentState = CurrentCreationState.ENTERISBASERANK;
                break;
            case ENTERISBASERANK:
                currentState = CurrentCreationState.FINISH;
                break;
            case FINISH:
                break;
        }
    }

    /**
     * sets the ranks name
     *
     * @param name
     */
    public void setRankName(String name) {
        this.name = name;
    }

    public void setCanInvite(boolean canInvite) {
        this.canInvite = canInvite;
    }

    public void setCanKick(boolean canKick) {
        this.canKick = canKick;
    }

    public void setCanPromote(boolean canPromote) {
        this.canPromote = canPromote;
    }

    public void setCanDisband(boolean canDisband) {
        this.canDisband = canDisband;
    }

    public void setCanUpgrade(boolean canUpgrade) {
        this.canUpgrade = canUpgrade;
    }

    public void setCanWithdrawMoney(boolean canWithdrawMoney) {
        this.canWithdrawMoney = canWithdrawMoney;
    }

    public void setCanUseBlackboard(boolean canUseBlackboard) {
        this.canUseBlackboard = canUseBlackboard;
    }

    public void setCanDoDiplomacy(boolean canDoDiplomacy) {
        this.canDoDiplomacy = canDoDiplomacy;
    }

    public void setIsBaseRank(boolean isBaseRank) {
        this.isBaseRank = isBaseRank;
    }

    public String getRankName() {
        return name;
    }

    public boolean isCanInvite() {
        return canInvite;
    }

    public boolean isCanKick() {
        return canKick;
    }

    public boolean isCanPromote() {
        return canPromote;
    }

    public boolean isCanDisband() {
        return canDisband;
    }

    public boolean isCanUpgrade() {
        return canUpgrade;
    }

    public boolean isCanWithdrawMoney() {
        return canWithdrawMoney;
    }

    public boolean isCanUseBlackboard() {
        return canUseBlackboard;
    }

    public boolean isCanDoDiplomacy() {
        return canDoDiplomacy;
    }

    public boolean isBaseRank() {
        return isBaseRank;
    }

    /**
     * returns the current state of the Session
     *
     * @return the CurrentEditState
     */
    public CurrentCreationState getCurrentState() {
        return currentState;
    }


    public enum CurrentCreationState {
        ENTERNAME,
        ENTERCANINVITE,
        ENTERCANKICK,
        ENTERCANPROMOTE,
        ENTERCANDISBAND,
        ENTERCANUPGRADE,
        ENTERCANWITHDRAWMONEY,
        ENTERCANUSEBLACKBOARD,
        ENTERCANDODIPLOMACY,
        ENTERISBASERANK,
        FINISH
    }

}
