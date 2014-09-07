/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sort;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author manabu
 */
public class ArrayShTest {
	
	public ArrayShTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of display method, of class ArraySh.
	 */
	@Test
	public void testDisplay() {
		System.out.println("display");
		ArraySh instance = null;
		instance.display();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of shellSort method, of class ArraySh.
	 */
	@Test
	public void testShellSort() {
		System.out.println("shellSort");

		int maxSize = 1000;            // instanceay size

		ArraySh instance = new ArraySh(maxSize);

		// データ準備
		Random rnd = new Random();
		for (int i = 0; i < maxSize; i++) {
			instance.insert(rnd.nextLong());
		}
		
		instance.display();                // display items

		long start = System.currentTimeMillis();
		
		instance.shellSort();

		long end = System.currentTimeMillis();
		instance.display();                // display them again

		System.out.printf("process time is  [%d] milli second.\n", (end - start) );
	}
	
}
