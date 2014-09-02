package com.nematode.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class NematodeTrackingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public NematodeTrackingPanel() {
		final JButton scanButton = new JButton();
		scanButton.setName("scanButton");
		scanButton.setText("Scan Image");
		this.add(scanButton);
		setupBorder();
	}

	private void setupBorder() {
		final Border raisedBevelBorder = BorderFactory
				.createRaisedBevelBorder();
		final Border loweredBevelBorder = BorderFactory
				.createLoweredBevelBorder();
		final CompoundBorder compoundBorder = BorderFactory
				.createCompoundBorder(raisedBevelBorder, loweredBevelBorder);

		this.setBorder(compoundBorder);
	}

}
