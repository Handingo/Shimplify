package de.shimplify.gui.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import de.shimplify.config.wrapper.Theme;

public class Button extends JButton {

	private final Theme theme;

	public Button(final Theme theme, final String text) {
		super(text);
		this.theme = theme;
		init();

		setFont(new Font("Arial", Font.BOLD, 16));
	}

	public Button(final Theme theme, final String iconPath, final int iconWidth, final int iconHeight) {
		this.theme = theme;
		init();

		final ImageIcon icon = new ImageIcon(iconPath);
		icon.setImage(icon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));
		setIcon(icon);
	}

	private void init() {
		setContentAreaFilled(false);
		setFocusable(false);
		setBorder(BorderFactory.createEmptyBorder());
		setForeground(theme.fontColor());
	}

	@Override
	protected void paintComponent(Graphics g) {
    	final ButtonModel model = getModel();
    	g.setColor(model.isPressed() ? theme.contentClickColor() : model.isRollover() ? theme.contentHoverColor() : theme.contentColor());
    	g.fillRect(0, 0, getWidth(), getHeight());
    	super.paintComponent(g);
    }
}