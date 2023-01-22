import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class PacketDemon extends Thread implements NativeKeyListener {
    int delay = 10;
    int rpm_delay = 4000;
    private DatagramSocket socket;
    private boolean running;
    private byte[] content = new byte[1500];
    private boolean LC_status = false;
    private KeyPresser presser = new KeyPresser(delay);

    public PacketDemon() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run(){
        logger.log("Thread started");
        running = true;
        presser.start();
        while (running){
            ////////////////////////////////UDP////////////////////////////////////w
            DatagramPacket packet = new DatagramPacket(content,content.length);
            try {socket.receive(packet);} catch (IOException e) {System.out.println("PACKET LOSS");}

            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            byte CurrentEngineRpm[] = Arrays.copyOfRange(packet.getData(),16,20);

            packet = new DatagramPacket(CurrentEngineRpm,CurrentEngineRpm.length,address,port);

            double calculated_output = byteArrayToFloat(packet.getData());


            ////////////////////////////////UDP////////////////////////////////////

            /////////////////////////ACTION / LISTENERS////////////////////////////
            GlobalScreen.addNativeKeyListener(new KeyListener());


            if(calculated_output!=0.0){
                if(!KeyListener.output.equals("")){
                            //on press action//

                    if(KeyListener.output=="F1"){
                        logger.log("Launch control engaged");
                        LC_status = true;

                        try {
                            Robot brake_snsr = new Robot();
                            logger.log("Holding handbrake");
                            brake_snsr.keyPress(KeyEvent.VK_SPACE);

                    } catch (AWTException e) {
                        e.printStackTrace();
                    }

                    }

                    if(KeyListener.output=="F2"){
                        logger.log("Launch control disengaged");
                        LC_status = false;
                        try {
                            Robot brake_snsr = new Robot();
                            logger.log("Releasing handbrake");
                            brake_snsr.keyRelease(KeyEvent.VK_SPACE);

                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                    }

                    if(calculated_output-500>rpm_delay){
                        LC_status = false;
                        logger.log("Launched at :"+calculated_output+" RPM ; TICK","STATISTIC");
                        try {
                            Robot brake_snsr = new Robot();
                            brake_snsr.keyRelease(KeyEvent.VK_SPACE);

                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                    }

                    if(KeyListener.output=="1"){rpm_delay = 1000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="1"){rpm_delay = 1000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="2"){rpm_delay = 2000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="3"){rpm_delay = 3000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="4"){rpm_delay = 4000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="5"){rpm_delay = 5000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="6"){rpm_delay = 6000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="7"){rpm_delay = 7000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="8"){rpm_delay = 8000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="9"){rpm_delay = 9000;logger.log("Changed to "+rpm_delay+" RPM");
                    }else if(KeyListener.output=="0"){rpm_delay = 0000;logger.log("Changed to "+rpm_delay+" RPM");
                    }
                          //on press action //
                }

                if (LC_status){

                    if(calculated_output<rpm_delay){
                        if(presser.done){
                            presser = new KeyPresser(delay);
                            presser.start();
                        }
                    }
                }
            }/////////////////////////ACTION / LISTENERS////////////////////////////
        }
    }


    float byteArrayToFloat(byte[] bytes) {
        int intBits =
                bytes[3] << 24 | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
        return Float.intBitsToFloat(intBits);
    }
}
