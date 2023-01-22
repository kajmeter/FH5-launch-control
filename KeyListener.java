import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {
    public static String output;
    public void nativeKeyPressed(NativeKeyEvent e) {
        output = NativeKeyEvent.getKeyText(e.getKeyCode());
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if (output.equals(NativeKeyEvent.getKeyText(e.getKeyCode()))){
            output ="";
        }

    }

    public void nativeKeyTyped(NativeKeyEvent e) {}

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        }

        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new KeyListener());
    }
}
