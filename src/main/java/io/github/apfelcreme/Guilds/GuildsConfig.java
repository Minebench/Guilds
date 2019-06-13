package io.github.apfelcreme.Guilds;


import io.github.apfelcreme.Guilds.Guild.GuildLevel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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
 * @author Lord36 aka Apfelcreme on 24.04.2015.
 */
public class GuildsConfig {

    private File languageConfigFile;
    private YamlConfiguration languageConfig;
    private final Guilds plugin;

    public GuildsConfig(Guilds plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        plugin.saveDefaultConfig();

        reloadLanguageConfig();
    }

    /**
     * reloads the language file
     */
    public void reloadLanguageConfig() {
        plugin.saveResource("lang." + getLanguage() + ".yml", false);
        languageConfigFile = new File(plugin.getDataFolder(), "lang." + getLanguage() + ".yml");
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
    }

    public String getLanguage() {
        return plugin.getConfig().getString("language");
    }

    /**
     * returns the table name of the player table
     *
     * @return the table name of the player table
     */
    public String getPlayerTable() {
        return plugin.getConfig().getString("mysql.tables.player");
    }

    /**
     * returns the table name of the ranks table
     *
     * @return the table name of the ranks table
     */
    public String getRanksTable() {
        return plugin.getConfig().getString("mysql.tables.ranks");
    }

    /**
     * returns the table name of the ranks table
     *
     * @return the table name of the ranks table
     */
    public String getInvitesTable() {
        return plugin.getConfig().getString("mysql.tables.invites");
    }

    /**
     * returns the table name of the blackboard table
     *
     * @return the table name of the blackboard table
     */
    public String getBlackboardTable() {
        return plugin.getConfig().getString("mysql.tables.blackboard");
    }

    /**
     * returns the table name of the alliance table
     *
     * @return the table name of the alliance table
     */
    public String getAllianceTable() {
        return plugin.getConfig().getString("mysql.tables.alliance");
    }

    /**
     * returns the table name of the alliance-Invite table
     *
     * @return the table name of the alliance-Invite table
     */
    public String getAllianceInviteTable() {
        return plugin.getConfig().getString("mysql.tables.allianceInvites");
    }

    /**
     * returns the table name of the guild table
     *
     * @return the table name of the guild table
     */
    public String getGuildsTable() {
        return plugin.getConfig().getString("mysql.tables.guilds");
    }

    /**
     * returns the table name of the money log table
     *
     * @return the table name of the money log table
     */
    public String getMoneyLogTable() {
        return plugin.getConfig().getString("mysql.tables.moneyLog");
    }

    /**
     * returns the username of the mysql user
     *
     * @return the username of the mysql user
     */
    public String getMysqlUser() {
        return plugin.getConfig().getString("mysql.user");
    }

    /**
     * returns the mysql password
     *
     * @return the mysql password
     */
    public String getMysqlPassword() {
        return plugin.getConfig().getString("mysql.password");
    }

    /**
     * returns the database name
     *
     * @return the database name
     */
    public String getMysqlDatabase() {
        return plugin.getConfig().getString("mysql.database");
    }

    /**
     * returns the database url
     *
     * @return the database url
     */
    public String getMysqlUrl() {
        return plugin.getConfig().getString("mysql.url");
    }

    /**
     * returns the database parameters
     *
     * @return the database parameters
     */
    public String getMysqlParameters() {
        return plugin.getConfig().getString("mysql.parameters");
    }

    /**
     * returns the default message prefix
     *
     * @return the default message prefix
     */
    public String getMessagePrefix() {
        return getText("prefix.messages");
    }

    /**
     * returns the number of blackboardMessages that are loaded
     *
     * @return the number of blackboardMessages that are loaded
     */
    public int getBlackboardMessageLimit() {
        return plugin.getConfig().getInt("guilds.blackboard.limit");
    }

    /**
     * returns the number of elements that are printed per page in lists like '/guild roster'
     *
     * @return the number of elements that are printed per page in lists like '/guild roster'
     */
    public int getListsPageSize() {
        return plugin.getConfig().getInt("guilds.lists.pageSize");
    }

    /**
     * returns a List that contains all guild command strings
     *
     * @return a List that contains all guild command strings
     */
    public List<String> getGuildCommandStrings(boolean hasGuild) {
        List<String> ret = new ArrayList<String>();
        if (hasGuild) {
            for (Object o : languageConfig.getConfigurationSection("texts.help.guild.command.hasGuild").getValues(true).values()) {
                ret.add(o.toString());
            }
        } else {
            for (Object o : languageConfig.getConfigurationSection("texts.help.guild.command.hasNoGuild").getValues(true).values()) {
                ret.add(o.toString());
            }
        }
        for (Object o : languageConfig.getConfigurationSection("texts.help.guild.command.both").getValues(true).values()) {
            ret.add(o.toString());
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * returns a List that contains all alliance command strings
     *
     * @return a List that contains all alliance command strings
     */
    public List<String> getAllianceCommandStrings(boolean hasAlliance) {
        List<String> ret = new ArrayList<String>();
        if (hasAlliance) {
            for (Object o : languageConfig.getConfigurationSection("texts.help.alliance.command.hasAlliance").getValues(true).values()) {
                ret.add(o.toString());
            }
        } else {
            for (Object o : languageConfig.getConfigurationSection("texts.help.alliance.command.hasNoAlliance").getValues(true).values()) {
                ret.add(o.toString());
            }
        }
        for (Object o : languageConfig.getConfigurationSection("texts.help.alliance.command.both").getValues(true).values()) {
            ret.add(o.toString());
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * returns a List that contains all guildadmin command strings
     *
     * @return a List that contains all guildadmin command strings
     */
    public List<String> getGuildAdminCommandStrings() {
        List<String> ret = new ArrayList<String>();
        for (Object o : languageConfig.getConfigurationSection("texts.help.guildadmin").getValues(true).values()) {
            ret.add(o.toString());
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * returns the maximum length of a guilds name
     *
     * @return the maximum length of a guild name
     */
    public int getGuildNameLength() {
        return plugin.getConfig().getInt("guilds.guildNameLength");
    }

    /**
     * returns the maximum length of a guilds tag
     *
     * @return the maximum length of a guild tag
     */
    public int getGuildTagLength() {
        return plugin.getConfig().getInt("guilds.guildTagLength");
    }

    /**
     * returns the maximum length of a prefix
     *
     * @return the maximum length of a prefix
     */
    public int getPrefixLength() {
        return plugin.getConfig().getInt("guilds.prefixLength");
    }

    /**
     * returns whether cross server teleport is allowed
     *
     * @return true or false
     */
    public boolean isCrossServerTeleportAllowed() {
        return plugin.getConfig().getBoolean("guilds.crossServerTeleport");
    }

    /**
     * get the level data by its level integer
     *
     * @param level the guild level
     * @return the LevelData
     */
    public GuildLevel getLevelData(int level) {
        String name = plugin.getConfig()
                .getString("level." + level + ".name");
        if (name == null) {
            return null;
        }
        int playerLimit = plugin.getConfig()
                .getInt("level." + level + ".limit");
        double enchantmentCost = plugin.getConfig()
                .getDouble("level." + level + ".enchantmentCost");
        double doubleCraftProbability = plugin.getConfig()
                .getDouble("level." + level + ".doubleCraftProbability");
        double specialDropChance = plugin.getConfig()
                .getDouble("level." + level + ".specialDropChance");
        double furnaceExpGainRatio = plugin.getConfig()
                .getDouble("level." + level + ".furnaceExpGainRatio");
        double cost = plugin.getConfig()
                .getDouble("level." + level + ".upgradeCost");
        int players = plugin.getConfig()
                .getInt("level." + level + ".upgradePlayers");
        int expCost = plugin.getConfig()
                .getInt("level." + level + ".upgradeExpCost");
        HashMap<Material, Integer> materialRequirements = new HashMap<>();
        if (plugin.getConfig().isConfigurationSection("level." + level + ".upgradeMaterials")) {
            for (Map.Entry<String, Object> entry : plugin.getConfig()
                    .getConfigurationSection("level." + level + ".upgradeMaterials").getValues(false).entrySet()) {
                try {
                    materialRequirements.put(
                            Material.valueOf(entry.getKey().toUpperCase()), Integer.parseInt(entry.getValue().toString()));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().log(Level.WARNING, entry.getKey() + " is not a valid Material name!");
                }
            }
        }
        return new GuildLevel(level, name, playerLimit, enchantmentCost, doubleCraftProbability, specialDropChance,
                furnaceExpGainRatio, cost, players, expCost, materialRequirements);
    }

    /**
     * get the level data by its name
     *
     * @param name the name of the guild level
     * @return the LevelData
     */
    public GuildLevel getLevelData(String name) {
        int level = -1;

        ConfigurationSection levelSection = plugin.getConfig().getConfigurationSection("level");

        for (String lvlStr : levelSection.getKeys(false)) {
            if (name.equalsIgnoreCase(levelSection.getString(lvlStr + ".name"))) {
                try {
                    level = Integer.parseInt(lvlStr);
                    break;
                } catch (NumberFormatException e) {
                    plugin.getLogger().log(Level.SEVERE, "Config level " + lvlStr + " is not an integer?", e);
                }
            }
        }

        return level > -1 ? getLevelData(level) : null;
    }

    /**
     * get whether or not this material is eligible for a special drop
     *
     * @return true or false
     * @param item
     */
    public boolean isSpecialDrop(ItemStack item) {
        return plugin.getConfig().isSet("specialDrops." + item.getType().toString());
    }

    /**
     * Get the chance of a certain material to create an additional drop
     *
     * @return the chance as a double configured in the config, should be between 0.0 and 1.0
     * @param item
     */
    public double getSpecialDropChance(ItemStack item) {
        return plugin.getConfig().getDouble("specialDrops." + item.getType().toString());
    }

    /**
     * Get whether or not the plugin runs in debug mode
     */
    public boolean isDebugEnabled() {
        return plugin.getConfig().getBoolean("debug");
    }

    /**
     * returs a ChatColor from the chat input given (for /guild <...> <...> <Color>)
     *
     * @param color the color string e.g. 'gelb'
     * @return the matching ChatColor e.g ChatColor.YELLOW
     */
    public ChatColor parseColor(String color) {
        if (color == null)
            return null;
        for (ChatColor a : ChatColor.values()) {
            for (String s : languageConfig.getStringList("chatColors." + a.name() + ".aliases")) {
                if (s.equalsIgnoreCase(color.toUpperCase())) {
                    return a;
                }
            }
        }
        return null;
    }

    /**
     * returns a texty string
     *
     * @param key the config path
     * @param replacements Optional replacements. Use {index} in the message to address them.
     * @return the text
     */
    public String getText(String key, String... replacements) {
        String ret = (String) languageConfig.get("texts." + key);
        if (ret != null && !ret.isEmpty()) {
            for (int i = 0; i < replacements.length; i++) {
                ret = ret.replace("{" + i + "}", replacements[i]);
            }
            return GuildsUtil.replaceChatColors(ret);
        } else {
            return "Missing text node: " + key;
        }
    }

    /**
     * returns a string that is guild-colored
     *
     * @param key   the key
     * @param color the color
     * @param replacements Optional replacements. Use {index} in the message to address them.
     * @return a nice String
     */
    public String getColoredText(String key, ChatColor color, String... replacements) {
        String ret = (String) languageConfig.get("texts." + key);

        if (ret != null && !ret.isEmpty()) {
            for (int i = 0; i < replacements.length; i++) {
                ret = ret.replace("{" + i + "}", replacements[i]);
            }
            ret = ret.replace("&accent", getAccentColor(color).toString());
            ret = ret.replace("&mainColor", color.toString());
            return GuildsUtil.replaceChatColors(ret);
        } else {
            return "Missing text node: " + key;
        }
    }

    /**
     * Check whether or not the language config has a value for a certain key
     * @param key   The language key to check for
     * @return      <tt>true</tt> if there is an entry; <tt>false</tt> if not
     */
    public boolean hasText(String key) {
        return languageConfig.contains("texts." + key);
    }

    /**
     * Whether or not one must meet the material requirements while upgrading
     * @return true or false
     */
    public boolean requireMaterialForUpgrade() {
        return plugin.getConfig().getBoolean("upgrade.requireMaterial");
    }

    /**
     * Whether or not one must meet the experience requirements while upgrading
     * @return true or false
     */
    public boolean requireExpForUpgrade() {
        return plugin.getConfig().getBoolean("upgrade.requireExp");
    }

    /**
     * Whether or not one must meet the players requirements while upgrading
     * @return true or false
     */
    public boolean requirePlayersForUpgrade() {
        return plugin.getConfig().getBoolean("upgrade.requirePlayers");
    }

    /**
     * Whether or not one must meet the money requirements while upgrading
     * @return true or false
     */
    public boolean requireMoneyForUpgrade() {
        return plugin.getConfig().getBoolean("upgrade.requireMoney");
    }

    /**
     * is the enchantment cost being reduced by the guild level?
     *
     * @return true or false
     */
    public boolean isEnchantmentBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.cheaperEnchantment");
    }

    /**
     * is the furnace exp increased by the guild level?
     *
     * @return true or false
     */
    public boolean isMoreFurnaceExpBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.moreFurnaceExp");
    }

    /**
     * is the chance to double drop activated?
     *
     * @return true or false
     */
    public boolean isDoubleCraftingBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.doubleCrafting");
    }

    /**
     * is the chance to drop special items on block break activated??
     *
     * @return true or false
     */
    public boolean isSpecialDropBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.specialDrop");
    }

    /**
     * Whether or not this plugin should use bungeecord features like syncing
     * @return
     */
    public boolean useBungeeCord() {
        return plugin.getConfig().getBoolean("bungeecord");
    }

    /**
     * returns a matching accent color the given color
     *
     * @param color the original color
     * @return a matching accent color (e.g. ChatColor.GOLD will return ChatColor.YELLOW)
     */
    private static ChatColor getAccentColor(ChatColor color) {
        switch (color) {
            case BLACK:
                return ChatColor.DARK_GRAY;
            case DARK_BLUE:
                return ChatColor.BLUE;
            case DARK_GREEN:
                return ChatColor.GREEN;
            case DARK_AQUA:
                return ChatColor.AQUA;
            case DARK_RED:
                return ChatColor.RED;
            case DARK_PURPLE:
                return ChatColor.LIGHT_PURPLE;
            case GOLD:
                return ChatColor.YELLOW;
            case GRAY:
                return ChatColor.DARK_GRAY;
            case DARK_GRAY:
                return ChatColor.GRAY;
            case BLUE:
                return ChatColor.DARK_AQUA;
            case GREEN:
                return ChatColor.DARK_GREEN;
            case AQUA:
                return ChatColor.DARK_AQUA;
            case RED:
                return ChatColor.DARK_RED;
            case LIGHT_PURPLE:
                return ChatColor.DARK_PURPLE;
            case YELLOW:
                return ChatColor.GOLD;
            case WHITE:
                return ChatColor.GRAY;
            default:
                return ChatColor.GRAY;
        }
    }
}
