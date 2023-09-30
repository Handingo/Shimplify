package de.shimplify;

import javax.swing.SwingUtilities;

import de.shimplify.gui.Window;

public final class Shimplify {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Window("Shimplify"));
	}
}