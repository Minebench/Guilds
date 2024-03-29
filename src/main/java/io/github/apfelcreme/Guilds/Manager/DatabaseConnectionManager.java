package io.github.apfelcreme.Guilds.Manager;

import com.zaxxer.hikari.HikariDataSource;
import io.github.apfelcreme.Guilds.Guilds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Guilds
 * Copyright (C) 2015 Lord36 aka Apfelcreme
 * <p/>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme on 24.04.2015.
 */
public class DatabaseConnectionManager {

    private Guilds plugin;
    private HikariDataSource ds;

    public DatabaseConnectionManager(Guilds plugin) {
        this.plugin = plugin;
        initConnection();
    }

    /**
     * initializes the database connection
     *
     * @return
     */
    private Connection initConnection() {
        if (plugin.getGuildsConfig().getMysqlDatabase() == null || plugin.getGuildsConfig().getMysqlDatabase().isEmpty()) {
            return null;
        } else {
            ds = new HikariDataSource();
            String dataSourceClassName = tryDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            if (dataSourceClassName == null) {
                dataSourceClassName = tryDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            }
            if (dataSourceClassName != null) {
                plugin.getLogger().log(Level.INFO, "Using " + dataSourceClassName + " database source");
                ds.setDataSourceClassName(dataSourceClassName);
            }

            if (dataSourceClassName == null) {
                String driverClassName = tryDriverClassName("org.mariadb.jdbc.Driver");
                if (driverClassName == null) {
                    driverClassName = tryDriverClassName("com.mysql.cj.jdbc.Driver");
                }
                if (driverClassName == null) {
                    driverClassName = tryDriverClassName("com.mysql.jdbc.Driver");
                }

                if (driverClassName != null) {
                    plugin.getLogger().log(Level.INFO, "Using " + driverClassName + " database driver");
                    ds.setDriverClassName(driverClassName);
                } else {
                    throw new RuntimeException("Could not find database driver or data source class! Plugin wont work without a database!");
                }
            }

            String url = "jdbc:mysql://" + plugin.getGuildsConfig().getMysqlUrl() + "/" + plugin.getGuildsConfig().getMysqlDatabase() + plugin.getGuildsConfig().getMysqlParameters();
            if (dataSourceClassName != null) {
                ds.addDataSourceProperty("url", url);
            } else {
                ds.setJdbcUrl(url);
            }
            ds.setUsername(plugin.getGuildsConfig().getMysqlUser());
            ds.setPassword(plugin.getGuildsConfig().getMysqlPassword());
            ds.setConnectionTimeout(5000);
            initTables();
        }
        return null;
    }

    private String tryDriverClassName(String className) {
        try {
            Class.forName(className).newInstance();
            return className;
        } catch (Exception ignored) {}
        return null;
    }

    private String tryDataSourceClassName(String className) {
        try {
            Class.forName(className);
            return className;
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * creates the database and tables
     */
    private void initTables() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getMysqlDatabase());
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getAllianceTable() + " (" +
                    "allianceId INTEGER AUTO_INCREMENT, " +
                    "alliance VARCHAR(90) NOT NULL UNIQUE, " +
                    "tag VARCHAR(12) NOT NULL, " +
                    "founded BIGINT, " +
                    "color VARCHAR(20) NOT NULL DEFAULT 'DARK_GREEN', " +
                    "PRIMARY KEY (allianceId));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getGuildsTable() + " (" +
                    "guildId INTEGER AUTO_INCREMENT, " +
                    "guild VARCHAR(90) NOT NULL UNIQUE, " +
                    "tag VARCHAR(12) NOT NULL, " +
                    "color VARCHAR(20) NOT NULL DEFAULT 'DARK_GREEN', " +
                    "balance DOUBLE DEFAULT 0, " +
                    "exp BIGINT DEFAULT 0, " +
                    "level TINYINT DEFAULT 1, " +
                    "founded BIGINT, " +
                    "allianceId INTEGER, " +
                    "guildHomeX DOUBLE, " +
                    "guildHomeY DOUBLE, " +
                    "guildHomeZ DOUBLE, " +
                    "guildHomeYaw FLOAT DEFAULT 0, " +
                    "guildHomePitch FLOAT DEFAULT 0, " +
                    "guildHomeWorld VARCHAR(50), " +
                    "guildHomeServer VARCHAR(50), " +
                    "FOREIGN KEY (allianceId) references " + plugin.getGuildsConfig().getAllianceTable() + " (allianceId) ON DELETE SET NULL, " +
                    "PRIMARY KEY (guildId));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getRanksTable() + " (" +
                    "rankId INTEGER AUTO_INCREMENT, " +
                    "rankName VARCHAR(50) NOT NULL, " +
                    "canInvite TINYINT(1) default 0, " +
                    "canKick TINYINT(1) default 0, " +
                    "canPromote TINYINT(1) default 0, " +
                    "canDisband TINYINT(1) default 0, " +
                    "canUpgrade TINYINT(1) default 0, " +
                    "canWithdrawMoney TINYINT(1) default 0, " +
                    "canUseBlackboard TINYINT(1) default 0, " +
                    "canDoDiplomacy TINYINT(1) default 0, " +
                    "isBaseRank TINYINT(1) default 0, " +
                    "isLeader TINYINT(1) default 0, " +
                    "guildId INTEGER, " +
                    "FOREIGN KEY (guildId) REFERENCES " + plugin.getGuildsConfig().getGuildsTable() + " (guildId) ON DELETE CASCADE, " +
                    "PRIMARY KEY (rankId));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getPlayerTable() + " (" +
                    "uuid VARCHAR(36) NOT NULL UNIQUE, " +
                    "playerName VARCHAR(50) NOT NULL, " +
                    "prefix VARCHAR(75), " +
                    "guildId INTEGER, " +
                    "rankId INTEGER, " +
                    "joined BIGINT, " +
                    "lastSeen BIGINT, " +
                    "FOREIGN KEY (guildId) REFERENCES " + plugin.getGuildsConfig().getGuildsTable() + " (guildId) ON DELETE SET NULL, " +
                    "FOREIGN KEY (rankId) REFERENCES " + plugin.getGuildsConfig().getRanksTable() + " (rankId) ON DELETE SET NULL, " +
                    "PRIMARY KEY (uuid));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getBlackboardTable() + " (" +
                    "messageId BIGINT auto_increment not null, " +
                    "player VARCHAR(36) NOT NULL, " +
                    "guildId INTEGER, " +
                    "message VARCHAR(255), " +
                    "timestamp TIMESTAMP, " +
                    "cleared TINYINT DEFAULT 0, " +
                    "FOREIGN KEY (player) REFERENCES " + plugin.getGuildsConfig().getPlayerTable() + " (uuid), " +
                    "FOREIGN KEY (guildId) REFERENCES " + plugin.getGuildsConfig().getGuildsTable() + " (guildId) ON DELETE CASCADE, " +
                    "PRIMARY KEY (messageId));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getInvitesTable() + " (" +
                    "inviteId INTEGER AUTO_INCREMENT, " +
                    "player VARCHAR(36) NOT NULL, " +
                    "targetPlayer VARCHAR(36) NOT NULL, " +
                    "guildId INTEGER NOT NULL, " +
                    "status SMALLINT DEFAULT 0, " +
                    "FOREIGN KEY (guildId) REFERENCES " + plugin.getGuildsConfig().getGuildsTable() + " (guildId) ON DELETE CASCADE, " +
                    "FOREIGN KEY (player) REFERENCES " + plugin.getGuildsConfig().getPlayerTable() + " (uuid), " +
                    "FOREIGN KEY (targetPlayer) REFERENCES " + plugin.getGuildsConfig().getPlayerTable() + " (uuid), " +
                    "PRIMARY KEY (inviteId));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getAllianceInviteTable() + " (" +
                    "allianceInviteId INTEGER AUTO_INCREMENT, " +
                    "allianceId INTEGER NOT NULL, " +
                    "guildId INTEGER NOT NULL, " +
                    "status SMALLINT DEFAULT 0, " +
                    "FOREIGN KEY (guildId) REFERENCES " + plugin.getGuildsConfig().getGuildsTable() + " (guildId) ON DELETE CASCADE, " +
                    "FOREIGN KEY (allianceId) REFERENCES " + plugin.getGuildsConfig().getAllianceTable() + " (allianceId) ON DELETE CASCADE, " +
                    "PRIMARY KEY (allianceInviteId));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + plugin.getGuildsConfig().getMoneyLogTable() + " (" +
                    "guildId INTEGER NOT NULL, " +
                    "player VARCHAR(36) NOT NULL, " +
                    "balanceChange DOUBLE NOT NULL, " +
                    "timestamp TIMESTAMP, " +
                    "FOREIGN KEY (guildId) REFERENCES " + plugin.getGuildsConfig().getGuildsTable() + " (guildId) ON DELETE CASCADE, " +
                    "FOREIGN KEY (player) REFERENCES " + plugin.getGuildsConfig().getPlayerTable() + " (uuid));");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTables();
    }

    private void updateTables() {
        try (Connection connection = getConnection()) {
            connection.createStatement().executeUpdate("ALTER TABLE " +
                    plugin.getGuildsConfig().getGuildsTable() +
                    " ADD guildHomePitch FLOAT DEFAULT 0 AFTER guildHomeZ," +
                    " ADD guildHomeYaw FLOAT DEFAULT 0 AFTER guildHomeZ");
        } catch (SQLException e) {
            if (!e.getMessage().contains("Duplicate column name")) {
                e.printStackTrace();
            }
        }
    }

    /**
     * returns the database connection to work with
     *
     * @return a Connection
     */
    public Connection getConnection() throws SQLException {
        return ds != null ? ds.getConnection() : null;
    }

}
