package se.su.dsv.mastermcvoiceit.remote;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * Created by felix on 2017-11-27.
 */

public final class SSHUtil {

    public static String runCommand(String command, SSHConnDetails connDetails) {
        String hostname = connDetails.getHost();
        String username = connDetails.getUser();
        String password = connDetails.getPassword();
        int port = connDetails.getPort();
        StringBuilder ret = new StringBuilder();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Connection conn = new Connection(hostname, port);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);

            if (!isAuthenticated)
                throw new IOException("Authentication failed.");

            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                ret.append(line).append('\n');
            }

            sess.close();
            conn.close();
        } catch (IOException e) {
            System.out.println(hostname);
            System.out.println(username);
            System.out.println(password);
            e.printStackTrace(System.err);
            System.exit(2);
        }

        return ret.toString();
    }

}
