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
public class ArrayBubTest {
	
	public ArrayBubTest() {
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
	 * Test of insert method, of class ArrayBub.
	 */
	@Test
	public void testInsert() {
		System.out.println("insert");
		long value = 0;
		ArrayBub instance = null;
		instance.insert(value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of bubbleSort method, of class ArrayBub.
	 */
	@Test
	public void testBubbleSort() {
		System.out.println("bubbleSort");

		int maxSize = 100;            // instanceay size
		
		ArrayBub instance = new ArrayBub(maxSize);

		// データ準備
		instance.insert(77);               // insert 10 items
		instance.insert(99);
		instance.insert(44);
		instance.insert(55);
		instance.insert(22);
		instance.insert(88);
		instance.insert(11);
		instance.insert(00);
		instance.insert(66);
		instance.insert(33);

		instance.display();                // display items

		long start = System.currentTimeMillis();
		
		instance.bubbleSort();

		long end = System.currentTimeMillis();
		instance.display();                // display them again

		System.out.printf("process time is  [%d] milli second.\n", (end - start) );
		
	}

	/**
	 * Test of bubbleSort method, of class ArrayBub.
	 */
	@Test
	public void testBubbleSort_ramdom() {
		System.out.println("bubbleSort");

		int maxSize = 100000;            // instanceay size
		
		ArrayBub instance = new ArrayBub(maxSize);

		// データ準備
		Random rnd = new Random();
		for (int i = 0; i < maxSize; i++) {
			instance.insert(rnd.nextLong());
		}

		instance.display();                // display items

		long start = System.currentTimeMillis();
		
		instance.bubbleSort();

		long end = System.currentTimeMillis();
		instance.display();                // display them again

		System.out.printf("process time is  [%d] milli second.\n", (end - start) );
		
	}
	
	
}
