package de.shimplify.gui.components.menu;

import java.awt.Graphics;

import javax.swing.ButtonModel;
import javax.swing.JMenu;

import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.util.UI;

public class Menu extends JMenu {

	private final Theme theme;

	public Menu(final String name, final Theme theme) {
		super(name, true);

		this.theme = theme;

		setBorder(UI.INNER_BORDER);
		setBackground(theme.backgroundColor());
		setForeground(theme.fontColor());
		setOpaque(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
    	final ButtonModel model = getModel();
    	g.setColor(model.isPressed() ? theme.contentClickColor() : model.isRollover() ? theme.contentHoverColor() : theme.fontColor());
    	g.fillRect(0, 0, getWidth(), getHeight());
    	super.paintComponent(g);
    }
}