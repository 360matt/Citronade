package fr.i360matt.citronade.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Delete {

    private final Connection connection;
    private final String tableName;
    private final Map<String, Object> query;

    public Delete (Connection connection, String tableName, Map<String, Object> query) {
        this.connection = connection;
        this.tableName = tableName;
        this.query = query;
    }

    public int execute () throws SQLException {
        final StringJoiner queryString = new StringJoiner(" AND ");
        final List<Object> queryValues = new ArrayList<>();

        for (final Map.Entry<String, Object> entry : query.entrySet()) {
            queryString.add(entry.getKey() + " = " + entry.getValue());
            queryValues.add(entry.getValue());
        }

        final String sql = String.format("DELETE FROM %s WHERE %s", tableName, queryString);
        final PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < queryValues.size(); i++)
            statement.setObject(i + 1, queryValues.get(i));
        return statement.executeUpdate();
    }

}
