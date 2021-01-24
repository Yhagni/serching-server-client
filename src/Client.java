import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.security.Security;
import java.util.Scanner;
//import com.sun.net.ssl.internal.ssl.Provider;

public class Client
{


    public static void main(String args[])
    {
        int serverPort = 35786;
        String serverName = "localhost";

        System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
        System.setProperty("javax.net.ssl.trustStorePassword","123qwerty");
        //Security.addProvider(new Provider());
        //System.setProperty("javax.net.debug","all");
        try
        {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(serverName,serverPort);
            DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());

            System.out.println(inputStream.readUTF());

            Scanner scanner = new Scanner(System.in);

            while (true)
            {
                System.out.print("Enter command:");
                String option = scanner.nextLine();
                outputStream.writeUTF(option);
                if (option.equals("register")) {
                    System.out.println("Login : ");
                    String login = scanner.nextLine();
                    System.out.println("Password : ");
                    String password = scanner.nextLine();
                    outputStream.writeUTF(login);
                    outputStream.writeUTF(password);
                    System.out.println(inputStream.readUTF());
                } else if (option.equals("close")) {
                    break;
                }else if (option.equals("login")) {
                    System.out.println("Login : ");
                    String login = scanner.nextLine();
                    System.out.println("Password : ");
                    String password = scanner.nextLine();
                    outputStream.writeUTF(login);
                    outputStream.writeUTF(password);
                    System.out.println(inputStream.readUTF());
                }else if (option.equals("remind")) {
                    System.out.println("Login : ");
                    String login = scanner.nextLine();
                    outputStream.writeUTF(login);
                    System.out.println(inputStream.readUTF());
                }



//                System.err.println(inputStream.readUTF());
//                if(messageToSend.equals("close"))
//                {
//                    break;
//                }
            }
        }
        catch(Exception ex)
        {
            System.err.println("Error: "+ex.toString());
        }
    }
}