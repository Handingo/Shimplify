package de.shimplify.gui.components.menu;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.border.MatteBorder;

import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.Window;

public final class MenuBar extends JMenuBar {

	public MenuBar(final Window window, final Theme theme) {
		setBorder(new MatteBorder(0, 0, 1, 0, theme.contentColor()));
		setBackground(theme.backgroundColor());
		setForeground(theme.fontColor());
		setOpaque(true);

		final JLabel toolName = new JLabel("Shimplify");
		toolName.setForeground(theme.contentClickColor());
		toolName.setFont(new Font("Arial", Font.ITALIC, 18));

		add(Box.createHorizontalStrut(15));
		add(toolName);
		add(Box.createHorizontalGlue());
		add(new CloseButton(theme, "X"));

		final DragListener dragListener = new DragListener(window);
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
	}

	private static class DragListener extends MouseAdapter {

		private final Window window;
		private int relWindowPosX, relWindowPosY;

		private DragListener(final Window window) {
			this.window = window;
			relWindowPosX = window.getX();
			relWindowPosY = window.getY();
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			relWindowPosX = e.getX();
			relWindowPosY = e.getY();
			window.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		@Override
		public void mouseDragged(final MouseEvent e) {
			final Rectangle bounds = window.getBounds();
			window.setBounds(e.getXOnScreen() - relWindowPosX, e.getYOnScreen() - relWindowPosY, bounds.width, bounds.height);
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}