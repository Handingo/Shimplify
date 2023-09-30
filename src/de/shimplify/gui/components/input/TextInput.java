package de.shimplify.gui.components.input;

import java.awt.BorderLayout;

import javax.swing.JTextArea;

import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.util.UI;

public final class TextInput extends JTextArea {
	public TextInput(final Theme theme) {
		super(16, Integer.MAX_VALUE);
		setLayout(new BorderLayout());
		setMargin(UI.INNER_MARGIN);
		setBackground(theme.contentColor());
		setForeground(theme.fontColor());
		setCaretColor(theme.fontColor());
		setWrapStyleWord(true);
		setLineWrap(true);
	}
}