package com.nematode.gui.main;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Test;

import com.nematode.fileIO.VideoFileChooser;
import com.nematode.model.MockVideo;
import com.nematode.model.MockVideoMatriarch;
import com.nematode.unittesting.AssertTestCase;

public class MainWindowViewControllerTest extends AssertTestCase {

	@Override
	protected void setUp() throws Exception {
		Loader.load(org.bytedeco.javacpp.opencv_core.class);
	}

	@Test
	public void testImplementsInterface() throws Exception {
		assertImplementsInterface(MainWindowControllerInterface.class,
				MainWindowViewController.class);
	}

	@Test
	public void testGetsMainWindowPassedIn() throws Exception {
		final MockMainWindow mockExtendableFrame = new MockMainWindow();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mockExtendableFrame, new MockVideoMatriarch());

		assertSame(mockExtendableFrame, mainWindowViewController.getMainWindow());
	}

	@Test
	public void testGetsVideoMatriarchPassedIn() throws Exception {
		final MockVideoMatriarch mockVideoMatriarch = new MockVideoMatriarch();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), mockVideoMatriarch);

		assertSame(mockVideoMatriarch, mainWindowViewController.getVideoMatriarch());
	}

	@Test
	public void testConstructorCorrectlySetsUpDefaultTimer() throws Exception {
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), new MockVideoMatriarch());

		final PlayVideoTimer playVideoTimer = assertIsOfTypeAndGet(PlayVideoTimer.class,
				mainWindowViewController.getPlayVideoTimer());
		assertEquals(1000, playVideoTimer.getDelay());

		final ActionListener[] actionListeners = playVideoTimer.getActionListeners();
		assertEquals(1, actionListeners.length);

		final PlayVideoTimerListener playVideoTimerListener = assertIsOfTypeAndGet(
				PlayVideoTimerListener.class, actionListeners[0]);
		assertSame(mainWindowViewController, playVideoTimerListener.getViewController());
	}

	@Test
	public void testDisposeCallsDisposeOnMainWindow() throws Exception {
		final MockMainWindow mainWindow = new MockMainWindow();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mainWindow, new MockVideoMatriarch());

		assertFalse(mainWindow.wasDisposeCalled());
		mainWindowViewController.dispose();
		assertTrue(mainWindow.wasDisposeCalled());
	}

	@Test
	public void testShowViewSetsTheVisabilityOfMainWindowToTrue() throws Exception {
		final MockMainWindow mainWindow = new MockMainWindow();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mainWindow, new MockVideoMatriarch());

		assertFalse(mainWindow.isVisible());
		assertFalse(mainWindow.wasSetVisableCalled());

		mainWindowViewController.showView();

		assertTrue(mainWindow.isVisible());
		assertTrue(mainWindow.wasSetVisableCalled());
	}

	@Test
	public void testConstructionAddsCorrectWindowListenerToMainWindow() throws Exception {
		final MockMainWindow mainWindow = new MockMainWindow();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mainWindow, new MockVideoMatriarch());

		final WindowListener[] windowListeners = mainWindow.getWindowListeners();
		assertEquals(1, windowListeners.length);

		final MainWindowCloseListener mainWindowCloseListener = assertIsOfTypeAndGet(
				MainWindowCloseListener.class, windowListeners[0]);
		assertSame(mainWindowViewController, mainWindowCloseListener.getViewController());
	}

	@Test
	public void testConstructionAddsCorrectActionListenerToPlayButton() throws Exception {
		final MockMainWindow mockMainWindow = new MockMainWindow();

		assertFalse(mockMainWindow.wasAddListenerToPlayButtonCalled());

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mockMainWindow, new MockVideoMatriarch());

		assertTrue(mockMainWindow.wasAddListenerToPlayButtonCalled());

		final PlayButtonListener listenerToAddToPlayButton = assertIsOfTypeAndGet(
				PlayButtonListener.class, mockMainWindow.getListenerToAddToPlayButton());
		assertSame(mainWindowViewController, listenerToAddToPlayButton.getViewController());
	}

	@Test
	public void testConstructionAddsCorrectActionListenerToPauseButton() throws Exception {
		final MockMainWindow mockMainWindow = new MockMainWindow();

		assertFalse(mockMainWindow.wasAddListenerToPauseButtonCalled());

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mockMainWindow, new MockVideoMatriarch());

		assertTrue(mockMainWindow.wasAddListenerToPauseButtonCalled());

		final PauseButtonListener listenerToAddToPauseButton = assertIsOfTypeAndGet(
				PauseButtonListener.class, mockMainWindow.getListenerToAddToPauseButton());
		assertSame(mainWindowViewController, listenerToAddToPauseButton.getViewController());
	}

	@Test
	public void testConstuctionAddsCorrectActionListnerToStopButton() throws Exception {
		final MockMainWindow mockMainWindow = new MockMainWindow();

		assertFalse(mockMainWindow.wasAddListenerToStopButtonCalled());

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mockMainWindow, new MockVideoMatriarch());

		assertTrue(mockMainWindow.wasAddListenerToStopButtonCalled());

		final StopButtonListener stopButtonListener = assertIsOfTypeAndGet(
				StopButtonListener.class, mockMainWindow.getListenerToAddToStopButton());
		assertSame(mainWindowViewController, stopButtonListener.getViewController());
	}

	@Test
	public void testConstructionAddsCorrectActionListenerToOpenVideoButton() throws Exception {
		final MockMainWindow mockMainWindow = new MockMainWindow();

		assertFalse(mockMainWindow.wasAddListenerToOpenVideoButtonCalled());

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mockMainWindow, new MockVideoMatriarch());

		assertTrue(mockMainWindow.wasAddListenerToOpenVideoButtonCalled());

		final OpenVideoButtonListener listenerToAddToOpenVideoButton = assertIsOfTypeAndGet(
				OpenVideoButtonListener.class, mockMainWindow.getListenerToAddToOpenVideoButton());
		assertSame(mainWindowViewController, listenerToAddToOpenVideoButton.getViewController());

		assertIsOfTypeAndGet(VideoFileChooser.class,
				listenerToAddToOpenVideoButton.getFileChooser());
	}

	@Test
	public void testSetVideoCallsSetVideoOnTheVideoMatriarchWithTheCorrectVideo_ThenCallsDisplayImageOnMainWindowWithCorrectImage()
			throws Exception {
		final MockVideo mockVideo = new MockVideo();
		final Mat expectedThumbnail = new Mat(1, 1, 1);
		mockVideo.setThumbnailToReturn(expectedThumbnail);

		final MockVideoMatriarch mockVideoMatriarch = new MockVideoMatriarch();

		final MockMainWindow mainWindow = new MockMainWindow();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mainWindow, mockVideoMatriarch);

		assertFalse(mockVideoMatriarch.wasSetVideoCalled());
		assertFalse(mainWindow.wasDisplayImageCalled());

		mainWindowViewController.setVideo(mockVideo);

		assertTrue(mockVideoMatriarch.wasSetVideoCalled());
		assertSame(mockVideo, mockVideoMatriarch.getVideo());

		assertTrue(mainWindow.wasDisplayImageCalled());
		assertSame(expectedThumbnail, mainWindow.getImageToDisplay());
	}

	@Test
	public void testShowNextFrameCallsGrabCurrentFrameOnVideoMatriarch_ThenDisplayImageOnMainWindow_WithTheCurrentFrame()
			throws Exception {
		final MockVideoMatriarch mockVideoMatriarch = new MockVideoMatriarch();
		final Mat currentFrame = new Mat();
		mockVideoMatriarch.setCurrentFrameToReturn(currentFrame);

		final MockMainWindow mainWindow = new MockMainWindow();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mainWindow, mockVideoMatriarch);

		mockVideoMatriarch.setRunning(true);
		assertFalse(mockVideoMatriarch.wasGrabCurrentFrameCalled());
		assertFalse(mainWindow.wasDisplayImageCalled());

		mainWindowViewController.showNextFrame();

		assertTrue(mockVideoMatriarch.wasGrabCurrentFrameCalled());
		assertTrue(mainWindow.wasDisplayImageCalled());

		assertSame(currentFrame, mainWindow.getImageToDisplay());
	}

	@Test
	public void testShowNextFrameCallsStopButtonPressedIfIsRunningIsFalse() throws Exception {
		final MockVideoMatriarch mockVideoMatriarch = new MockVideoMatriarch();
		final MockMainWindow mockMainWindow = new MockMainWindow();
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				mockMainWindow, mockVideoMatriarch);

		final PlayVideoTimer videoTimer = assertIsOfTypeAndGet(PlayVideoTimer.class,
				mainWindowViewController.getPlayVideoTimer());
		videoTimer.start();
		assertTrue(videoTimer.isRunning());

		mockVideoMatriarch.setRunning(true);
		mockMainWindow.setDisplayImageWasCalled(false);

		mainWindowViewController.showNextFrame();
		assertTrue(videoTimer.isRunning());
		assertFalse(mockVideoMatriarch.wasStopVideoCalled());
		assertTrue(mockMainWindow.wasDisplayImageCalled());

		mockVideoMatriarch.setRunning(false);
		mockMainWindow.setDisplayImageWasCalled(false);

		mainWindowViewController.showNextFrame();
		assertFalse(videoTimer.isRunning());
		assertTrue(mockVideoMatriarch.wasStopVideoCalled());
		assertFalse(mockMainWindow.wasDisplayImageCalled());
	}

	@Test
	public void testPlayButtonPressedCallsStartVideoOnVideoMatriarch_VideoNotAlreadyRunning()
			throws Exception {
		final MockVideoMatriarch mockVideoMatriarch = new MockVideoMatriarch();

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), mockVideoMatriarch);

		assertFalse(mockVideoMatriarch.wasStartVideoCalled());

		mainWindowViewController.playButtonPressed();

		assertTrue(mockVideoMatriarch.wasStartVideoCalled());
	}

	@Test
	public void testPlayButtonPressedDoesntCallStartOnVideoMatriarch_IfVideoAlreadyRunning()
			throws Exception {
		final MockVideoMatriarch mockVideoMatriarch = new MockVideoMatriarch();
		mockVideoMatriarch.setRunning(true);

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), mockVideoMatriarch);

		assertFalse(mockVideoMatriarch.wasStartVideoCalled());
		assertTrue(mockVideoMatriarch.isRunning());

		mainWindowViewController.playButtonPressed();

		assertFalse(mockVideoMatriarch.wasStartVideoCalled());
		assertTrue(mockVideoMatriarch.isRunning());
	}

	@Test
	public void testPlayButtonPressedStartsTimer_DisposeStopsTimer() throws Exception {
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), new MockVideoMatriarch());

		final PlayVideoTimer playVideoTimer = assertIsOfTypeAndGet(PlayVideoTimer.class,
				mainWindowViewController.getPlayVideoTimer());

		assertFalse(playVideoTimer.isRunning());
		mainWindowViewController.playButtonPressed();
		assertTrue(playVideoTimer.isRunning());

		mainWindowViewController.dispose();
		assertFalse(playVideoTimer.isRunning());
	}

	@Test
	public void testPauseButtonPressedStopsTimer() throws Exception {
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), new MockVideoMatriarch());

		final PlayVideoTimer playVideoTimer = assertIsOfTypeAndGet(PlayVideoTimer.class,
				mainWindowViewController.getPlayVideoTimer());

		playVideoTimer.start();
		assertTrue(playVideoTimer.isRunning());

		mainWindowViewController.pauseButtonPressed();
		assertFalse(playVideoTimer.isRunning());
	}

	@Test
	public void testStopButtonPressedCallsStartVideoOnVideoMatriarch() throws Exception {
		final MockVideoMatriarch mockVideoMatriarch = new MockVideoMatriarch();

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), mockVideoMatriarch);

		assertFalse(mockVideoMatriarch.wasStopVideoCalled());
		mainWindowViewController.stopButtonPressed();
		assertTrue(mockVideoMatriarch.wasStopVideoCalled());
	}

	@Test
	public void testStopButtonPressedStopsTimer() throws Exception {
		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), new MockVideoMatriarch());

		final PlayVideoTimer playVideoTimer = assertIsOfTypeAndGet(PlayVideoTimer.class,
				mainWindowViewController.getPlayVideoTimer());

		playVideoTimer.start();
		assertTrue(playVideoTimer.isRunning());

		mainWindowViewController.stopButtonPressed();
		assertFalse(playVideoTimer.isRunning());
	}

	@Test
	public void testSetVideoCallsSetDelayOnTimerWithTheInverseOfTheFramerateFromTheVideo()
			throws Exception {
		final double expectedFrameRate = 25.;
		final MockVideo mockVideo = new MockVideo();
		mockVideo.setFrameRateToReturn(expectedFrameRate);

		final MainWindowViewController mainWindowViewController = new MainWindowViewController(
				new MockMainWindow(), new MockVideoMatriarch());

		final PlayVideoTimer playVideoTimer = assertIsOfTypeAndGet(PlayVideoTimer.class,
				mainWindowViewController.getPlayVideoTimer());

		assertEquals(1000, playVideoTimer.getDelay());

		mainWindowViewController.setVideo(mockVideo);
		assertEquals(40, playVideoTimer.getDelay());
	}
}
