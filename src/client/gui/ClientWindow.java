package client.gui;

import javax.swing.JFrame;

/**
 * The top-level container for the GUI.
 */
public class ClientWindow extends JFrame
{
    public ClientWindow()
    {
        initUI();
    }

    private void initUI()
    {
        setTitle("Octothorpe # - The Game");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
