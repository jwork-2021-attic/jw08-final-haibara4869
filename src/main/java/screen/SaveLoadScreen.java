package screen;

import asciiPanel.AsciiPanel;
import java.io.File;
import java.util.Date;

public abstract class SaveLoadScreen implements Screen {

    public SaveLoadScreen(Screen preScreen) {
        this.preScreen = preScreen;
    }

    protected final Screen preScreen;
    protected int curSelected;
    protected File[] save = new File[] { new File("records/record1.txt"), new File("records/record2.txt"),
            new File("records/record3.txt") };

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("~.Record1.~", 4, 5);
        terminal.write("~.Record2.~", 4, 7);
        terminal.write("~.Record3.~", 4, 9);
        terminal.write((char)26, 3, 5 + curSelected * 2);
        for (int i = 0; i < 3; ++i) {
            if (save[i].exists())
                terminal.write(new Date(save[i].lastModified()).toString(), 9, 6 + 2 * i);
            else
                terminal.write("Empty", 9, 6 + 2 * i);
        }

    }
}