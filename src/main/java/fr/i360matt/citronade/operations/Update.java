package fr.i360matt.citronade.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Update {

    private final Connection connection;
    private final String tableName;
    private final Map<String, Object> query;
    private final Map<String, Object> update;

    public Update (Connection connection, String tableName, Map<String, Object> query, Map<String, Object> update) {
        this.connection = connection;
        this.tableName = tableName;
        this.query = query;
        this.update = update;
    }

    public int execute () throws SQLException {
        if (query.isEmpty() || update.isEmpty())
            throw new IllegalArgumentException("Query and update must not be empty");

        final StringJoiner queryString = new StringJoiner(" AND ");
        final List<Object> queryValues = new ArrayList<>();

        for (final Map.Entry<String, Object> entry : query.entrySet()) {
            queryString.add(entry.getKey() + " = ?");
            queryValues.add(entry.getValue());
        }

        final StringJoiner updateString = new StringJoiner(", ");
        final List<Object> updateValues = new ArrayList<>();
        for (final Map.Entry<String, Object> entry : update.entrySet()) {
            updateString.add(entry.getKey() + " = ?");
            updateValues.add(entry.getValue());
        }

        final String sql = String.format("UPDATE %s SET %s WHERE %s", tableName, updateString, queryString);
        final PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < queryValues.size(); i++)
            statement.setObject(i + 1, queryValues.get(i));
        for (int i = 0; i < updateValues.size(); i++)
            statement.setObject(i + queryValues.size() + 1, updateValues.get(i));

        return statement.executeUpdate();
    }

}
