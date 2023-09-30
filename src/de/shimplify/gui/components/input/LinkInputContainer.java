package de.shimplify.gui.components.input;

import java.awt.Component;

import javax.swing.JScrollPane;

import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.util.LabelBorder;

public final class LinkInputContainer extends JScrollPane {

	public LinkInputContainer(final Theme theme, final Component component) {
		super(component, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
		setBorder(new LabelBorder(" Weblinks, separated by newlines or whitespaces", theme.fontColor()));
		setBackground(theme.backgroundColor());
	}
}