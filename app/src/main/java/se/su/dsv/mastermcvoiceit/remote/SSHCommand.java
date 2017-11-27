package se.su.dsv.mastermcvoiceit.remote;

/**
 * Created by felix on 2017-11-27.
 */

public class SSHCommand {
    private String HOST = "192.168.2.3";
    private String USER = "";


    public class SSHConnDetails {
        private String host, user, password;

        public SSHConnDetails(String host, String user, String password) {
            this.host = host;
            this.user = user;
            this.password = password;
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
    }
}
