package com.redecommunity.hub.spawn.storage;

import com.redecommunity.api.spigot.util.serialize.LocationSerialize;
import com.redecommunity.common.shared.databases.mysql.dao.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by @SrGutyerrez
 */
public class SpawnStorage extends Table {
    @Override
    public String getTableName() {
        return "server_spawn";
    }

    @Override
    public String getDatabaseName() {
        return "server";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER PRIMARY KEY AUTO_INCREMENT," +
                                "`location` VARCHAR(255)" +
                                ");",
                        this.getTableName()
                )
        );
    }

    @Override
    public <T> void insert(T object) {
        String query = String.format(
                "INSERT INTO %s " +
                        "(" +
                        "`location`" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "'%s'" +
                        ");",
                this.getTableName(),
                object
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <U, I> void update(U key, I value) {
        String query = String.format(
                "UPDATE %s SET `%s`='%s';",
                this.getTableName(),
                key,
                value
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            Integer affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) this.insert(value);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <T> T findOne() {
        String query = String.format(
                "SELECT * FROM %s",
                this.getTableName()
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            return resultSet.next() ? (T) LocationSerialize.toLocation(resultSet.getString("location")) : null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
