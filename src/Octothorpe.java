import java.io.File;

import game.OctothorpeGame;

public class Octothorpe
{
    public static void main(String[] args)
    {
        OctothorpeGame game = new OctothorpeGame();
        game.addPlayer("rafael");
        game.run();
    }
}
