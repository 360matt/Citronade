package fr.i360matt.citronade.operations.get;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ResultOption {

    private final ResultSet resultSet;

    public ResultOption (ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Map<String, Object> getOneRaw ()  {
        try {
            Map<String, Object> result = new HashMap<>();
            resultSet.next();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                result.put(columnName, resultSet.getObject(columnName));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getOneRaw (@NotNull Consumer<Map<String, Object>> consumer)  {
        Map<String, Object> result = getOneRaw();
        consumer.accept(result);
    }

    public List<Map<String, Object>> getAllRaw () {
        try {
            List<Map<String, Object>> result = new java.util.ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    row.put(columnName, resultSet.getObject(columnName));
                }
                result.add(row);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAllRaw (@NotNull Consumer<List<Map<String, Object>>> consumer)  {
        List<Map<String, Object>> result = getAllRaw();
        consumer.accept(result);
    }

    public <T> T getOne (@NotNull Class<T> clazz)  {
        try {
            T result = clazz.newInstance();
            resultSet.next();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                Object value = resultSet.getObject(columnName);

                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.getName().equals(columnName)) {
                        field.setAccessible(true);
                        field.set(result, value);
                    }
                }
            }
            return result;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void getOne (@NotNull Class<T> clazz, @NotNull Consumer<T> consumer)  {
        T result = getOne(clazz);
        consumer.accept(result);
    }

    public <T> List<T> getAll (@NotNull Class<T> clazz) {
        try {
            List<T> result = new java.util.ArrayList<>();
            while (resultSet.next()) {
                T row = clazz.newInstance();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Object value = resultSet.getObject(columnName);
                    for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                        if (field.getName().equals(columnName)) {
                            field.setAccessible(true);
                            field.set(row, value);
                        }
                    }
                }
                result.add(row);
            }
            return result;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void getAll (@NotNull Class<T> clazz, @NotNull Consumer<List<T>> consumer) {
        List<T> result = getAll(clazz);
        consumer.accept(result);
    }

}
