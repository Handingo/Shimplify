package de.shimplify.gui.components.start;

import java.awt.Component;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;

import de.shimplify.config.wrapper.Theme;
import de.shimplify.converter.ExtractionSessionManager;
import de.shimplify.gui.components.destination.TextInputSmall;
import de.shimplify.gui.components.input.TextInput;
import de.shimplify.gui.util.Button;

public final class StartButton extends Button {

	// https://geizhals.de/apple-iphone-14-128gb-mitternacht-a2806602.html
	public StartButton(final Theme theme, final String text, final TextInput linkInput, final TextInputSmall destinationInput) {
		super(theme, text);

		final AtomicInteger finishedSessions = new AtomicInteger();

		addActionListener(event -> {
			final String[] links = linkInput.getText().split("\\s+");
			final int linkAmount = links.length;

			if (linkAmount < 1 || links[0].length() < 2) {
				sendError(linkInput, "Error", "Please enter at least one valid link");
				return;
			}

			
			final String destinationText = destinationInput.getText();

			if (destinationText.length() < 1) {
				sendError(destinationInput, "Error", "Invalid destination '" + destinationText + "'");
				return;
			}

			final File destination = new File(destinationText);

			if (!destination.exists() && !destination.mkdirs()) {
				sendError(destinationInput, "Error", "Couldn't create destination point '" + destinationText + "'");
				return;
			}

			setEnabled(false);

			final AtomicInteger textCounter = new AtomicInteger();
			final ScheduledExecutorService textAnimationThread = Executors.newSingleThreadScheduledExecutor();
			final int charAmount = 20, splitCharAmount = charAmount / 2;

			// loading bar animation
			textAnimationThread.scheduleAtFixedRate(() -> {
				final int count = (textCounter.incrementAndGet() - 1) % (charAmount + 1);
				final String firstBar = new String(new char[Math.min(splitCharAmount, count)]).replace('\0', '|');
				final String secondBar = new String(new char[Math.max(0, count - splitCharAmount) % (splitCharAmount + 1)]).replace('\0', '|');
				setText(String.format(Locale.US, "%-11s %d / %d  %-11s", firstBar, finishedSessions.get(), links.length, secondBar));
			}, 70, 70, TimeUnit.MILLISECONDS);

			// starting ExtractionSessions for each link via the ExtractionSessionManager class
			new ExtractionSessionManager(links, finishedSessions, destination, (error) -> {
				if (error != null) {
					sendError(linkInput, "Process stopped", error);
				}

				textAnimationThread.shutdownNow();
				finishedSessions.set(0);
				setEnabled(true);
				setText(text);
			});
		});
	}

	private void sendError(final Component holder, final String title, final String message) {
		JOptionPane.showMessageDialog(holder, message, title, JOptionPane.ERROR_MESSAGE);
	}
}