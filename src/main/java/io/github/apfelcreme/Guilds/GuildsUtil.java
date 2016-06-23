package io.github.apfelcreme.Guilds;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

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
 * @author Lord36 aka Apfelcreme on 19.05.2015.
 */
public class GuildsUtil {

    /**
     * returns whether a string only contains numbers
     *
     * @param string the string to be checked
     * @return true or false
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("([0-9])*", string);
    }

    /**
     * joins a number of strings and places a seperator between them
     * -> taken from StringUtils to reduce the number of dependencies
     *
     * @param array     the array of strings
     * @param separator the seperator
     * @return a joined string
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        } else {
            if (separator == null) {
                separator = "";
            }

            int noOfItems = array.length;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        buf.append(separator);
                    }

                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

    /**
     * replaces all chat codes like '&f' by their representative ChatColor values
     *
     * @param input the input string
     * @return a new string that actually works
     */
    public static String replaceChatColors(String input) {
        if (input == null) {
            return null;
        }
        for (int i = 0; i < ChatColor.values().length; i++) {
            // the enum contains no element called "ChatColor.OBFUSCATED",
            // but ChatColor.values()[] does !?
            if (!ChatColor.values()[i].name()
                    .equalsIgnoreCase("obfuscated")) {
                String replace = "&"
                        + ChatColor.values()[i].getChar();
                input = input.replace(
                        replace,
                        ChatColor.valueOf(
                                ChatColor.values()[i].name().toUpperCase())
                                .toString());
            }
        }
        return input;
    }

    /**
     * straps a string of all color chars and chatcolor values
     *
     * @param string a string
     * @return a string free of color tags
     */
    public static String strip(String string) {
        return ChatColor.stripColor(replaceChatColors(string));
    }

    /**
     * counts the number of a specific material in an inventory
     *
     * @param inventory the inventory
     * @param material  the material
     * @param countSpecial should we count items that have meta/enchantments/are damaged?
     * @return the number of a specific material in an inventory
     */
    public static int countItems(Inventory inventory, Material material, boolean countSpecial) {
        int amount = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == material) {
                if (countSpecial || (!itemStack.hasItemMeta() && itemStack.getDurability() == 0)) {
                    amount += itemStack.getAmount();
                }
            }
        }
        return amount;
    }

    /**
     * Remove a specific material from an inventory
     *
     * @param inventory the inventory
     * @param material  the material
     * @param count the amount to remove
     * @param removeSpecial should we remove items that have meta/enchantments/are damaged?
     */
    public static void removeItems(Inventory inventory, Material material, int count, boolean removeSpecial) {
        for (int i = 0; i < inventory.getContents().length && count > 0; i++) {
            ItemStack itemStack = inventory.getContents()[i];
            if (itemStack != null && itemStack.getType() == material) {
                if (removeSpecial || (!itemStack.hasItemMeta() && itemStack.getDurability() == 0)) {
                    if (itemStack.getAmount() > count) {
                        itemStack.setAmount(itemStack.getAmount() - count);
                        inventory.setItem(i, itemStack);
                        count = 0;
                    } else {
                        count -= itemStack.getAmount();
                        inventory.clear(i);
                    }
                }
            }
        }
    }

    /**
     * Get the exp you need for a certain level. Uses the 1.8 exp math
     *
     * Function from Essentials
     *
     * @param level The level
     * @return The exp
     */
    public static int getExpAtLevel(final int level) {
        if (level <= 15) {
            return (2 * level) + 7;
        }
        if ((level >= 16) && (level <= 30)) {
            return (5 * level) - 38;
        }
        return (9 * level) - 158;

    }

    /**
     * Get the total exp a player has
     *
     * This method is required because the Bukkit Player.getTotalExperience() method shows exp that has been 'spent'.
     *
     * Without this people would be able to use exp and then still sell it.
     *
     * Function from Essentials
     *
     * @param player the player to get the total exp from
     * @return The total exp the player has
     */
    public static int getTotalExperience(final Player player) {
        int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    /**
     * Humanize a material name. E.g turn STONE_AXE into Stone Axe
     * @return the humanized material name
     */
    public static String humanize(Material type) {
        String[] parts = type.toString().split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].substring(0, 1) + parts[i].substring(1).toLowerCase();
        }
        return join(parts, " ");
    }
}
