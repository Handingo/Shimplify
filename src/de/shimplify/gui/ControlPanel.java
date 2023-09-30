package de.shimplify.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.components.destination.DestinationInputContainer;
import de.shimplify.gui.components.destination.TextInputSmall;
import de.shimplify.gui.components.input.LinkInputContainer;
import de.shimplify.gui.components.input.TextInput;
import de.shimplify.gui.components.start.StartButton;
import de.shimplify.gui.components.start.StartButtonContainer;
import de.shimplify.gui.util.UI;

public final class ControlPanel extends JPanel {
	public ControlPanel(final Theme theme) {
		super(true);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(UI.OUTER_BORDER);
		setBackground(theme.backgroundColor());

		final TextInput linkInput = new TextInput(theme);
		final LinkInputContainer linkInputContainer = new LinkInputContainer(theme, linkInput);

		final TextInputSmall destinationInput = new TextInputSmall(theme); // get default location
		final DestinationInputContainer destinationInputContainer = new DestinationInputContainer(theme, destinationInput);

		final StartButtonContainer startButtonContainer = new StartButtonContainer();
		startButtonContainer.add(new StartButton(theme, "Start", linkInput, destinationInput));

		add(linkInputContainer);
		add(UI.getSpacer());
		add(destinationInputContainer);
		add(UI.getSpacer());
		add(startButtonContainer);
	}
}