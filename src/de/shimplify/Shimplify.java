package de.shimplify;

import javax.swing.SwingUtilities;

import de.shimplify.gui.Window;

public final class Shimplify {
	public static void main(String[] args) {
		/*
		 * TODO
		 * - exportierte jar kommt mit umlauten nicht klar, vllt extra deklarieren, dass in UTF-8 geprintet werden soll?
		 * - Settings speichern nach dem Beenden des Programms
		 * - HTML conversion
		 * - ggf. GUI Settings, vllt Scrollbar stylen
		 */
		SwingUtilities.invokeLater(() -> new Window("Shimplify"));
	}
}