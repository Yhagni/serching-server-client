import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.Scanner;
import java.security.Provider;
import java.security.Security;

public class Client
{
    public static void main(String[] args)
    {

        boolean isLoggedIn = false;
        int serverPort = 35786;
        String serverName = "localhost";

        System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
        System.setProperty("javax.net.ssl.trustStorePassword","123qwerty");
        Provider provider = Security.getProvider("SUN");
        Security.addProvider(provider);
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
                }else if (option.equals("login")) {
                    if(!isLoggedIn)
                    {
                        System.out.println("Login : ");
                        String login = scanner.nextLine();
                        System.out.println("Password : ");
                        String password = scanner.nextLine();
                        outputStream.writeUTF(login);
                        outputStream.writeUTF(password);
                        String correctData = inputStream.readUTF();
                        if(correctData.equals("You are logged in!")){
                            System.out.println(correctData);
                            isLoggedIn = true;
                        }
                        else
                        {
                            System.out.println(correctData);
                        }
                    }
                    else {
                        System.out.println(inputStream.readUTF());
                    }

                }else if (option.equals("remind")) {
                    System.out.println("Login : ");
                    String login = scanner.nextLine();
                    outputStream.writeUTF(login);
                    outputStream.writeUTF(login);
                    System.out.println(inputStream.readUTF());
                }else if (option.equals("search")) {
                    String response = inputStream.readUTF();
                    System.out.println(response);
                    if(!response.equals("You are not logged in!")) {
                        String phrase = scanner.nextLine();
                        outputStream.writeUTF(phrase);
                        System.out.println(inputStream.readUTF());
                        int size = Integer.parseInt(inputStream.readUTF());
                        if (size > 10) {
                            System.out.println("These are top 10 of them: ");
                        }
                        System.out.println(inputStream.readUTF());
                    }
                }else if (option.equals("close")) {
                    System.out.println(inputStream.readUTF());
                    break;
                }
                else {
                    System.out.println("There is no such command");
                }

            }
        }
        catch(Exception ex)
        {
            System.out.println("Server refused connection, sorry!");
        }
    }
}