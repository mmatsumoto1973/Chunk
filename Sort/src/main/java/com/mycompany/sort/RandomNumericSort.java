/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author manabu
 */
public class RandomNumericSort {
	static int MAX_COUNT = 100000000;
	
	static private long[] theArray = new long [MAX_COUNT];
	static private int nElems = 0;

	static private String strInputFile;
	static private String strOutputFile;
    static private boolean isOrderByDesc;
	
	public static void main(String args[]){

		if (!setArgument(args)) {
			System.out.println("Usage : inputfile outputfile [order: asc/desc]");
			return;
		}

		long start, end;
		
		inputNonSortedData(strInputFile);
		start = System.currentTimeMillis();
		quickSort();
		end = System.currentTimeMillis();
		System.out.printf("Quick Sort is [%d] milisecond.\n", (end - start) );
		outputSortedData(strOutputFile);

	}
   
	/**
	 * 引数設定 
	 * 
	 * @param args 
	 */
	private static boolean setArgument(String args[]) {
		try {
			strInputFile = args[0];
			strOutputFile = args[1];
			if (args.length == 3){
				if (args[2].equalsIgnoreCase("asc")) {
					isOrderByDesc = false;
				} else if (args[2].equalsIgnoreCase("desc")) {
					isOrderByDesc = true;
				} else {
					return false;
				}
			} else {
				isOrderByDesc = false;
			}
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * ソート前のデータ読み込み
	 * 
	 * @param filePath 入力ファイル
	 */
	private static void inputNonSortedData(String filePath) {
		try{
			init();
			
			File file = new File(filePath);

			if (checkBeforeReadfile(file)){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str = br.readLine();
				while(str != null){
					insert(Long.parseLong(str));
					str = br.readLine();
				}
				br.close();
			}else{
			  System.out.println("ファイルを書き込めません");
			}
		}catch(IOException e){
		  System.out.println(e);
		}	
	}

	/**
	 * ソート後のデータ出力
	 * 
	 * 
	 * @param filePath 
	 */
	private static void outputSortedData(String filePath) {
		try{
		  File file = new File(filePath);

			if (checkBeforeWritefile(file)){
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

				for (int i = 0; i < nElems; i++) {
					pw.println(Long.toString(theArray[i]));
				}

				pw.close();
			}else{
			  System.out.println("ファイルに書き込めません");
			}
		}catch(IOException e){
		  System.out.println(e);
		}	
	}

	private static boolean checkBeforeWritefile(File file){
		if (!file.exists() || (file.isFile() && file.canWrite())) {
			return true;
		}
		return false;
	  }

	private static boolean checkBeforeReadfile(File file){
		if (file.exists()){
		  if (file.isFile() && file.canRead()){
			return true;
		  }
		}
		return false;
	  }
	
	/**
	 * 
	 */
	private static void init() {
		for (int i = 0; i < MAX_COUNT; i++){
			theArray[i] = 0;
		}
		nElems= 0;
	}

	/**
	 * ソート用レコードのinsert
	 * 
	 * @param value 
	 */
	private static void insert(long value) {
		theArray[nElems] = value;
		nElems++;
	}

	/**
	 * Quickソート処理
	 * 
	 */
	public static void quickSort() {
		recQuickSort(0, nElems-1);
	}

	/**
	 * Quickソート再帰処理
	 * 
	 * @param left
	 * @param right 
	 */
	private static void recQuickSort(int left, int right) {
		int size = right-left+1;
		if(size < 10)
		   insertionSort(left, right);
		else {
		   long median = medianOf3(left, right);
		   int partition = partitionIt(left, right, median);
		   recQuickSort(left, partition-1);
		   recQuickSort(partition+1, right);
		}
	}

	private static long medianOf3(int left, int right) {
	  int center = (left+right)/2;
									   // order left & center
	  if( theArray[left] > theArray[center] )
		 swap(left, center);
									   // order left & right
	  if( theArray[left] > theArray[right] )
		 swap(left, right);
									   // order center & right
	  if( theArray[center] > theArray[right] )
		 swap(center, right);

	  swap(center, right-1);           // put pivot on right
	  return theArray[right-1];        // return median value
	}  // end medianOf3()

	private static void swap(int dex1, int dex2) {
	  long temp = theArray[dex1];      // A into temp
	  theArray[dex1] = theArray[dex2];   // B into A
	  theArray[dex2] = temp;             // temp into B
	}  // end swap(

	private static int partitionIt(int left, int right, long pivot) {
	   int leftPtr = left;             // right of first elem
	   int rightPtr = right - 1;       // left of pivot
	   while(true)
		  {
		  while( theArray[++leftPtr] < pivot )  // find bigger
			 ;                                  // (nop)
		  while( theArray[--rightPtr] > pivot ) // find smaller
			 ;                                  // (nop)
		  if(leftPtr >= rightPtr)      // if pointers cross,
			 break;                    //    partition done
		  else                         // not crossed, so
			 swap(leftPtr, rightPtr);  // swap elements
		  }  // end while(true)
	   swap(leftPtr, right-1);         // restore pivot
	   return leftPtr;                 // return pivot location
	}  // end partitionIt()
									   // insertion sort

	private static void insertionSort(int left, int right) {
	  int in, out;
									   //  sorted on left of out
	  for(out=left+1; out<=right; out++)
		 {
		 long temp = theArray[out];  // remove marked item
		 in = out;                     // start shifts at out
									   // until one is smaller,
		 while(in>left && theArray[in-1] >= temp)
			{
			theArray[in] = theArray[in-1]; // shift item to right
			--in;                      // go left one position
			}
		 theArray[in] = temp;          // insert marked item
		 }  // end for
	}  // end insertionSort()
	
	/**
	 * shell ソート処理
	 * 
	 */
	private static void shellSort() {
		int inner, outer;
		long temp;

		int h = 1;                     // find initial value of h
		while(h <= nElems/3)
			 h = h*3 + 1;                // (1, 4, 13, 40, 121, ...)

		  while(h>0)                     // decreasing h, until h=1
			 {
										 // h-sort the file
			 for(outer=h; outer<nElems; outer++) {
				temp = theArray[outer];
				inner = outer;
										 // one subpass (eg 0, 4, 8)
				while(inner > h-1 && theArray[inner-h] >=  temp) {
					theArray[inner] = theArray[inner-h];
					inner -= h;
				}
				theArray[inner] = temp;
				}  // end for
			 h = (h-1) / 3;              // decrease h
		  }
	}

}
