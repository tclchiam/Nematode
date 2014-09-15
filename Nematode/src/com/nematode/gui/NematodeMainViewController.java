package com.nematode.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import com.nematode.imaging.FrameImageAssembler;
import com.nematode.imaging.ImageProcessingRunner;
import com.nematode.imaging.VideoFrameHandler;
import com.nematode.imaging.VideoFrameHandlerInterface;
import com.nematode.model.NematodeVideoFrame;

public class NematodeMainViewController implements MainViewControllerInterface {

	private final NematodeFrame nematodeMainView;
	private final NematodePanelViewControllerInterface nematodeProjectPanelViewController;
	private final NematodePanelViewControllerInterface nematodeVideoPanelViewController;
	private final NematodePanelViewControllerInterface nematodeTrackingPanelViewController;
	private final VideoFrameHandler videoFrameHandler;

	public NematodeMainViewController() {
		this.nematodeMainView = new NematodeMainView();

		final NematodeVideoFrame nematodeVideoFrame = new NematodeVideoFrame();
		this.videoFrameHandler = new VideoFrameHandler(nematodeVideoFrame,
				new FrameImageAssembler(), new ImageProcessingRunner());

		this.nematodeProjectPanelViewController = new NematodeProjectPanelViewController(
				this.videoFrameHandler);
		this.nematodeVideoPanelViewController = new NematodeVideoPanelViewController(
				nematodeVideoFrame);
		this.nematodeTrackingPanelViewController = new NematodeTrackingPanelViewController(
				this.videoFrameHandler);

		addPanelsToFrame();
	}

	private void addPanelsToFrame() {
		final Container contentPane = this.nematodeMainView.getContentPane();

		contentPane.add(
				this.nematodeProjectPanelViewController.getNematodePanel(),
				BorderLayout.WEST);
		contentPane.add(
				this.nematodeVideoPanelViewController.getNematodePanel(),
				BorderLayout.CENTER);
		contentPane.add(
				this.nematodeTrackingPanelViewController.getNematodePanel(),
				BorderLayout.EAST);
	}

	@Override
	public NematodeFrame getNematodeMainView() {
		return this.nematodeMainView;
	}

	public NematodePanelViewControllerInterface getNematodeProjectPanelViewController() {
		return this.nematodeProjectPanelViewController;
	}

	public NematodePanelViewControllerInterface getNematodeTrackingPanelViewController() {
		return this.nematodeTrackingPanelViewController;
	}

	public NematodePanelViewControllerInterface getNematodeVideoPanelViewController() {
		return this.nematodeVideoPanelViewController;
	}

	public VideoFrameHandlerInterface getVideoFrameHandler() {
		return this.videoFrameHandler;
	}
}
