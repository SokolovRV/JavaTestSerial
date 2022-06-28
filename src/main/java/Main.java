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
                    String tx_s = i + "1234567";
                    String pinOn = "gpio write " + args[1] + " 1";
                    String pinOff = "gpio write " + args[1] + " 0";
                    byte[] bytes_tx = tx_s.getBytes();
                    if (args.length > 1) {
                        Runtime r = Runtime.getRuntime();
                        Process p = r.exec(pinOn);
                        p.waitFor();
                        serial.writeBytes(bytes_tx);
                        while(serial.getOutputBufferBytesCount() > 0);
                        r.exec(pinOff);
                    } else serial.writeString(tx_s);
                    System.out.println(s);

                    Thread.sleep(1000);

                    if (serial.getInputBufferBytesCount() > 0) {
                        byte[] bytes = serial.readBytes();
                        System.out.println("Receive: "+ new String(bytes));
                    }
                } catch (SerialPortException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("End test");
    }
}
