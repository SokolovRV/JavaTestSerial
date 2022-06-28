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
                    String tx_s = i + "";
                    String pinOn = "gpio write " + args[1] + " 1";
                    String pinOff = "gpio write " + args[1] + " 0";
                    if (args.length > 1) {
                        Process p = Runtime.getRuntime().exec(pinOn);
                        p.waitFor();
                        serial.writeString(tx_s);
                        Runtime.getRuntime().exec(pinOff);
                        p.waitFor();
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
