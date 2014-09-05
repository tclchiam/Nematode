package com.nematode.imaging;

import java.awt.image.BufferedImage;

import org.junit.Test;

import com.nematode.fileIO.MockValidatedImageFile;
import com.nematode.gui.MockNematodeVideoFrame;
import com.nematode.model.NematodeVideoFrameInterface;
import com.nematode.nullmodel.NullFrameImage;
import com.nematode.unittesting.AssertTestCase;

public class VideoFrameBuilderTest extends AssertTestCase {

	@Test
	public void testImplementsInterface() throws Exception {
		assertImplementsInterface(VideoFrameBuilderInterface.class,
				VideoFrameBuilder.class);
	}

	@Test
	public void testNematodeVideoFrame() throws Exception {
		final MockNematodeVideoFrame expectedNematodeVideoFrame = new MockNematodeVideoFrame();
		final VideoFrameBuilder videoFrameBuilder = new VideoFrameBuilder(
				expectedNematodeVideoFrame);

		final NematodeVideoFrameInterface actualNematodeVideoFrame = videoFrameBuilder
				.getNematodeVideoFrame();
		assertSame(expectedNematodeVideoFrame, actualNematodeVideoFrame);
	}

	@Test
	public void testBuildVideoFrameImageSetsRealVideoFrame_ValidFile()
			throws Exception {
		final MockNematodeVideoFrame videoFrame = new MockNematodeVideoFrame();
		final VideoFrameBuilder videoFrameBuilder = new VideoFrameBuilder(
				videoFrame);

		final MockValidatedImageFile mockValidatedImageFile = new MockValidatedImageFile();
		mockValidatedImageFile.setIsFileValid(true);
		final BufferedImage expectedBufferedImage = new BufferedImage(100, 80,
				BufferedImage.TYPE_INT_RGB);
		mockValidatedImageFile.setFileImage(expectedBufferedImage);

		videoFrameBuilder.buildVideoFrameImage(mockValidatedImageFile);

		final VideoFrameImage frameImage = assertIsOfTypeAndGet(
				VideoFrameImage.class, videoFrame.getVideoFrameImage());

		final BufferedImage actualImage = frameImage.getImage();
		assertSame(expectedBufferedImage, actualImage);
	}

	@Test
	public void testBuildVideoFrameImageSetsNullVideoFrame_InvalidFile()
			throws Exception {
		final MockNematodeVideoFrame videoFrame = new MockNematodeVideoFrame();
		final VideoFrameBuilder videoFrameBuilder = new VideoFrameBuilder(
				videoFrame);

		final MockValidatedImageFile mockValidatedImageFile = new MockValidatedImageFile();
		mockValidatedImageFile.setIsFileValid(false);

		videoFrameBuilder.buildVideoFrameImage(mockValidatedImageFile);

		assertIsOfTypeAndGet(NullFrameImage.class,
				videoFrame.getVideoFrameImage());
	}
}