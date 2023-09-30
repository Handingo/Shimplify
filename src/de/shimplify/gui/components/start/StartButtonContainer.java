package de.shimplify.gui.components.start;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

public final class StartButtonContainer extends JPanel {

	public StartButtonContainer() {
		super(new BorderLayout(), true);
		setPreferredSize(new Dimension(getPreferredSize().width, 50));
	}
}