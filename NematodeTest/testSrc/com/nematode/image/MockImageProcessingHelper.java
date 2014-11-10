package com.nematode.image;

import java.awt.image.BufferedImage;
import java.util.List;

import com.nematode.image.BlackAndWhiteImage;
import com.nematode.image.GreyScaleImage;
import com.nematode.image.processing.ImageProcessingHelperInterface;
import com.nematode.model.NematodeWormInterface;

public class MockImageProcessingHelper implements ImageProcessingHelperInterface {

	private boolean convertImageToGreyScaleWasCalled = false;
	private boolean convertImageToBlackAndWhiteWasCalled = false;
	private boolean markDifferencesInImagesInWhiteWasCalled = false;
	private boolean overlayImageWasCalled = false;
	private boolean removeObjectFromImageWasCalled = false;
	private boolean drawObjectsOnNewImageWasCalled = false;
	private BufferedImage baseImageToOverlay;
	private BufferedImage topImageToOverlay;
	private BufferedImage originalRemovalImage;
	private NematodeWormInterface wormToRemove;
	private BufferedImage baseImageToDraw;
	private List<NematodeWormInterface> objectsToDraw;
	private BlackAndWhiteImage removeObjectImageToReturn;

	@Override
	public GreyScaleImage convertImageToGreyScale(final BufferedImage inputImage) {
		this.convertImageToGreyScaleWasCalled = true;
		return new GreyScaleImage(new NullBufferedImage());
	}

	public boolean wasConvertImageToGreyScaleCalled() {
		return this.convertImageToGreyScaleWasCalled;
	}

	@Override
	public BlackAndWhiteImage convertImageToBlackAndWhite(final GreyScaleImage inputImage,
			final double toleranceSeperator) {
		this.convertImageToBlackAndWhiteWasCalled = true;

		return new BlackAndWhiteImage(new NullBufferedImage());
	}

	public boolean wasConvertImageToBlackAndWhiteCalled() {
		return this.convertImageToBlackAndWhiteWasCalled;
	}

	@Override
	public BufferedImage markDifferencesInImagesInWhite(final BufferedImage originalImage,
			final BufferedImage updatedImage) {
		this.markDifferencesInImagesInWhiteWasCalled = true;
		return new NullBufferedImage();
	}

	public boolean wasMarkDifferencesInImagesInWhiteCalled() {
		return this.markDifferencesInImagesInWhiteWasCalled;
	}

	@Override
	public BufferedImage overlayImage(final BufferedImage baseImage, final BufferedImage topImage) {
		this.baseImageToOverlay = baseImage;
		this.topImageToOverlay = topImage;
		this.overlayImageWasCalled = true;
		return new NullBufferedImage();
	}

	public boolean wasOverlayImageCalled() {
		return this.overlayImageWasCalled;
	}

	public BufferedImage getBaseImageToOverlay() {
		return this.baseImageToOverlay;
	}

	public BufferedImage getTopImageToOverlay() {
		return this.topImageToOverlay;
	}

	@Override
	public BlackAndWhiteImage removeObjectFromImage(final BufferedImage originalImage,
			final NematodeWormInterface worm) {
		this.originalRemovalImage = originalImage;
		this.wormToRemove = worm;
		this.removeObjectFromImageWasCalled = true;
		return this.removeObjectImageToReturn;
	}

	public void setRemoveObjectImageToReturn(final BlackAndWhiteImage removeObjectImageToReturn) {
		this.removeObjectImageToReturn = removeObjectImageToReturn;
	}

	public boolean wasRemoveObjectFromImageCalled() {
		return this.removeObjectFromImageWasCalled;
	}

	public BufferedImage getOriginalRemovalImage() {
		return this.originalRemovalImage;
	}

	public NematodeWormInterface getWormToRemove() {
		return this.wormToRemove;
	}

	@Override
	public BlackAndWhiteImage drawObjectsOnNewImage(final BufferedImage baseImage,
			final List<NematodeWormInterface> objects) {
		this.baseImageToDraw = baseImage;
		this.objectsToDraw = objects;
		this.drawObjectsOnNewImageWasCalled = true;

		return new BlackAndWhiteImage(new NullBufferedImage());
	}

	public boolean wasDrawObjectsOnNewImageCalled() {
		return this.drawObjectsOnNewImageWasCalled;
	}

	public BufferedImage getBaseImageToDraw() {
		return this.baseImageToDraw;
	}

	public List<NematodeWormInterface> getObjectsToDraw() {
		return this.objectsToDraw;
	}
}
