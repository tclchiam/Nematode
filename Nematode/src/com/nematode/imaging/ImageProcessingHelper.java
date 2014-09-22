package com.nematode.imaging;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageProcessingHelper implements ImageProcessingHelperInterface {

	@Override
	public GreyScaleImage convertImageToGreyScale(final BufferedImage inputImage) {
		final GreyScaleImage greyScaleImage = new GreyScaleImage(
				inputImage.getWidth(), inputImage.getHeight());

		final Graphics graphics = greyScaleImage.getGraphics();
		graphics.drawImage(inputImage, 0, 0, null);
		graphics.dispose();

		return greyScaleImage;
	}

	@Override
	public BlackAndWhiteImage convertImageToBlackAndWhite(
			final GreyScaleImage inputImage, final double toleranceSeperator) {
		final int width = inputImage.getWidth();
		final int height = inputImage.getHeight();
		final int[] inputImageRGB = new int[width * height];
		final int[] outputImageRGB = new int[width * height];

		inputImage.getRGB(0, 0, width, height, inputImageRGB, 0, width);

		for (int i = 0; i < inputImageRGB.length; i++) {
			final HSBColor hsbColor = new HSBColor(inputImageRGB[i]);
			if (hsbColor.getBrightness() > toleranceSeperator) {
				outputImageRGB[i] = Color.WHITE.getRGB();
			} else {
				outputImageRGB[i] = Color.BLACK.getRGB();
			}
		}

		final BlackAndWhiteImage outputImage = new BlackAndWhiteImage(width,
				height);

		outputImage.setRGB(0, 0, width, height, outputImageRGB, 0, width);

		return outputImage;
	}

	@Override
	public BufferedImage overlayImage(final BufferedImage baseImage,
			final BufferedImage topImage) {
		final int width = baseImage.getWidth();
		final int height = baseImage.getHeight();

		final int[] baseImageRGB = new int[width * height];
		final int[] topImageRGB = new int[width * height];
		final int[] outputImageRGB = new int[width * height];

		baseImage.getRGB(0, 0, width, height, baseImageRGB, 0, width);
		topImage.getRGB(0, 0, width, height, topImageRGB, 0, width);

		for (int i = 0; i < topImageRGB.length; i++) {
			if (topImageRGB[i] == Color.BLACK.getRGB()) {
				outputImageRGB[i] = Color.CYAN.getRGB();
			} else {
				outputImageRGB[i] = baseImageRGB[i];
			}
		}

		final BufferedImage outputImage = new BufferedImage(width, height,
				baseImage.getType());

		outputImage.setRGB(0, 0, width, height, outputImageRGB, 0, width);

		return outputImage;
	}

	@Override
	public BufferedImage markDifferencesInImagesInWhite(
			final BufferedImage originalImage, final BufferedImage updatedImage) {
		final int width = originalImage.getWidth();
		final int height = originalImage.getHeight();

		final int[] originalImageRGB = new int[width * height];
		final int[] updatedImageRGB = new int[width * height];
		final int[] binaryOutputImageRGB = new int[width * height];

		originalImage.getRGB(0, 0, width, height, originalImageRGB, 0, width);
		updatedImage.getRGB(0, 0, width, height, updatedImageRGB, 0, width);

		for (int i = 0; i < updatedImageRGB.length; i++) {
			if (originalImageRGB[i] == updatedImageRGB[i]) {
				binaryOutputImageRGB[i] = Color.BLACK.getRGB();
			} else {
				binaryOutputImageRGB[i] = Color.WHITE.getRGB();
			}
		}

		final BufferedImage binaryOutputImage = new BlackAndWhiteImage(width,
				height);

		binaryOutputImage.setRGB(0, 0, width, height, binaryOutputImageRGB, 0,
				width);

		return binaryOutputImage;
	}
}