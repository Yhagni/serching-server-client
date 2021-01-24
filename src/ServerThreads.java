import org.xml.sax.SAXException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerThreads extends Thread {

    private SSLSocket sslSocket;
    private SSLServerSocket sslServerSocket;
    private XmlReader xmlReader;
    private boolean isLoggedIn = false;

    public ServerThreads(SSLSocket sslSocket, SSLServerSocket sslServerSocket) throws IOException, SAXException, ParserConfigurationException {
        this.sslSocket = sslSocket;
        this.sslServerSocket = sslServerSocket;
        xmlReader = new XmlReader();
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());

            outputStream.writeUTF("Welcome ");

            while (true) {
                String option = inputStream.readUTF();

                if (option.equals("register")) {
                    String login = inputStream.readUTF();
                    String password = inputStream.readUTF();
                    if(!xmlReader.isTheSameUser(login)){
                        xmlReader.addElementToXmlFile(login, password);
                        outputStream.writeUTF("Your account was created successfully!");
                    }else{
                        outputStream.writeUTF("This user already exists!");
                    }
                }
                else  if (option.equals("close")) {
                    System.out.println("User disconnected!");
                }
                else  if(option.equals("login")) {
                    String login = inputStream.readUTF();
                    String password = inputStream.readUTF();
                    if(xmlReader.logIn(login, password)) {
                        xmlReader.addElementToXmlFile(login, password);
                        outputStream.writeUTF("You are logIn!");
                        isLoggedIn = true;
                    }else {
                        outputStream.writeUTF("Your login data are incorrect!");
                    }
                }
                else if (option.equals("remind")) {
                    String login = inputStream.readUTF();
                    if (!xmlReader.passwordReminder(login).equals("")) {
                        outputStream.writeUTF("This is Your password: " + xmlReader.passwordReminder(login));
                    }else {
                        outputStream.writeUTF("User with given login does not exist!");
                }
                }

//                if (recivedMessage.equals("close")) {
//                    outputStream.writeUTF("Bye");
//                    outputStream.close();
//                    inputStream.close();
//                    sslSocket.close();
//                    sslServerSocket.close();
//                    break;
//                } else {
//                    outputStream.writeUTF("You Said : " + recivedMessage);
//                }
            }
        } catch (IOException | TransformerException e) {
            return;
        }
    }
}


