package de.shimplify.gui.components.destination;

import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.shimplify.config.Config;
import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.util.LabelBorder;
import de.shimplify.gui.util.UI;

public final class DestinationInputContainer extends JPanel {

	public DestinationInputContainer(final Theme theme, final TextInputSmall input) {
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(new LabelBorder(" Destination", theme.fontColor()));
		setBackground(theme.backgroundColor());

		final File defaultDirectory = new File(Config.get().getDefaults().getProperty("destination", "./"));
		final File directory = defaultDirectory.isDirectory() ? defaultDirectory : new File("./");
		final boolean isAbsolutePath = directory.isAbsolute() || (directory.getPath().length() > 1 && directory.getPath().charAt(1) == ':');
		final String path = isAbsolutePath ? directory.getPath() : directory.getAbsolutePath().replace(".", "");

		input.setText(path.endsWith("\\") ? path.substring(0, path.length() - 1) : path);

		final DestinationSelectionButton button = new DestinationSelectionButton(theme, input);

		add(input);
		add(UI.getSpacer());
		add(button);
	}
}