package ru.ifmo.prog.lab8;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class DatabaseClient<T> {
    private final static String HOST = "localhost";
    private final static String ENDPOINT = "jdbc:postgresql://" + HOST + ":5432/studs";
    private final static String DRIVER = "org.postgresql.Driver";
    private final static String USER = "s******";
    private final static String PASSWORD = "******";
    private Connection connection = null;
    private static DatabaseClient instance = null;

    public static DatabaseClient getInstance() {
        return instance == null ? instance = new DatabaseClient() : instance;
    }

    private DatabaseClient()  {
        try {
            SqlHelper.registration(DRIVER);
            connection = SqlHelper.connection(ENDPOINT, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isTableExist(String tableName) {
        String query = format("SELECT * FROM %s LIMIT 1",
                normalizeTableName(tableName));

        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int createTable(String tableName, Class itemClass) throws SQLException {
        String query = format("CREATE TABLE %s (key VARCHAR(4096) PRIMARY KEY, %s);",
                normalizeTableName(tableName),
                Arrays
                        .stream(itemClass.getDeclaredFields())
                        .map(field -> {
                            String type = "VARCHAR(4096)";
                            if (field.getType() == Double.class) {
                                type = "double precision";
                            } else if (field.getType() == Integer.class) {
                                type = "integer";
                            } else if (field.getType() == Boolean.class) {
                                type = "boolean";
                            } else if (field.getType() == OffsetDateTime.class) {
                                type = "TIMESTAMP";
                            } else if (field.getType() == LabColor.class) {
                                type = "VARCHAR(50)";
                            } else if (field.getType() == String.class) {
                                type = "VARCHAR(4096)";
                            }

                            return String.format("%s %s", field.getName(), type);
                        })
                        .collect(Collectors.joining( ", " ))
        );
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }

    public int dropTable(String tableName) throws SQLException {
        String query = format("DROP TABLE %s CASCADE CONSTRAINTS", normalizeTableName(tableName));
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }

    public int createItem(String tableName, String newKey, Object newItem) throws SQLException {
        Class itemClass = newItem.getClass();
        String query = format("INSERT INTO %s (key, %s) VALUES ('%s', %s)",
                normalizeTableName(tableName),
                getFieldsNames(itemClass),
                newKey,
                getFieldsValues(newItem)
        );
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }

    }

    public Hashtable<String, T> readAllItems(String tableName, Class itemClass) throws SQLException {
        String query = format("SELECT key, %s FROM %s",
                getFieldsNames(itemClass),
                normalizeTableName(tableName));
        Hashtable<String, T> hashtable = new Hashtable<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                String key = resultSet.getString(1);

                hashtable.put(key, (T) getPopulatedItem(itemClass, resultSet));
            }

        }

        return hashtable;
    }

    public T readItem(String tableName, String key, Class itemClass) throws SQLException {
        String query = format("SELECT key, %s FROM %s WHERE key='%s'",
                getFieldsNames(itemClass),
                normalizeTableName(tableName),
                key);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return (T) getPopulatedItem(itemClass, resultSet);
            }
        }

        return null;
    }

    public int updateItem(String tableName, String key, Object item) throws SQLException {
        Class itemClass = item.getClass();
        String query = format("UPDATE %s SET %s WHERE key='%s'",
                normalizeTableName(tableName),
                Arrays.stream(itemClass.getDeclaredFields())
                        .map(field -> field.getName() + "=" + normalizeFieldValue(field, item))
                        .collect(Collectors.joining( ", " )),
                key
        );
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }

    public int deleteAllItems(String tableName) throws SQLException {
        String query = format("TRUNCATE TABLE %s",
                normalizeTableName(tableName));
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }

    public int deleteItem(String tableName, String key) throws SQLException {
        String query = format("DELETE FROM %s WHERE key='%s'",
                normalizeTableName(tableName),
                key);
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }

    private static Object getPopulatedItem(Class itemClass, ResultSet resultSet) {
        try {
            Object item = itemClass.newInstance();
            Arrays
                    .stream(itemClass.getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            if (field.getType() == Double.class) {
                                field.set(item, resultSet.getDouble(field.getName()));
                            } else if (field.getType() == Integer.class) {
                                field.set(item, resultSet.getInt(field.getName()));
                            } else if (field.getType() == Boolean.class) {
                                field.set(item, resultSet.getBoolean(field.getName()));
                            } else if (field.getType() == OffsetDateTime.class) {
                                field.set(item, OffsetDateTime.ofInstant(Instant.ofEpochMilli(resultSet.getTimestamp(field.getName()).getTime()), ZoneId.of("UTC")));
                            } else if (field.getType() == LabColor.class) {
                                field.set(item, new LabColor(resultSet.getString(field.getName())));
                            } else if (field.getType() == String.class) {
                                field.set(item, resultSet.getString(field.getName()));
                            }
                            field.setAccessible(false);
                        } catch (IllegalAccessException | SQLException e) {
                            e.printStackTrace();
                        }
                    });
            return item;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String normalizeTableName(String tableName) {
        return tableName.replaceAll("[.]", "_");
    }

    private static String getFieldsNames(Class itemClass) {
        return Arrays
                .stream(itemClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.joining( ", " ));
    }

    private static String getFieldsValues(Object item) {
        return Arrays
                .stream(item.getClass().getDeclaredFields())
                .map(field -> normalizeFieldValue(field, item))
                .collect(Collectors.joining( ", " ));
    }

    private static String normalizeFieldValue(Field field, Object item) {
        String value = "NULL";
        try {
            field.setAccessible(true);
            if (field.getType() == Double.class
                    || field.getType() == Integer.class
                    || field.getType() == Boolean.class) {
                value = field.get(item).toString();
            } else if (field.getType() == OffsetDateTime.class
                    || field.getType() == LabColor.class
                    || field.getType() == String.class) {
                value = "'" + field.get(item).toString() + "'";
            }
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            return "NULL";
        }

        return value;
    }
}
