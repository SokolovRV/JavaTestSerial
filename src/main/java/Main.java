import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SerialPort serial = new SerialPort(args[0]);
        try {
            serial.openPort();
            serial.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }

        if (serial.isOpened())
        {
            System.out.println("Serial is opened");
            System.out.println("Start test");
            for (int i = 1; i <= 10; i++) {
                try {
                    String s = "Send: " + i;
                    byte[] bytes_tx = {(byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0xac, (byte) 0x0A};
                    serial.writeBytes(bytes_tx);
                    System.out.println(s);
                    Thread.sleep(1000);
                    if (serial.getInputBufferBytesCount() > 0) {
                        byte[] bytes = serial.readBytes();
                        System.out.print("Receive: ");
                        for (int ii = 0; ii < bytes.length; ii++)
                            System.out.print(String.format("0x%02X", bytes[ii]) + " ");
                        System.out.println();
                    } else System.out.println("Not receive");
                } catch (SerialPortException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("End test");
    }
}
