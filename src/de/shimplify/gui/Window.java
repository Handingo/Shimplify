package de.shimplify.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;

import de.shimplify.config.Config;
import de.shimplify.config.wrapper.Theme;
import de.shimplify.gui.components.menu.MenuBar;

public final class Window extends JFrame {

	private Theme theme;

	public Window(final String title) {
		super(title);

		theme = Theme.load(Config.get().getDefaults().getProperty("theme", "Dark"));

		// UIManager.put("Menu.selectionBackground", theme.contentHoverColor());

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final double screenWidth = toolkit.getScreenSize().getWidth();
        final double screenHeight = toolkit.getScreenSize().getHeight();

        final float windowScaling = 2.0f;

        final int width = (int) (screenWidth / windowScaling);
        final int height = (int) (screenHeight / windowScaling);
        final int paddingLeft = (int) (screenWidth / 2.0D - (width >> 1));
        final int paddingTop = (int) (screenHeight / 2.0D - (height >> 1));

        setResizable(false);
        setUndecorated(true);
        setBounds(paddingLeft, paddingTop, width, height);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        setIconImage(toolkit.getImage("./config/logo.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(new MenuBar(this, this.theme));
        add(new ControlPanel(this.theme));

        setVisible(true);
	}

	public void setTheme(final Theme theme) {
		this.theme = theme;
	}
}