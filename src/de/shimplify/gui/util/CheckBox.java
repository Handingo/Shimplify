package de.shimplify.gui.util;

import javax.swing.JCheckBox;

import de.shimplify.config.wrapper.Theme;

public final class CheckBox extends JCheckBox {

	public CheckBox(final String name, final Theme theme) {
		super(name, true);

		setBorder(UI.ITEM_BORDER);
		setBackground(theme.backgroundColor());
		setForeground(theme.fontColor());
		setFocusPainted(false);
		setOpaque(true);
	}
}