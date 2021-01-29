import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Provider;
import java.security.Security;


public class Server extends Thread
{
    public static void main(String[] args)
    {
        int port = 35786;

        System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","123qwerty");
        Provider provider = Security.getProvider("SUN");
        Security.addProvider(provider);
        try {
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port);
            System.out.println("Server started and is waiting for clients!");

            while (true) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                ServerThreads serverThreads = new ServerThreads(sslSocket, sslServerSocket);
                serverThreads.start();
            }
        }
        catch(Exception ex)
        {
            System.out.println("User ended connection");
        }
    }
}
