package visuotech.com.kampus.attendance;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkAddress {

    public static void getInterfaces() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                System.out.println("Net interface: " + ni.getName() + " - " + ni.getDisplayName());

                Enumeration<InetAddress> e2 = ni.getInetAddresses();

                while (e2.hasMoreElements()) {
                    InetAddress ip = e2.nextElement();
                    System.out.println("IP address: " + ip.toString());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("List all network interfaces example");
        System.out.println();
        getInterfaces();
    }
}