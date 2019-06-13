package io.github.apfelcreme.Guilds.Guild;

import org.bukkit.Material;

import java.util.HashMap;

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
public class GuildLevel {

    private int level;
    private String name;
    private int playerLimit;
    private double enchantmentCost;
    private double doubleCraftProbability;
    private double specialDropChance;
    private double furnaceExpGainRatio;
    private double cost;
    private int players;
    private int expCost;
    private HashMap<Material, Integer> materialRequirements;

    public GuildLevel(Integer level, String name, int playerLimit, double enchantmentCost,
                      double doubleCraftProbability, double specialDropChance, double furnaceExpGainRatio, double cost,
                      int players, int expCost, HashMap<Material, Integer> materialRequirements) {
        this.level = level;
        this.name = name;
        this.playerLimit = playerLimit;
        this.enchantmentCost = enchantmentCost;
        this.doubleCraftProbability = doubleCraftProbability;
        this.specialDropChance = specialDropChance;
        this.furnaceExpGainRatio = furnaceExpGainRatio;
        this.cost = cost;
        this.players = players;
        this.expCost = expCost;
        this.materialRequirements = materialRequirements;
    }

    /**
     * returns the level
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * returns the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * returns the player limit
     *
     * @return the player limit
     */
    public int getPlayerLimit() {
        return playerLimit;
    }

    /**
     * returns the enchantment cost modifier
     *
     * @return the enchantment cost modifier
     */
    public double getEnchantmentCost() {
        return enchantmentCost;
    }

    /**
     * returns the chance for double drops
     *
     * @return the chance for double drops
     */
    public double getDoubleCraftProbability() {
        return doubleCraftProbability;
    }

    /**
     * returns the chance for special drops
     *
     * @return the chance for special drops
     */
    public double getSpecialDropChance() {
        return specialDropChance;
    }

    /**
     * returns the furnace exp modifier
     *
     * @return the furnace exp modifier
     */
    public double getFurnaceExpGainRatio() {
        return furnaceExpGainRatio;
    }

    /**
     * returns the upgrade money cost
     *
     * @return the upgrade money cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * returns the upgrade players amount
     *
     * @return the upgrade players amount
     */
    public int getRequiredPlayers() {
        return players;
    }

    /**
     * returns the upgrade exp cost
     *
     * @return the upgrade exp cost
     */
    public int getExpCost() {
        return expCost;
    }

    /**
     * returns the material requirements
     *
     * @return the material requirements
     */
    public HashMap<Material, Integer> getMaterialRequirements() {
        return materialRequirements;
    }
}
