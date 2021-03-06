package com.nematode.fileIO;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.junit.Test;

import com.nematode.unittesting.AssertTestCase;

public class ImageFileFilterTest extends AssertTestCase {

	@Test
	public void testExtendsFileFilter() throws Exception {
		assertExtends(FileFilter.class, ImageFileFilter.class);
	}

	@Test
	public void testAcceptReturnsTrueFor_jpg_jpeg_png_bmp() throws Exception {
		final ImageFileFilter imageFileFilter = new ImageFileFilter();
		assertTrue(imageFileFilter.accept(new File("testResources/Images/testJPEG.jpeg")));
		assertTrue(imageFileFilter.accept(new File("testResources/Images/testPNG.png")));
		assertTrue(imageFileFilter.accept(new File("testResources/Images/testJPG.jpg")));
		assertTrue(imageFileFilter.accept(new File("testResources/Images/testBMP.bmp")));
	}

	@Test
	public void testAcceptReturnsTrueForDirectories() throws Exception {
		final ImageFileFilter imageFileFilter = new ImageFileFilter();
		assertTrue(imageFileFilter.accept(new File("testResources")));
		assertFalse(imageFileFilter.accept(new File("testResources/Text/testTxt.txt")));
	}

	@Test
	public void testGetDescription() throws Exception {
		final ImageFileFilter imageFileFilter = new ImageFileFilter();
		assertEquals("JPEG, PNG, JPG, and BMP image files", imageFileFilter.getDescription());
	}
}
