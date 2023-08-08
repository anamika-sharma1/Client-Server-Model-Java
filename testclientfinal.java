import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;
import java.io.Serializable;
import java.util.Scanner;
import java.util.Vector;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

class packet implements Serializable {
    private int packet_id;
    private String client_id;
    private String source_ip;
    private String destination_ip;
    private String payload;
    private String message_name;
    private String security_certificate;

    public packet(int pid, String cid, String sip, String dip, String pl, String msg, String sc) {
        this.packet_id = pid;
        this.client_id = cid;
        this.source_ip = sip;
        this.destination_ip = dip;
        this.payload = pl;
        this.message_name = msg;
        this.security_certificate = sc;
    }

    public int getPid() {
        return packet_id;
    }

    public String getCid() {
        return client_id;
    }

    public String getSip() {
        return source_ip;
    }

    public String getDip() {
        return destination_ip;
    }

    public String getPayload() {
        return payload;
    }

    public String getMessageName() {
        return message_name;
    }

    public String getSecurityCertificate() {
        return security_certificate;
    }
}

public class testclientfinal {
    public static void main(String[] args) {
        try {

            Scanner scanner = new Scanner(System.in);
            Vector<packet> packets_vector = new Vector<>();

            System.out.println("Enter Message to be sent :");
            String message = "", temp = "";

            while (true) {
                temp = "";
                temp = scanner.nextLine();
                if (temp.equals("0")) {
                    // System.out.println("yes");
                    break;
                } else {
                    message = message + temp + "\n";
                }
                // System.out.println(message + "\n" + temp);
            }

            // System.out.println("You entered:");
            // System.out.println(message + message.length());

            int ml = message.length();

            int ps = 0;
            System.out.println("\nEnter Packet Size :");
            ps = scanner.nextInt();

            int no_of_packets = (ml % ps) == 0 ? ml / ps : (ml / ps) + 1;
            System.out.println(no_of_packets);

            int i = 0, j = 0;
            for (i = 1; i <= no_of_packets; i++) {
                if (j + ps > ml) {
                    packet p = new packet(i, "dell1234", "127.0.0.1", "127.0.0.1", message.substring(j, ml),
                            "Message from Anamika Sharma", "Null");
                    packets_vector.add(p);
                    break;
                } else {
                    packet p = new packet(i, "dell1234", "127.0.01", "127.0.0.1", message.substring(j, j + ps),
                            "Message from Anamika Sharma", "Null");
                    j = j + ps;
                    packets_vector.add(p);
                }
            }

            int count = 1;
            Vector<DatagramPacket> packetsToSend = new Vector<>();
            for (packet p : packets_vector) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream ts = new ObjectOutputStream(baos);
                ts.writeObject(p);
                byte[] objectData = baos.toByteArray();
                DatagramPacket dp;

                // Create a DatagramPacket with the object data
                if (count == 1) {
                    dp = new DatagramPacket(objectData, objectData.length, InetAddress.getLocalHost(),
                            1234);
                    count = count * -1;
                } else {
                    dp = new DatagramPacket(objectData, objectData.length, InetAddress.getLocalHost(),
                            5678);
                    count = count * -1;
                }

                packetsToSend.add(dp);
            }

            DatagramSocket socket1 = new DatagramSocket();
            DatagramSocket socket2 = new DatagramSocket();

            // Send each packet one by one
            for (DatagramPacket dp : packetsToSend) {
                if (dp.getPort() == 1234) {
                    // System.out.println("port 1");
                    socket1.send(dp);
                } else if (dp.getPort() == 5678) {
                    // System.out.println("port 2");
                    socket2.send(dp);
                }
            }

            // Close the socket
            socket1.close();
            socket2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}