package com.nematode.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class ToolbarPanel extends ExtendableJPanel {

	private static final long serialVersionUID = 1L;

	public ToolbarPanel() {
		this.setName("toolbarPanel");
		this.setLayout(new GridBagLayout());

		addCropScanAreaButton();
		addBorder();
	}

	private void addCropScanAreaButton() {
		final JButton cropScanAreaButton = new JButton();
		cropScanAreaButton.setName("cropScanAreaButton");
		cropScanAreaButton.setPreferredSize(new Dimension(20, 20));
		cropScanAreaButton.setToolTipText("Select Area To Scan");

		final ImageIcon cropButtonImageIcon = new ImageIcon(
				GuiConstants.CROP_AREA_BUTTON_IMAGE_PATH);
		cropScanAreaButton.setIcon(cropButtonImageIcon);

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		this.add(cropScanAreaButton, constraints);
	}

	private void addBorder() {
		final Border raisedBevelBorder = BorderFactory.createRaisedBevelBorder();
		final Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();
		final CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(raisedBevelBorder,
				loweredBevelBorder);
		this.setBorder(compoundBorder);
	}

}