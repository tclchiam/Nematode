package com.nematode.model;

import java.util.ArrayList;

import org.junit.Test;

import com.nematode.fileIO.MockValidatedImageFile;
import com.nematode.gui.MockVideoFrame;
import com.nematode.image.BlackAndWhiteImage;
import com.nematode.image.MockDisplayFrameImage;
import com.nematode.image.MockEdgeDetectionRunner;
import com.nematode.image.MockFrameImageAssembler;
import com.nematode.image.MockImageProcessingRunner;
import com.nematode.image.NullBufferedImage;
import com.nematode.image.NullFrameImage;
import com.nematode.unittesting.AssertTestCase;

public class VideoFrameHandlerTest extends AssertTestCase {

	@Test
	public void testImplementsInterface() throws Exception {
		assertImplementsInterface(VideoFrameHandlerInterface.class, VideoFrameHandler.class);
	}

	@Test
	public void testGetsVideoFrame() throws Exception {
		final MockVideoFrame expectedVideoFrame = new MockVideoFrame();
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(expectedVideoFrame,
				new MockFrameImageAssembler(), new MockImageProcessingRunner(),
				new MockEdgeDetectionRunner());

		final VideoFrameInterface actualVideoFrame = videoFrameHandler.getVideoFrame();
		assertSame(expectedVideoFrame, actualVideoFrame);
	}

	@Test
	public void testGetsFrameImageAssembler() throws Exception {
		final MockFrameImageAssembler expectedFrameImageAssembler = new MockFrameImageAssembler();
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(new MockVideoFrame(),
				expectedFrameImageAssembler, new MockImageProcessingRunner(),
				new MockEdgeDetectionRunner());

		assertSame(expectedFrameImageAssembler, videoFrameHandler.getFrameImageAssembler());
	}

	@Test
	public void testGetsImageProcessingRunner() throws Exception {
		final MockImageProcessingRunner expectedImageProcessingRunner = new MockImageProcessingRunner();
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(new MockVideoFrame(),
				new MockFrameImageAssembler(), expectedImageProcessingRunner,
				new MockEdgeDetectionRunner());

		assertSame(expectedImageProcessingRunner, videoFrameHandler.getImageProcessingRunner());
	}

	@Test
	public void testGetsEdgeDetectionRunner() throws Exception {
		final MockEdgeDetectionRunner edgeDetectionRunner = new MockEdgeDetectionRunner();
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(new MockVideoFrame(),
				new MockFrameImageAssembler(), new MockImageProcessingRunner(), edgeDetectionRunner);

		assertSame(edgeDetectionRunner, videoFrameHandler.getEdgeDetectionRunner());
	}

	@Test
	public void testUpdateDisplayImageFromScannedObjectsSetsNewDisplayImageOnFrameUsingObjectsInImage()
			throws Exception {
		final BlackAndWhiteImage expectedImageWithObjects = new BlackAndWhiteImage(
				new NullBufferedImage());

		final MockVideoFrame videoFrame = new MockVideoFrame();
		final MockFrameImageAssembler mockAssembler = new MockFrameImageAssembler();

		final MockDisplayFrameImage expectedDisplayImage = new MockDisplayFrameImage();
		mockAssembler.setDisplayImageToReturn(expectedDisplayImage);

		final MockImageProcessingRunner imageProcessingRunner = new MockImageProcessingRunner();
		imageProcessingRunner.setImageWithObjectsToReturn(expectedImageWithObjects);
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(videoFrame,
				mockAssembler, imageProcessingRunner, new MockEdgeDetectionRunner());

		assertFalse(mockAssembler.wasCreateDisplayFrameImageCalled());
		assertFalse(imageProcessingRunner.wasCreateImageWithIdentifiedObjectsCalled());

		videoFrameHandler.updateDisplayImageFromScannedObjects();

		assertFalse(imageProcessingRunner.wasCreateImageWithIdentifiedObjectsCalled());

		assertFalse(mockAssembler.wasCreateDisplayFrameImageCalled());
	}

	@Test
	public void testBuildNewFrameImage_WithValidImage_CallsCreateFromFrameAssembler()
			throws Exception {
		final MockVideoFrame videoFrame = new MockVideoFrame();
		final MockFrameImageAssembler mockAssembler = new MockFrameImageAssembler();
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(videoFrame,
				mockAssembler, new MockImageProcessingRunner(), new MockEdgeDetectionRunner());

		final MockValidatedImageFile mockValidatedImageFile = new MockValidatedImageFile();
		mockValidatedImageFile.setIsFileValid(true);

		assertFalse(mockAssembler.wasCreateDisplayFrameImageCalled());
		assertFalse(mockAssembler.wasCreateVideoFrameImageCalled());
		videoFrameHandler.buildNewFrameImageFromFile(mockValidatedImageFile);
		assertFalse(mockAssembler.wasCreateDisplayFrameImageCalled());
		assertTrue(mockAssembler.wasCreateVideoFrameImageCalled());
	}

	@Test
	public void testBuildNewFrameImage_WithInvalidImage_DoesntCallCreateFromFrameAssembler()
			throws Exception {
		final MockFrameImageAssembler mockAssembler = new MockFrameImageAssembler();
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(new MockVideoFrame(),
				mockAssembler, new MockImageProcessingRunner(), new MockEdgeDetectionRunner());

		final MockValidatedImageFile mockValidatedImageFile = new MockValidatedImageFile();
		mockValidatedImageFile.setIsFileValid(false);

		assertFalse(mockAssembler.wasCreateVideoFrameImageCalled());
		assertFalse(mockAssembler.wasCreateDisplayFrameImageCalled());

		videoFrameHandler.buildNewFrameImageFromFile(mockValidatedImageFile);
		final VideoFrameInterface videoFrame = videoFrameHandler.getVideoFrame();

		assertFalse(mockAssembler.wasCreateVideoFrameImageCalled());
		assertFalse(mockAssembler.wasCreateDisplayFrameImageCalled());

		assertIsOfTypeAndGet(NullFrameImage.class, videoFrame.getVideoFrameImage());
	}

	@Test
	public void testScanImageCallsImageProcessingRunnerWithVideoFrame_CorrectOrder()
			throws Exception {
		final MockImageProcessingRunner imageProcessingRunner = new MockImageProcessingRunner();
		final MockEdgeDetectionRunner edgeDetectionRunner = new MockEdgeDetectionRunner();

		final MockVideoFrame videoFrame = new MockVideoFrame();
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(videoFrame,
				new MockFrameImageAssembler(), imageProcessingRunner, edgeDetectionRunner);

		assertFalse(imageProcessingRunner.wasPreprocessImageForScanningCalled());
		assertFalse(edgeDetectionRunner.wasFindAllObjectsInImageCalled());

		videoFrameHandler.scanImage();

		assertTrue(imageProcessingRunner.wasPreprocessImageForScanningCalled());
		assertSame(videoFrame, imageProcessingRunner.getUnprocessedVideoFrame());

		assertTrue(edgeDetectionRunner.wasFindAllObjectsInImageCalled());
	}

	@Test
	public void testScanImageSetsListOfObjectsOnVideoFrame() throws Exception {
		final MockVideoFrame videoFrame = new MockVideoFrame();
		final MockEdgeDetectionRunner edgeDetectionRunner = new MockEdgeDetectionRunner();
		final ArrayList<NematodeWormInterface> expectedObjectsInImageList = new ArrayList<NematodeWormInterface>();
		edgeDetectionRunner.setObjectsInImageList(expectedObjectsInImageList);
		final VideoFrameHandler videoFrameHandler = new VideoFrameHandler(videoFrame,
				new MockFrameImageAssembler(), new MockImageProcessingRunner(), edgeDetectionRunner);

		assertFalse(videoFrame.wasSetObjectsOnImageCalled());

		videoFrameHandler.scanImage();

		assertTrue(videoFrame.wasSetObjectsOnImageCalled());
		assertSame(expectedObjectsInImageList, videoFrame.getObjectsOnImage());
	}
}
