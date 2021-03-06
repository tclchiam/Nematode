package com.nematode.gui.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Test;

import com.nematode.bucket.StrategyBucket;
import com.nematode.gui.ExtendableJPanel;
import com.nematode.gui.GuiConstants;
import com.nematode.gui.ImagePanel;
import com.nematode.gui.ImageStore;
import com.nematode.image.processing.MockImageResizer;
import com.nematode.unittesting.AssertTestCase;

public class MainWindowTest extends AssertTestCase {

	private BufferedImage expectedBackgroundImage;

	@Override
	protected void setUp() throws Exception {
		Loader.load(org.bytedeco.javacpp.opencv_core.class);

		final ImageStore imageStore = new ImageStore();
		StrategyBucket.setImageStore(imageStore);

		this.expectedBackgroundImage = imageStore.getMainWindowBackgroundImage();
	}

	@Test
	public void testExtendsClass() throws Exception {
		assertExtends(AbstractMainWindow.class, MainWindow.class);
	}

	@Test
	public void testGetsImageResizerPassedIn() throws Exception {
		final MockImageResizer mockImageResizer = new MockImageResizer();
		final MainWindow mainWindow = new MainWindow(mockImageResizer);

		assertSame(mockImageResizer, mainWindow.getImageResizer());
	}

	@Test
	public void testAddListenerToPlayButtonAddsCorrectListenerToPlayButton() throws Exception {
		final MockMainWindowActionListener mockActionListener = new MockMainWindowActionListener();
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final JButton playButton = mainWindow.getPlayButton();
		assertEquals(0, playButton.getActionListeners().length);

		mainWindow.addListenerToPlayButton(mockActionListener);

		final ActionListener[] actionListeners = playButton.getActionListeners();
		assertEquals(1, actionListeners.length);
		assertSame(mockActionListener, actionListeners[0]);
	}

	@Test
	public void testAddListenerToPauseButtonAddsCorrectListenerToPauseButton() throws Exception {
		final MockMainWindowActionListener mockActionListener = new MockMainWindowActionListener();
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final JButton pauseButton = mainWindow.getPauseButton();
		assertEquals(0, pauseButton.getActionListeners().length);

		mainWindow.addListenerToPauseButton(mockActionListener);

		final ActionListener[] actionListeners = pauseButton.getActionListeners();
		assertEquals(1, actionListeners.length);
		assertSame(mockActionListener, actionListeners[0]);
	}

	@Test
	public void testAddListenerToStopButtonAddsCorrectListenerToStopButton() throws Exception {
		final MockMainWindowActionListener mockActionListener = new MockMainWindowActionListener();
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final JButton stopButton = mainWindow.getStopButton();
		assertEquals(0, stopButton.getActionListeners().length);

		mainWindow.addListenerToStopButton(mockActionListener);

		final ActionListener[] actionListeners = stopButton.getActionListeners();
		assertEquals(1, actionListeners.length);
		assertSame(mockActionListener, actionListeners[0]);
	}

	@Test
	public void testAddListenerToOpenVideoButtonAddsCorrectListenerToOpenVideoButton()
			throws Exception {
		final MockMainWindowActionListener mockActionListener = new MockMainWindowActionListener();
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final JButton openVideoButton = mainWindow.getOpenVideoButton();
		assertEquals(0, openVideoButton.getActionListeners().length);

		mainWindow.addListenerToOpenVideoButton(mockActionListener);

		final ActionListener[] actionListeners = openVideoButton.getActionListeners();
		assertEquals(1, actionListeners.length);
		assertSame(mockActionListener, actionListeners[0]);
	}

	@Test
	public void testDisplayImageCallsResizeImageWithAspect_AndWriteImageCorrectlyToImageLabel()
			throws Exception {
		final MockImageResizer imageResizer = new MockImageResizer();
		final MainWindow mainWindow = new MainWindow(imageResizer);

		final Mat expectedImage = new Mat(2, 2, 1);
		imageResizer.setImageWithAspectToReturn(expectedImage.getBufferedImage());

		final JLabel videoLabel = mainWindow.getVideoLabel();
		final ImageIcon defaultImageIcon = assertIsOfTypeAndGet(ImageIcon.class,
				videoLabel.getIcon());

		final Mat displayImage = new Mat(1, 1, 1);

		assertFalse(imageResizer.wasResizeImageWithAspectCalled());
		mainWindow.displayImage(displayImage);
		assertTrue(imageResizer.wasResizeImageWithAspectCalled());

		assertSame(displayImage.getBufferedImage(), imageResizer.getImageWithAspectPassedIn());
		assertEquals(GuiConstants.DISPLAY_HEIGHT, imageResizer.getHeightWithAspect());
		assertEquals(GuiConstants.DISPLAY_WIDTH, imageResizer.getWidthWithAspect());

		final ImageIcon newImageIcon = assertIsOfTypeAndGet(ImageIcon.class, videoLabel.getIcon());
		assertNotSame(defaultImageIcon, newImageIcon);

		final BufferedImage actualImage = assertIsOfTypeAndGet(BufferedImage.class,
				newImageIcon.getImage());
		assertSame(expectedImage.getBufferedImage(), actualImage);
	}

	@Test
	public void testConstructorSetsUpFrameCorrectly() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		assertEquals(JFrame.DO_NOTHING_ON_CLOSE, mainWindow.getDefaultCloseOperation());

		assertEquals(GuiConstants.VIDEO_FRAME_HEIGHT, mainWindow.getHeight());
		assertEquals(GuiConstants.VIDEO_FRAME_WIDTH, mainWindow.getWidth());
		assertTrue(mainWindow.isResizable());

		final Container contentPane = mainWindow.getContentPane();
		assertIsOfTypeAndGet(GridBagLayout.class, contentPane.getLayout());
	}

	@Test
	public void testConstructorCorrectlySetsUpContentPane() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final ImagePanel imageContentPane = assertIsOfTypeAndGet(ImagePanel.class,
				mainWindow.getContentPane());

		assertSame(this.expectedBackgroundImage, imageContentPane.getImage());
	}

	@Test
	public void testHasCorrectNumberOfComponents_AndLayout() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();
		assertEquals(2, contentPane.getComponentCount());
		assertIsOfTypeAndGet(GridBagLayout.class, contentPane.getLayout());
	}

	@Test
	public void testControlPanelIsCorrectlyPlaced() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();

		final GridBagLayout mainWindowLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				contentPane.getLayout());
		final ExtendableJPanel controlPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(0));

		assertEquals("controlPanel", controlPanel.getName());
		assertEquals(2, controlPanel.getComponentCount());
		assertFalse(controlPanel.isOpaque());
		assertIsOfTypeAndGet(GridBagLayout.class, controlPanel.getLayout());
		assertNull(controlPanel.getBorder());

		final GridBagConstraints controlPanelContraints = mainWindowLayout
				.getConstraints(controlPanel);
		assertEquals(0, controlPanelContraints.gridx);
		assertEquals(0, controlPanelContraints.gridy);
		assertEquals(0.0, controlPanelContraints.weightx);
		assertEquals(1.0, controlPanelContraints.weighty);
		assertEquals(GridBagConstraints.VERTICAL, controlPanelContraints.fill);
		assertEquals(GridBagConstraints.LINE_START, controlPanelContraints.anchor);
	}

	@Test
	public void testScanningPanelIsCorrectlyPlaced() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();

		final GridBagLayout mainWindowLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				contentPane.getLayout());
		final ExtendableJPanel scanningPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(1));

		assertEquals("scanningPanel", scanningPanel.getName());
		assertEquals(3, scanningPanel.getComponentCount());
		assertFalse(scanningPanel.isOpaque());
		assertIsOfTypeAndGet(GridBagLayout.class, scanningPanel.getLayout());
		assertNull(scanningPanel.getBorder());

		final GridBagConstraints scanningPanelConstraints = mainWindowLayout
				.getConstraints(scanningPanel);
		assertEquals(1, scanningPanelConstraints.gridx);
		assertEquals(0, scanningPanelConstraints.gridy);
		assertEquals(0.8, scanningPanelConstraints.weightx);
		assertEquals(1.0, scanningPanelConstraints.weighty);
		assertEquals(GridBagConstraints.BOTH, scanningPanelConstraints.fill);
		assertEquals(GridBagConstraints.LINE_END, scanningPanelConstraints.anchor);
	}

	@Test
	public void testProjectPanelIsCorrectlySetUp() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();

		final ExtendableJPanel controlPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(0));

		final ExtendableJPanel projectPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				controlPanel.getComponent(0));

		assertEquals("projectPanel", projectPanel.getName());

		assertEquals(GuiConstants.BACKGROUND_COLOR, projectPanel.getBackground());
		assertEquals(1, projectPanel.getComponentCount());
		assertTrue(projectPanel.isOpaque());

		assertIsOfTypeAndGet(CompoundBorder.class, projectPanel.getBorder());
		assertIsOfTypeAndGet(GridBagLayout.class, projectPanel.getLayout());
	}

	@Test
	public void testOpenVideoButtonIsCorrectlyAddedToProjectPanel() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();

		final ExtendableJPanel controlPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(0));

		final ExtendableJPanel projectPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				controlPanel.getComponent(0));

		final GridBagLayout projectPanelLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				projectPanel.getLayout());

		final JButton openVideoButton = assertIsOfTypeAndGet(JButton.class,
				projectPanel.getComponent(0));
		assertSame(mainWindow.getOpenVideoButton(), openVideoButton);
		assertEquals("openVideoButton", openVideoButton.getName());
		assertEquals("Open Video", openVideoButton.getText());

		final GridBagConstraints openVideoButtonConstraints = projectPanelLayout
				.getConstraints(openVideoButton);

		assertEquals(0, openVideoButtonConstraints.gridx);
		assertEquals(0, openVideoButtonConstraints.gridy);
		assertEquals(1.0, openVideoButtonConstraints.weighty);
		assertEquals(GridBagConstraints.NORTH, openVideoButtonConstraints.anchor);
		assertEquals(GridBagConstraints.HORIZONTAL, openVideoButtonConstraints.fill);
		assertEquals(new Insets(5, 5, 5, 5), openVideoButtonConstraints.insets);
	}

	@Test
	public void testProjectPanelIsCorrectlyAddedToControlPanel() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();

		final ExtendableJPanel controlPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(0));
		final GridBagLayout controlPanelLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				controlPanel.getLayout());

		final ExtendableJPanel projectPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				controlPanel.getComponent(0));

		final GridBagConstraints projectPanelConstraints = controlPanelLayout
				.getConstraints(projectPanel);
		assertEquals(0, projectPanelConstraints.gridx);
		assertEquals(0, projectPanelConstraints.gridy);
		assertEquals(1, projectPanelConstraints.gridheight);
		assertEquals(0.5, projectPanelConstraints.weighty);
		assertEquals(GridBagConstraints.BOTH, projectPanelConstraints.fill);
	}

	@Test
	public void testTrackingPanelIsCorrectlySetUp() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();

		final ExtendableJPanel controlPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(0));

		final ExtendableJPanel trackingPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				controlPanel.getComponent(1));

		assertEquals("trackingPanel", trackingPanel.getName());

		assertEquals(GuiConstants.BACKGROUND_COLOR, trackingPanel.getBackground());
		assertEquals(0, trackingPanel.getComponentCount());
		assertTrue(trackingPanel.isOpaque());

		assertIsOfTypeAndGet(CompoundBorder.class, trackingPanel.getBorder());
		assertIsOfTypeAndGet(GridBagLayout.class, trackingPanel.getLayout());
	}

	@Test
	public void testTrackingPanelIsCorrectlyAddedToControlPanel() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();

		final ExtendableJPanel controlPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(0));
		final GridBagLayout controlPanelLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				controlPanel.getLayout());

		final ExtendableJPanel trackingPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				controlPanel.getComponent(1));

		final GridBagConstraints trackingPanelConstraints = controlPanelLayout
				.getConstraints(trackingPanel);
		assertEquals(0, trackingPanelConstraints.gridx);
		assertEquals(1, trackingPanelConstraints.gridy);
		assertEquals(1, trackingPanelConstraints.gridheight);
		assertEquals(0.5, trackingPanelConstraints.weighty);
		assertEquals(GridBagConstraints.BOTH, trackingPanelConstraints.fill);
	}

	@Test
	public void testVideoPanelIsCorrectlySetUp() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();
		final ExtendableJPanel scanningPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(1));

		final ExtendableJPanel videoPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				scanningPanel.getComponent(0));

		assertEquals("videoPanel", videoPanel.getName());

		assertEquals(GuiConstants.BACKGROUND_COLOR, videoPanel.getBackground());
		assertEquals(1, videoPanel.getComponentCount());
		assertTrue(videoPanel.isOpaque());

		assertIsOfTypeAndGet(CompoundBorder.class, videoPanel.getBorder());
		assertIsOfTypeAndGet(GridBagLayout.class, videoPanel.getLayout());
	}

	@Test
	public void testVideoLabelIsCorrectlyAddedToVideoPanel() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();
		final ExtendableJPanel scanningPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(1));

		final ExtendableJPanel videoPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				scanningPanel.getComponent(0));

		final JLabel videoLabel = assertIsOfTypeAndGet(JLabel.class, videoPanel.getComponent(0));
		assertEquals("videoLabel", videoLabel.getName());
		assertSame(mainWindow.getVideoLabel(), videoLabel);

		final Dimension expectedDimentions = new Dimension(GuiConstants.DISPLAY_WIDTH,
				GuiConstants.DISPLAY_HEIGHT);
		assertEquals(expectedDimentions, videoLabel.getSize());

		final ImageIcon imageIcon = assertIsOfTypeAndGet(ImageIcon.class, videoLabel.getIcon());
		final BufferedImage defaultIconImage = assertIsOfTypeAndGet(BufferedImage.class,
				imageIcon.getImage());
		assertEquals(GuiConstants.DISPLAY_WIDTH, defaultIconImage.getWidth());
		assertEquals(GuiConstants.DISPLAY_HEIGHT, defaultIconImage.getHeight());

		final GridBagLayout videoPanelLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				videoPanel.getLayout());
		final GridBagConstraints videoLabelConstraints = videoPanelLayout
				.getConstraints(videoLabel);
		assertEquals(GridBagConstraints.CENTER, videoLabelConstraints.anchor);
		assertEquals(new Insets(1, 1, 1, 1), videoLabelConstraints.insets);
	}

	@Test
	public void testVideoPanelIsCorrectlyAddedToScanningPanel() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();
		final ExtendableJPanel scanningPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(1));
		final GridBagLayout scanningPanelLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				scanningPanel.getLayout());

		final ExtendableJPanel videoPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				scanningPanel.getComponent(0));

		final GridBagConstraints videoPanelConstraints = scanningPanelLayout
				.getConstraints(videoPanel);
		assertEquals(1, videoPanelConstraints.gridx);
		assertEquals(1, videoPanelConstraints.gridy);
		assertEquals(4, videoPanelConstraints.gridheight);
		assertEquals(1.0, videoPanelConstraints.weightx);
		assertEquals(0.1, videoPanelConstraints.weighty);
		assertEquals(GridBagConstraints.BOTH, videoPanelConstraints.fill);
	}

	@Test
	public void testToolbarPanelIsCorrectlyAddedToScanningPanel() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();
		final ExtendableJPanel scanningPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(1));
		final GridBagLayout scanningPanelLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				scanningPanel.getLayout());

		final ToolbarPanel toolbarPanel = assertIsOfTypeAndGet(ToolbarPanel.class,
				scanningPanel.getComponent(1));
		assertSame(mainWindow.getToolbarPanel(), toolbarPanel);

		final GridBagConstraints toolbarPanelConstraints = scanningPanelLayout
				.getConstraints(toolbarPanel);
		assertEquals(1, toolbarPanelConstraints.gridx);
		assertEquals(0, toolbarPanelConstraints.gridy);
		assertEquals(1.0, toolbarPanelConstraints.weightx);
		assertEquals(0.1, toolbarPanelConstraints.weighty);
		assertEquals(GridBagConstraints.PAGE_START, toolbarPanelConstraints.anchor);
		assertEquals(GridBagConstraints.BOTH, toolbarPanelConstraints.fill);
	}

	@Test
	public void testGettingToolbarButtonsReturnsCorrectButtons() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final ToolbarPanel toolbarPanel = mainWindow.getToolbarPanel();
		assertSame(toolbarPanel.getPlayButton(), mainWindow.getPlayButton());
		assertSame(toolbarPanel.getPauseButton(), mainWindow.getPauseButton());
		assertSame(toolbarPanel.getStopButton(), mainWindow.getStopButton());
	}

	@Test
	public void testStatusPanelIsCorrectlySetUp() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();
		final ExtendableJPanel scanningPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(1));

		final ExtendableJPanel statusPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				scanningPanel.getComponent(2));

		assertEquals("statusPanel", statusPanel.getName());

		assertEquals(GuiConstants.BACKGROUND_COLOR, statusPanel.getBackground());
		assertEquals(0, statusPanel.getComponentCount());
		assertTrue(statusPanel.isOpaque());

		assertIsOfTypeAndGet(CompoundBorder.class, statusPanel.getBorder());
	}

	@Test
	public void testStatusPanelIsCorrectlyAddedToScanningPanel() throws Exception {
		final MainWindow mainWindow = new MainWindow(new MockImageResizer());

		final Container contentPane = mainWindow.getContentPane();
		final ExtendableJPanel scanningPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				contentPane.getComponent(1));
		final GridBagLayout scanningPanelLayout = assertIsOfTypeAndGet(GridBagLayout.class,
				scanningPanel.getLayout());

		final ExtendableJPanel statusPanel = assertIsOfTypeAndGet(ExtendableJPanel.class,
				scanningPanel.getComponent(2));

		final GridBagConstraints statusPanelConstraints = scanningPanelLayout
				.getConstraints(statusPanel);
		assertEquals(1, statusPanelConstraints.gridx);
		assertEquals(5, statusPanelConstraints.gridy);
		assertEquals(1.0, statusPanelConstraints.weightx);
		assertEquals(0.5, statusPanelConstraints.weighty);
		assertEquals(GridBagConstraints.PAGE_END, statusPanelConstraints.anchor);
		assertEquals(GridBagConstraints.BOTH, statusPanelConstraints.fill);
	}
}
