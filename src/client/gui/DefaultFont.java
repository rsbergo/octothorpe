package client.gui;

import java.awt.Font;

/**
 * Default fonts used in the GUI
 */
public class DefaultFont
{
    public static Font getPlain()
    {
        return getPlain(12);
    }

    public static Font getPlain(int size)
    {
        return new Font("Monospaced", Font.PLAIN, size);
    }

    public static Font getBold()
    {
        return getBold(12);
    }

    public static Font getBold(int size)
    {
        return new Font("Monospaced", Font.BOLD, size);
    }
}
