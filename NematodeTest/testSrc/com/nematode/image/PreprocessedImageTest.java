package com.nematode.image;

import java.awt.image.BufferedImage;

import org.junit.Test;

import com.nematode.image.PreprocessedImage;
import com.nematode.unittesting.AssertTestCase;

public class PreprocessedImageTest extends AssertTestCase {

	@Test
	public void testExtendsBufferedImage() throws Exception {
		assertExtends(BufferedImage.class, PreprocessedImage.class);
	}

	@Test
	public void testImageTypeIsBinary_SizeConstructor() throws Exception {
		final PreprocessedImage blackAndWhiteImage = new PreprocessedImage(1, 1);

		assertEquals(BufferedImage.TYPE_BYTE_BINARY,
				blackAndWhiteImage.getType());
	}

	@Test
	public void testImageTypeIsBinary_CopyConstructor() throws Exception {
		final PreprocessedImage blackAndWhiteImage = new PreprocessedImage(
				new NullBufferedImage());

		assertEquals(BufferedImage.TYPE_BYTE_BINARY,
				blackAndWhiteImage.getType());
	}

}
