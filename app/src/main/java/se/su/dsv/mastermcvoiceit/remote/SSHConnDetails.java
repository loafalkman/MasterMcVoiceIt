package se.su.dsv.mastermcvoiceit.remote;

/**
 * Created by felix on 2017-11-27.
 */

public class SSHConnDetails {
    private String host, user, password;
    private int port;

    public SSHConnDetails(String host, String user, String password, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }
}
