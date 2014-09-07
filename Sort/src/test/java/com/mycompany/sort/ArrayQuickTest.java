/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sort;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author manabu
 */
public class ArrayQuickTest {
	
	public ArrayQuickTest() {
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
	 * Test of recQuickSort method, of class ArrayQuick.
	 */
	@Test
	public void testQuickSort() {
		System.out.println("quickSort");

		int maxSize = 1000;            // instanceay size

		ArrayQuick instance = new ArrayQuick(maxSize);

		// データ準備
		Random rnd = new Random();
		for (int i = 0; i < maxSize; i++) {
			instance.insert(rnd.nextLong());
		}
		
		instance.display();                // display items

		long start = System.currentTimeMillis();
		
		instance.quickSort();

		long end = System.currentTimeMillis();
		instance.display();                // display them again

		System.out.printf("process time is  [%d] milli second.\n", (end - start) );
	
	}

	
}
