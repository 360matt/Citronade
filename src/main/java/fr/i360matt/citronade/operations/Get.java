package fr.i360matt.citronade.operations;

import fr.i360matt.citronade.operations.get.GetOptions;
import fr.i360matt.citronade.operations.get.ResultOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Get {

    private final Connection connection;
    private final String tableName;
    private final Map<String, Object> query;
    private GetOptions options;


    public Get (Connection connection, String tableName, Map<String, Object> query) {
        this.connection = connection;
        this.tableName = tableName;
        this.query = query;
        this.options = new GetOptions();
    }

    public ResultOption execute () throws SQLException {
        if (this.query.isEmpty())
            throw new IllegalArgumentException("Query is empty");

        String select = (this.options.getProjection() == null) ? "*" : String.join(",", this.options.getProjection());

        StringJoiner where = new StringJoiner(" AND ");
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Object> entry : this.query.entrySet()) {
            where.add(entry.getKey() + "=?");
            values.add(entry.getValue());
        }

        String order = (this.options.getOrder() == null) ? "" : " ORDER BY " + String.join(",", this.options.getOrder());
        String limit = (this.options.getLimit() == -1) ? "" : " LIMIT " + this.options.getLimit();
        String skip = (this.options.getSkip() == -1) ? "" : " OFFSET " + this.options.getSkip();

        String query = String.format("SELECT %s FROM %s WHERE %s %s %s %s", select, this.tableName, where, order, limit, skip);

        PreparedStatement statement = this.connection.prepareStatement(query);
        for (int i = 0; i < values.size(); i++) {
            statement.setObject(i + 1, values.get(i));
        }

        return new ResultOption(statement.executeQuery());
    }

    public GetOptions options () {
        return this.options;
    }

    public void setOptions (GetOptions options) {
        this.options = options;
    }


}
