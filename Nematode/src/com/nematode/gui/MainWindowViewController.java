package com.nematode.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;

import com.nematode.image.detection.ContourAreaCalculator;
import com.nematode.image.detection.EdgeDetectionRunner;
import com.nematode.image.detection.SquareContourTracer;
import com.nematode.image.processing.ImageProcessingHelper;
import com.nematode.image.processing.ImageProcessingRunner;
import com.nematode.model.VideoFrame;
import com.nematode.model.VideoFrameHandler;
import com.nematode.model.VideoFrameHandlerInterface;
import com.nematode.model.factory.FrameImageAssembler;
import com.nematode.model.factory.NematodeWormBuilder;

public class MainWindowViewController implements MainWindowControllerInterface {

	private final ProjectPanelViewControllerInterface projectPanelViewController;
	private final VideoPanelViewControllerInterface videoPanelViewController;
	private final TrackingPanelViewControllerInterface trackingPanelViewController;
	private final ToolbarPanelViewControllerInterface toolbarPanelViewController;
	private final StatusPanelViewControllerInterface statusPanelViewController;
	private final ExtendableJFrame mainWindow;
	private VideoFrameHandlerInterface videoFrameHandler;

	public MainWindowViewController() {
		this.mainWindow = new MainWindow();

		setUpVideoFrameHandler();

		this.projectPanelViewController = new ProjectPanelViewController(this.videoFrameHandler);
		this.videoPanelViewController = new VideoPanelViewController(this.videoFrameHandler);
		this.trackingPanelViewController = new TrackingPanelViewController(this.videoFrameHandler);
		this.toolbarPanelViewController = new ToolbarPanelViewController();
		this.statusPanelViewController = new StatusPanelViewController();

		addPanelsToFrame();
	}

	private void setUpVideoFrameHandler() {
		final VideoFrame videoFrame = new VideoFrame();

		final ImageProcessingRunner imageProcessingRunner = new ImageProcessingRunner(
				new ImageProcessingHelper());
		final NematodeWormBuilder nematodeWormBuilder = new NematodeWormBuilder(
				new ContourAreaCalculator());
		final EdgeDetectionRunner edgeDetectionRunner = new EdgeDetectionRunner(
				new SquareContourTracer(), nematodeWormBuilder, new ImageProcessingHelper());

		this.videoFrameHandler = new VideoFrameHandler(videoFrame, new FrameImageAssembler(),
				imageProcessingRunner, edgeDetectionRunner);
	}

	private void addPanelsToFrame() {
		final Container contentPane = this.mainWindow.getContentPane();

		addProjectPanel();
		contentPane.add(this.videoPanelViewController.getVideoPanel());
		addTrackingPanel();
		contentPane.add(this.toolbarPanelViewController.getToolbarPanel());
		contentPane.add(this.statusPanelViewController.getStatusPanel());
	}

	private void addProjectPanel() {
		final Container contentPane = this.mainWindow.getContentPane();

		final GridBagConstraints projectPanelConstraints = new GridBagConstraints();
		projectPanelConstraints.gridx = 0;
		projectPanelConstraints.gridy = 0;
		contentPane.add(this.projectPanelViewController.getProjectPanel(), projectPanelConstraints);
	}

	private void addTrackingPanel() {
		final Container contentPane = this.mainWindow.getContentPane();

		final GridBagConstraints trackingPanelConstraints = new GridBagConstraints();
		trackingPanelConstraints.gridx = 0;
		trackingPanelConstraints.gridy = 1;
		contentPane.add(this.trackingPanelViewController.getTrackingPanel(),
				trackingPanelConstraints);
	}

	@Override
	public ExtendableJFrame getMainWindow() {
		return this.mainWindow;
	}

	public ProjectPanelViewControllerInterface getProjectPanelViewController() {
		return this.projectPanelViewController;
	}

	public TrackingPanelViewControllerInterface getTrackingPanelViewController() {
		return this.trackingPanelViewController;
	}

	public VideoPanelViewControllerInterface getVideoPanelViewController() {
		return this.videoPanelViewController;
	}

	public ToolbarPanelViewControllerInterface getToolbarPanelViewController() {
		return this.toolbarPanelViewController;
	}

	public StatusPanelViewControllerInterface getStatusPanelViewController() {
		return this.statusPanelViewController;
	}

	public VideoFrameHandlerInterface getVideoFrameHandler() {
		return this.videoFrameHandler;
	}
}
