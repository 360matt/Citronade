package fr.i360matt.citronade;

public class Auth {
    private String host;
    private int port;
    private String user;
    private String password;
    private String database;

    public String getHost () {
        return host;
    }
    public void setHost (String host) {
        this.host = host;
    }

    public int getPort () {
        return port;
    }
    public void setPort (int port) {
        this.port = port;
    }

    public String getUser () {
        return user;
    }
    public void setUser (String user) {
        this.user = user;
    }

    public String getPassword () {
        return password;
    }
    public void setPassword (String password) {
        this.password = password;
    }

    public String getDatabase () {
        return database;
    }
    public void setDatabase (String database) {
        this.database = database;
    }

    public String getURI () {
        if (password != null && !password.isEmpty()) {
            return String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", host, port, database, user, password);
        } else {
            return String.format("jdbc:mysql://%s:%d/%s?user=%s", host, port, database, user);
        }
    }
}
