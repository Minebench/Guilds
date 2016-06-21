package io.github.apfelcreme.Guilds;


import io.github.apfelcreme.Guilds.Guild.GuildLevel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

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
        plugin.saveResource("lang.de.yml", false);

        reloadLanguageConfig();
    }

    /**
     * reloads the language file
     */
    public void reloadLanguageConfig() {
        languageConfigFile = new File(plugin.getDataFolder() + "/lang.de.yml");
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
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
        return plugin.getConfig().getInt("guilds.blackboard.limit", 5);
    }

    /**
     * returns the number of elements that are printed per page in lists like '/guild roster'
     *
     * @return the number of elements that are printed per page in lists like '/guild roster'
     */
    public int getListsPageSize() {
        return plugin.getConfig().getInt("guilds.lists.pageSize", 10);
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
        return plugin.getConfig().getInt("guilds.guildNameLength", 30);
    }

    /**
     * returns the maximum length of a guilds tag
     *
     * @return the maximum length of a guild tag
     */
    public int getGuildTagLength() {
        return plugin.getConfig().getInt("guilds.guildTagLength", 4);
    }

    /**
     * returns the maximum length of a prefix
     *
     * @return the maximum length of a prefix
     */
    public int getPrefixLength() {
        return plugin.getConfig().getInt("guilds.prefixLength", 25);
    }

    /**
     * returns whether cross server teleport is allowed
     *
     * @return true or false
     */
    public boolean isCrossServerTeleportAllowed() {
        return plugin.getConfig().getBoolean("guilds.crossServerTeleport", false);
    }

    /**
     * the materials that are required to upgrade the guild on the given level
     *
     * @param level the guild level + 1
     * @return the materials that are needed for the upgrade
     */
    public GuildLevel getLevelData(Integer level) {
        String name = plugin.getConfig()
                .getString("level." + level.toString() + ".name");
        if (name == null) {
            return null;
        }
        Integer playerLimit = plugin.getConfig()
                .getInt("level." + level.toString() + ".limit");
        Double enchantmentCost = plugin.getConfig()
                .getDouble("level." + level.toString() + ".enchantmentCost");
        Double doubleCraftProbability = plugin.getConfig()
                .getDouble("level." + level.toString() + ".doubleCraftProbability");
        Double specialDropChance = plugin.getConfig()
                .getDouble("level." + level.toString() + ".specialDropChance");
        Double furnaceExpGainRatio = plugin.getConfig()
                .getDouble("level." + level.toString() + ".furnaceExpGainRatio");
        Double cost = plugin.getConfig()
                .getDouble("level." + level.toString() + ".upgradeCost");
        Integer expCost = plugin.getConfig()
                .getInt("level." + level.toString() + ".upgradeExpCost");
        HashMap<Material, Integer> materialRequirements = new HashMap<Material, Integer>();
        for (Map.Entry entry : plugin.getConfig()
                .getConfigurationSection("level." + level.toString() + ".upgradeMaterials").getValues(true).entrySet()) {
            materialRequirements.put(
                    Material.valueOf(entry.getKey().toString()), Integer.parseInt(entry.getValue().toString()));
        }
        return new GuildLevel(level, name, playerLimit, enchantmentCost, doubleCraftProbability, specialDropChance,
                furnaceExpGainRatio, cost, expCost, materialRequirements);
    }

    /**
     * generates a random Drop with the weights configured in the config.yml
     *
     * @return a Material that will be dropped for the player in the BlockBreakEvent
     */
    public Material getNewRandomDrop() {
        Map<String, Object> input = plugin.getConfig().getConfigurationSection("specialDrops").getValues(true);
        Double total = 0.0;
        Map<Material, Double> chances = new HashMap<Material, Double>();
        Map<Material, Double> cumulatedChances = new HashMap<Material, Double>();
        for (Map.Entry<String, Object> material : input.entrySet()) {
            chances.put(Material.valueOf(material.getKey()), Double.parseDouble(material.getValue().toString()));
            cumulatedChances.put(Material.valueOf(material.getKey()), total);
            total += Double.parseDouble(material.getValue().toString());
        }
        Double random = Math.random() * total;
        for (Map.Entry<Material, Double> material : cumulatedChances.entrySet()) {
            if ((random > cumulatedChances.get(material.getKey())) && (random < (cumulatedChances.get(material.getKey()) + chances.get(material.getKey())))) {
                return material.getKey();
            }
        }
        return null;
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
     * @return the text
     */
    public String getText(String key) {
        String ret = (String) languageConfig.get("texts." + key);
        if (ret != null && !ret.isEmpty()) {
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
     * @return a nice String
     */
    public String getColoredText(String key, ChatColor color) {
        String ret = (String) languageConfig.get("texts." + key);

        if (ret != null && !ret.isEmpty()) {
            ret = ret.replaceAll("&accent", getAccentColor(color).toString());
            ret = ret.replaceAll("&mainColor", color.toString());
            return GuildsUtil.replaceChatColors(ret);
        } else {
            return "Missing text node: " + key;
        }
    }

    /**
     * is the enchantment cost being reduced by the guild level?
     *
     * @return true or false
     */
    public boolean isEnchantmentBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.cheaperEnchantment", false);
    }

    /**
     * is the furnace exp increased by the guild level?
     *
     * @return true or false
     */
    public boolean isMoreFurnaceExpBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.moreFurnaceExp", false);
    }

    /**
     * is the chance to double drop activated?
     *
     * @return true or false
     */
    public boolean isDoubleCraftingBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.doubleCrafting", false);
    }

    /**
     * is the chance to drop special items on block break activated??
     *
     * @return true or false
     */
    public boolean isSpecialDropBonusActivated() {
        return plugin.getConfig().getBoolean("bonus.specialDrop", false);
    }

    /**
     * Whether or not this plugin should use bungeecord features like syncing
     * @return
     */
    public boolean useBungeeCord() {
        return plugin.getConfig().getBoolean("bungeecord", true);
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
