package fr.i360matt.citronade;

import com.mysql.cj.jdbc.MysqlDataSource;
import fr.i360matt.citronade.operations.get.GetOptions;
import fr.i360matt.citronade.operations.get.ResultOption;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Citronade implements Closeable {

    private final Connection connection;
    private final DataStore dataStore;

    public Citronade (final String uri) {
        MysqlDataSource datas = new MysqlDataSource();
        datas.setURL(uri);
        this.connection = this.createConnection(datas);
        this.dataStore = new DataStore(this);
    }

    public Citronade (@NotNull final Auth auth) {
        this(auth.getURI());
    }

    public Citronade (@NotNull final Consumer<Auth> consumer) {
        Auth auth = new Auth();
        consumer.accept(auth);
        MysqlDataSource datas = new MysqlDataSource();
        datas.setURL(auth.getURI());
        this.connection = this.createConnection(datas);
        this.dataStore = new DataStore(this);
    }

    private Connection createConnection (@NotNull MysqlDataSource datas) {
        try {
            datas.setServerTimezone("UTC");
            return datas.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection () {
        if (this.connection == null)
            throw new RuntimeException("Connection is closed");
        return this.connection;
    }

    @Override
    public void close () {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert (final String table, @NotNull Consumer<Map<String, Object>> consumer) {
        Map<String, Object> values = new HashMap<>();
        consumer.accept(values);
        return this.dataStore.insert(table, values);
    }

    public int update (final String table, @NotNull BiConsumer<Map<String, Object>, Map<String, Object>> consumer) {
        Map<String, Object> query = new HashMap<>();
        Map<String, Object> update = new HashMap<>();
        consumer.accept(query, update);
        return this.dataStore.update(table, query, update);
    }

    public int delete (final String table, @NotNull Consumer<Map<String, Object>> consumer) {
        Map<String, Object> query = new HashMap<>();
        consumer.accept(query);
        return this.dataStore.delete(table, query);
    }

    public ResultOption get (final String table, @NotNull Consumer<Map<String, Object>> consumer) {
        Map<String, Object> query = new HashMap<>();
        GetOptions options = new GetOptions();
        consumer.accept(query);
        return this.dataStore.get(table, query, options);
    }

    public ResultOption get (final String table, @NotNull BiConsumer<Map<String, Object>, GetOptions> consumer) {
        Map<String, Object> query = new HashMap<>();
        GetOptions options = new GetOptions();
        consumer.accept(query, options);
        return this.dataStore.get(table, query, options);
    }








}
