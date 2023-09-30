package de.shimplify.gui.components.destination;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import de.shimplify.config.wrapper.Theme;

public final class TextInputSmall extends JTextField {

	public TextInputSmall(final Theme theme) {
		setBackground(theme.contentColor());
		setForeground(theme.fontColor());
		setCaretColor(theme.fontColor());
		setBorder(BorderFactory.createLineBorder(theme.contentColor(), 10));
	}
}