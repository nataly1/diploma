import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static ru.natasha.connect.ConnectWithServer.getConnectWithServer;

public class Main {

    public static void main (String[] args) {

        getConnectWithServer();

        //узнаем ip адресс компьютера в локальной сети
        try {
            InetAddress address = InetAddress.getLocalHost();
            String myLANIP = address.getHostAddress();

            System.out.println(myLANIP);
        } catch (UnknownHostException e) {
            System.out.println(e.getClass());
        }

    }
}
