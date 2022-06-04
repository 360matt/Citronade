package fr.i360matt.citronade.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Insert {

    private final Connection connection;
    private final String tableName;
    private final Map<String, Object> keyVals;

    public Insert (Connection connection, String tableName, Map<String, Object> keyVals) {
        this.connection = connection;
        this.tableName = tableName;
        this.keyVals = keyVals;
    }

    public int execute () throws SQLException {
        if (keyVals.isEmpty())
            throw new IllegalArgumentException("No key-value pairs to insert");

        final StringJoiner columns = new StringJoiner(", ");
        final StringJoiner values = new StringJoiner(", ");
        final List<Object> params = new ArrayList<>();

        for (final Map.Entry<String, Object> entry : keyVals.entrySet()) {
            columns.add(entry.getKey());
            values.add("?");
            params.add(entry.getValue());
        }

        final String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, values);
        final PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++)
            statement.setObject(i + 1, params.get(i));
        return statement.executeUpdate();
    }


}
