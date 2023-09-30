package de.shimplify.gui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.Border;

public final class UI {
	public static final Insets INNER_MARGIN = new Insets(10, 10, 10, 10);
	public static final Border ITEM_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	public static final Border INNER_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	public static final Border OUTER_BORDER = BorderFactory.createEmptyBorder(20, 20, 20, 20);

	public static Component getSpacer() {
		return Box.createRigidArea(new Dimension(5, 20));
	}
}