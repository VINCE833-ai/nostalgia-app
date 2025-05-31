package photoboothApp;

import javax.swing.*;
import java.awt.*;

public class buttons {
    private Runnable switchToPage2Callback;

    public void setSwitchCallback(Runnable callback) {
        this.switchToPage2Callback = callback;
    }

    public JPanel createResponsiveButtonsPanel() {
        JPanel panel = new JPanel(null);

        JButton button1 = new JButton("Take a photo");
        button1.setBounds(100, 200, 300, 55);
        button1.addActionListener(e -> {
            if (switchToPage2Callback != null) {
                switchToPage2Callback.run(); // trigger switch
            }
        });
        panel.add(button1);

        JButton button2 = new JButton("Import Photo");
        button2.setBounds(100, 300, 300, 55);
        panel.add(button2);

        return panel;
    }
}
