package io.github.apfelcreme.Guilds.Command.Guild.Session;

import io.github.apfelcreme.Guilds.Guild.Rank;

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
public class EditRankSession {

    private Rank rank;
    private CurrentEditState currentState;

    private String name = null;
    private boolean canInvite = false;
    private boolean canKick = false;
    private boolean canPromote = false;
    private boolean canDisband = false;
    private boolean canUpgrade = false;
    private boolean canWithdrawMoney = false;
    private boolean canUseBlackboard = false;
    private boolean canDoDiplomacy = false;

    private boolean isBaseRank = false;

    public EditRankSession(Rank rank) {
        this.rank = rank;
        this.name = rank.getName();
        this.currentState = CurrentEditState.ENTERNAME;
    }

    /**
     * moves the current session one step forward
     */
    public void nextStep() {
        switch (currentState) {
            case ENTERNAME:
                currentState = CurrentEditState.ENTERCANINVITE;
                break;
            case ENTERCANINVITE:
                currentState = CurrentEditState.ENTERCANKICK;
                break;
            case ENTERCANKICK:
                currentState = CurrentEditState.ENTERCANPROMOTE;
                break;
            case ENTERCANPROMOTE:
                currentState = CurrentEditState.ENTERCANDISBAND;
                break;
            case ENTERCANDISBAND:
                currentState = CurrentEditState.ENTERCANUPGRADE;
                break;
            case ENTERCANUPGRADE:
                currentState = CurrentEditState.ENTERCANWITHDRAWMONEY;
                break;
            case ENTERCANWITHDRAWMONEY:
                currentState = CurrentEditState.ENTERCANUSEBLACKBOARD;
                break;
            case ENTERCANUSEBLACKBOARD:
                currentState = CurrentEditState.ENTERCANDODIPLOMACY;
                break;
            case ENTERCANDODIPLOMACY:
                currentState = CurrentEditState.ENTERISBASERANK;
                break;
            case ENTERISBASERANK:
                currentState = CurrentEditState.FINISH;
                break;
            case FINISH:
                break;
        }
    }

    public void setName(String name) {
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

    public String getName() {
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


    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "EditRankSession{" +
                "rank=" + rank +
                ", currentState=" + currentState +
                ", name='" + name + '\'' +
                ", canInvite=" + canInvite +
                ", canKick=" + canKick +
                ", canPromote=" + canPromote +
                ", canDisband=" + canDisband +
                ", canUpgrade=" + canUpgrade +
                ", canWithdrawMoney=" + canWithdrawMoney +
                ", canUseBlackboard=" + canUseBlackboard +
                ", canDoDiplomacy=" + canDoDiplomacy +
                ", isBaseRank=" + isBaseRank +
                '}';
    }

    /**
     * returns the current state of the Session
     *
     * @return the CurrentEditState
     */
    public CurrentEditState getCurrentState() {
        return currentState;
    }

    public enum CurrentEditState {
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
