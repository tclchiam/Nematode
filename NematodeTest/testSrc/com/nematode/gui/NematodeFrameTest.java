package com.nematode.gui;

import javax.swing.JFrame;

import org.junit.Test;

import com.nematode.unittesting.AssertTestCase;

public class NematodeFrameTest extends AssertTestCase {
	@Test
	public void testExtendsJFrame() throws Exception {
		assertExtends(JFrame.class, NematodeFrame.class);
	}

}
