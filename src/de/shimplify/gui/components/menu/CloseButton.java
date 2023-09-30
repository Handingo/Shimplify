package de.shimplify.gui.components.menu;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;

import de.shimplify.config.wrapper.Theme;

public final class CloseButton extends JButton {

	private final Theme theme;

	public CloseButton(final Theme theme, final String text) {
		super(text);
		this.theme = theme;

		setContentAreaFilled(false);
		setFocusable(false);
		setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
		setForeground(theme.fontColor());
		setFont(new Font("Arial", Font.PLAIN, 16));

		addActionListener(e -> System.exit(0));
	}

	@Override
	protected void paintComponent(Graphics g) {
    	final ButtonModel model = getModel();
    	g.setColor(model.isPressed() ? theme.contentClickColor() : model.isRollover() ? theme.warnColor() : theme.backgroundColor());
    	g.fillRect(0, 0, getWidth(), getHeight());
    	super.paintComponent(g);
    }
}