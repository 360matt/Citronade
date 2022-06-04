package fr.i360matt.citronade;

import fr.i360matt.citronade.annots.Save;
import fr.i360matt.citronade.annots.Table;
import fr.i360matt.citronade.operations.Delete;
import fr.i360matt.citronade.operations.Get;
import fr.i360matt.citronade.operations.Insert;
import fr.i360matt.citronade.operations.Update;
import fr.i360matt.citronade.operations.get.GetOptions;
import fr.i360matt.citronade.operations.get.ResultOption;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class DataStore {

    private final Citronade citronade;
    private final HashMap<Class<?>, String> classToTable;

    public DataStore (Citronade citronade) {
        this.citronade = citronade;
        this.classToTable = new HashMap<>();
    }

    public int insert (@NotNull String table, @NotNull Map<String, Object> values) {
        Insert insert = new Insert(citronade.getConnection(), table, values);
        try {
            return insert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert (@NotNull Object object) {
       /* String table = this.classToTable.get(object.getClass());
        if (table == null) {
            throw new IllegalArgumentException("No table for class " + object.getClass().getName());
        }
        this.save(object, table);*/

        Table tableAnnot = object.getClass().getAnnotation(Table.class);
        if (tableAnnot == null)
            throw new IllegalArgumentException("No table for class " + object.getClass().getName());
        this.insert(tableAnnot.name(), object);
    }

    private int insert (String table, @NotNull Object object) {
        Map<String, Object> values = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Save.class)) {
                field.setAccessible(true);
                try {
                    values.put(field.getName(), field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Insert get = new Insert(citronade.getConnection(), table, values);
        try {
            return get.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update (final String table, @NotNull final Map<String, Object> query, final Map<String, Object> values) {
        Update update = new Update(citronade.getConnection(), table, query, values);
        try {
            return update.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int delete (final String table, @NotNull final Map<String, Object> query) {
        Delete delete = new Delete(citronade.getConnection(), table, query);
        try {
            return delete.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultOption get (final String table, @NotNull final Map<String, Object> query, @NotNull GetOptions options) {
        Get get = new Get(citronade.getConnection(), table, query);
        get.setOptions(options);
        try {
            return get.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
