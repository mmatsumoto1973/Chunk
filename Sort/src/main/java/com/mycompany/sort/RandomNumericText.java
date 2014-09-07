/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author manabu
 */
public class RandomNumericText {

	public static void main(String args[]){
		createFile("D:\\work\\自分\\仕事\\ソースコンテスト\\sort\\random.txt", 10, 10000000);
	}
	
	static void createFile(String filePath, int digits, int lines) {
	
		try{
		  File file = new File(filePath);

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
			for (int i = 0; i < lines; i++) {
				long tmp = Long.parseLong(RandomStringUtils.randomNumeric(digits));
				pw.println(tmp);
			}
			
			pw.close();
		}catch(IOException e){
		  System.out.println(e);
		}	
	}
}
