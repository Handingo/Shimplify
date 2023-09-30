package de.shimplify.gui.components.destination;

import javax.swing.JFileChooser;

import de.shimplify.config.Config;

public final class DestinationSelection extends JFileChooser {

	public DestinationSelection() {
		super(Config.get().getDefaults().getProperty("destination", "./"));
		setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		setDialogTitle("Select the destination directory");
	}
}