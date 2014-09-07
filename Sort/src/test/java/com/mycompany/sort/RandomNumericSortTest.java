/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sort;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author manabu
 */
public class RandomNumericSortTest {
	
	public RandomNumericSortTest() {
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
	 * Test of main method, of class RandomNumericSort.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		String[] args = {"D:\\Study\\JavaAlgorithm\\random.txt", "D:\\Study\\JavaAlgorithm\\random_sorted.txt"};
		RandomNumericSort.main(args);
	}

	
}
