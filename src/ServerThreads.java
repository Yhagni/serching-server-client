import org.xml.sax.SAXException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
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
            Finder finder = new Finder();

            outputStream.writeUTF("Welcome ");

            while (true) {
                String option = inputStream.readUTF();
                if (option.equals("register")) {
                    String login = inputStream.readUTF();
                    String password = inputStream.readUTF();
                    if (!xmlReader.isTheSameUser(login)) {
                        xmlReader.addElementToXmlFile(login, password);
                        outputStream.writeUTF("Your account was created successfully!");
                    } else {
                        outputStream.writeUTF("This user already exists!");
                    }
                } else if (option.equals("login")) {
                    String login = inputStream.readUTF();
                    String password = inputStream.readUTF();
                    if (xmlReader.logIn(login, password)) {
                        outputStream.writeUTF("You are logIn!");
                        isLoggedIn = true;
                    } else {
                        outputStream.writeUTF("Your login data are incorrect!");
                    }
                } else if (option.equals("remind")) {
                    String login = inputStream.readUTF();
                    if (!xmlReader.passwordReminder(login).equals("")) {
                        outputStream.writeUTF("This is Your password: " + xmlReader.passwordReminder(login));
                    } else {
                        outputStream.writeUTF("User with given login does not exist!");
                    }
                } else if (option.equals("search")) {
                    if (isLoggedIn) {
                        outputStream.writeUTF("Put phrase which are You searching");
                        String phrase = inputStream.readUTF();
                        outputStream.writeUTF("This is top 10 results: \n");
                        while (finder.howManyFoundPhrases(phrase) < 10)
                        {
                            outputStream.writeUTF(finder.findPhrase(phrase));
                        }
                        System.out.println("There was " + finder.howManyFoundPhrases(phrase) + " matches with the given phrase");
                    } else {
                        outputStream.writeUTF("You are not logged in!");
                    }
                } else if (option.equals("close")) {
                    outputStream.writeUTF("You are disconnected");
                    System.out.println("User disconnected");
                    outputStream.close();
                    inputStream.close();
                    sslSocket.close();
                    sslServerSocket.close();
                    break;
                }
            }
        } catch (IOException | TransformerException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            return;
        }
    }
}


