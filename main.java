import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.net.SocketException;

public class main {

    public static void main(String[] args) throws SocketException {
        System.out.println("| LCF5 ; Launch Control FH5 0.1 K.K |");
        PacketDemon demon = new PacketDemon();
        logger.log("Starting UDP packet demon");
        logger.log("Starting key manager");
        logger.log("Starting misc services");
        demon.start();

        try {
            logger.log("Starting key listener");
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

    }
}
