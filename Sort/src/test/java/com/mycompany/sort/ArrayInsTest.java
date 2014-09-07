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
public class ArrayInsTest {
	
	public ArrayInsTest() {
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
	 * Test of insert method, of class ArrayIns.
	 */
	@Test
	public void testInsert() {
		System.out.println("insert");
		long value = 0;
		ArrayIns instance = null;
		instance.insert(value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertionSort method, of class ArrayIns.
	 */
	@Test
	public void testInsertionSort() {
		System.out.println("insertionSort");
		int maxSize = 1000;            // instanceay size

		ArrayIns instance = new ArrayIns(maxSize);

		// データ準備
		Random rnd = new Random();
		for (int i = 0; i < maxSize; i++) {
			instance.insert(rnd.nextLong());
		}
		
		instance.display();                // display items

		long start = System.currentTimeMillis();
		
		instance.insertionSort();

		long end = System.currentTimeMillis();
		instance.display();                // display them again

		System.out.printf("process time is  [%d] milli second.\n", (end - start) );
	
	}
	
}
