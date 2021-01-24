import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Provider;
import java.security.Security;

public class Server extends Thread
{
    public static void main(String args[])
    {
        int port = 35786;

        System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","123qwerty");
        //Security.addProvider("SUN");
        //System.setProperty("javax.net.debug","all");
        try {
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port);
            System.out.println("Echo Server Started & Ready to accept Client Connection");

            while (true) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                ServerThreads serverThreads = new ServerThreads(sslSocket, sslServerSocket);
                serverThreads.start();
            }
        }
        catch(Exception ex)
        {
            System.err.println("Error Happened : "+ex.toString());
        }
    }
}
