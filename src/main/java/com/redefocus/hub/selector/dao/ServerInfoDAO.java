package com.redefocus.hub.selector.dao;

import com.google.common.collect.Sets;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.hub.selector.data.ServerInfo;
import com.redefocus.hub.selector.manager.ServerInfoManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class ServerInfoDAO extends Table {
    @Override
    public String getTableName() {
        return "server_server_info";
    }

    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`npc_id` INTEGER NOT NULL," +
                                "`slot` INTEGER NOT NULL," +
                                "`server_id` INTEGER NOT NULL," +
                                "`active` BOOLEAN NOT NULL," +
                                "`material_id` INTEGER NOT NULL," +
                                "`description` TEXT" +
                                ");",
                        this.getTableName()
                )
        );
    }

    @Override
    public <T> Set<T> findAll() {
        Set<T> serverInfoList = Sets.newConcurrentHashSet();

        String query = String.format(
                "SELECT * FROM %s;",
                this.getTableName()
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                ServerInfo serverInfo = ServerInfoManager.toServerInfo(resultSet);

                serverInfoList.add((T) serverInfo);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return serverInfoList;
    }
}
