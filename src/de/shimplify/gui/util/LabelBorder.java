package de.shimplify.gui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public final class LabelBorder extends TitledBorder {

	private final Insets insets;

	public LabelBorder(final String title, final Color titleColor) {
		super(title);
		this.titleColor = titleColor;
		border = BorderFactory.createEmptyBorder();
		insets = new Insets(25, 0, 0, 0);
	}

	@Override
	public Insets getBorderInsets(final Component c) {
		return insets;
	}
}