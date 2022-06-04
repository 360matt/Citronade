package fr.i360matt.citronade.operations.get;

import fr.i360matt.citronade.operations.Get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetOptions {

    private int limit = -1;
    private int skip = -1;

    private List<String> order;
    private List<String> projection;

    public GetOptions limit (int limit) {
        this.limit = limit;
        return this;
    }

    public GetOptions skip (int skip) {
        this.skip = skip;
        return this;
    }

    public GetOptions order (String column, int direction) {
        if (this.order == null)
            this.order = new ArrayList<>();
        this.order.add(column + " " + (direction == 1 ? "ASC" : "DESC"));
        return this;
    }

    public GetOptions projection (String... columns) {
        this.projection = new ArrayList<>();
        Collections.addAll(this.projection, columns);
        return this;
    }

    public int getLimit () {
        return limit;
    }

    public int getSkip () {
        return skip;
    }

    public List<String> getOrder () {
        return order;
    }

    public List<String> getProjection () {
        return projection;
    }

}
