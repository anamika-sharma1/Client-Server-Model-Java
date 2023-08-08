import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.Object;
import java.util.Vector;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class testserver5678 {
    public static void main(String[] args) {
        try {
            // Create a datagram socket
            DatagramSocket socket = new DatagramSocket(5678);
            DatagramSocket sendSocket = new DatagramSocket();

            // Create a byte array to store the received data
            byte[] buffer = new byte[1024];
            Vector<DatagramPacket> packetsToSend = new Vector<>();
            // Receive each packet one by one
            for (int i = 0; i < 2; i++) { // We know we are expecting 2 packets
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                socket.receive(dp);

                Object object = new Object();

                try {
                    object = convertToObject(dp.getData());
                } catch (Exception e) {
                    e.getStackTrace();
                }

                packet p = (packet) object;

                // Print the received data
                System.out.println("\nReceived object: Packet Id = " + p.getPid() + ", Client ID = " + p.getCid()
                        + ", Source IP = " + p.getSip()
                        + ", Destination IP = " + p.getDip()
                        + ", Payload = " + p.getPayload()
                        + ", Message Name = " + p.getMessageName()
                        + ", Security Certficate = " + p.getSecurityCertificate() + "\n");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream ts = new ObjectOutputStream(baos);
                ts.writeObject(p);
                byte[] objectData = baos.toByteArray();
                DatagramPacket dp_new = new DatagramPacket(objectData, objectData.length, InetAddress.getLocalHost(),
                        8000);
                packetsToSend.add(dp_new);
            }

            for (DatagramPacket dp : packetsToSend) {
                sendSocket.send(dp);
            }
            // Close the socket
            socket.close();
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
