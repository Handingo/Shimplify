package de.shimplify.gui.components.destination;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.util.Button;

public final class DestinationSelectionButton extends Button {

	public DestinationSelectionButton(final Theme theme, final TextInputSmall input) {
		super(theme, "./config/destination_icon.png", 38, 38);
		setBorder(BorderFactory.createEmptyBorder());

		final DestinationSelection destinationSelection;

		try {
			final LookAndFeel lf = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			destinationSelection = new DestinationSelection();
			UIManager.setLookAndFeel(lf);
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not initialize destination selection");
		}

		addActionListener(event -> {
			if (destinationSelection.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
				input.setText(destinationSelection.getSelectedFile().getAbsolutePath());
			}
		});
	}
}