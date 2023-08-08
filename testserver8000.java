import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.Object;
import java.util.*;

public class testserver8000 {
    public static void main(String[] args) {
        try {
            // Create a datagram socket
            DatagramSocket socket = new DatagramSocket(8000);

            // Create a byte array to store the received data
            byte[] buffer = new byte[1024];
            Vector<packet> received_packets = new Vector<>();

            // Receive each packet one by one
            for (int i = 0; i < 4; i++) { // We know we are expecting 4 packets
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                socket.receive(dp);

                Object object = new Object();

                try {
                    object = convertToObject(dp.getData());
                } catch (Exception e) {
                    e.getStackTrace();
                }

                packet p = (packet) object;
                received_packets.add(p);

                // Print the received data
                System.out.println("\nReceived object: Packet Id = " + p.getPid() + ", Client ID = " + p.getCid()
                        + ", Source IP = " + p.getSip()
                        + ", Destination IP = " + p.getDip()
                        + ", Payload = " + p.getPayload()
                        + ", Message Name = " + p.getMessageName()
                        + ", Security Certficate = " + p.getSecurityCertificate() + "\n");
            }

            // Close the socket
            socket.close();

            Collections.sort(received_packets, new Comparator<packet>() {
                public int compare(packet p1, packet p2) {
                    return Integer.compare(p1.getPid(), p2.getPid());
                }
            });

            String received_message = "";

            for (packet p : received_packets) {
                received_message = received_message + p.getPayload();
            }

            System.out.println("\n\nMessage Received :-\n" + received_message + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Object convertToObject(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}
