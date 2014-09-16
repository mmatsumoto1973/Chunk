package com.mycompany.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NumericSort {
	static int MAX_COUNT = 100000000;
	static long LIMIT_MIN = 0L;
	static long LIMIT_MAX = 10000000000L;
	
	static private long[] array = new long [MAX_COUNT];
	static private int nElements = 0;

	static private String strInputFile;
	static private String strOutputFile;
    static private boolean isOrderByDesc;
	
	public static void main(String args[]){

		if (!setArguments(args)) {
			System.out.println("Usage : inputfile outputfile [ asc | desc ]");
			return;
		}

		long start, end;

		if (inputNonSortedData(strInputFile)) {
			start = System.currentTimeMillis();
			quickSort(0, nElements-1);
			end = System.currentTimeMillis();
			System.out.printf("Quick Sort is [%d] milisecond.\n", (end - start) );
			outputSortedData(strOutputFile);
		}
	}

	/**
	 * 引数設定 
	 * 
	 * @param args 
	 */
	private static boolean setArguments(String args[]) {
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
	private static boolean inputNonSortedData(String filePath) {
		boolean isSuccesed = false;
		try{
			init();

			File file = new File(filePath);

			if (checkBeforeReadfile(file)) {
				isSuccesed = true;
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str = br.readLine();
				while(str != null) {
					long value = Long.parseLong(str);
					if (!withInTheLimits(value)) {
						System.out.printf("対象外の値が含まれています。ソート対象の値\nvalue : %d\n", value);
						isSuccesed = false;
						break;
					}
					if (!insert(value)) {
						System.out.printf("対象が多いためソートできません。ソート可能な自然数の数は %d 個までです。\n", MAX_COUNT);
						isSuccesed = false;
						break;
					}
					str = br.readLine();
				}
				br.close();
			}else{
				System.out.printf("ファイルを読み取れません。パスを確認して下さい。\nfile : %s\n", filePath);
				isSuccesed = false;
			}
		} catch(IOException e) {
			System.out.println(e);
			isSuccesed = false;
		}
		return isSuccesed;
	}

	/**
	 * ソート後のデータ出力
	 * 
	 * @param filePath 
	 */
	private static void outputSortedData(String filePath) {
		try {
			File file = new File(filePath);

			if (checkBeforeWritefile(file)){
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				for (int i = 0; i < nElements; i++) {
					pw.println(Long.toString(array[i]));
				}
				pw.close();
			}else{
				System.out.printf("ファイルに書き込めません。アクセス権限や他のアプリケーションで使用状況を確認して下さい。\nfile : %s\n", filePath);
			}
		} catch(IOException e) {
		  System.out.println(e);
		}
	}

	/**
	 * ファイル書き込みチェック
	 * 
	 * @param file
	 * @return 
	 */
	private static boolean checkBeforeWritefile(File file) {
		if (!file.exists() || (file.isFile() && file.canWrite())) {
			return true;
		}
		return false;
	}

	/**
	 * ファイル読み込みチェック
	 * 
	 * @param file
	 * @return 
	 */
	private static boolean checkBeforeReadfile(File file) {
		if (file.exists() && file.isFile() && file.canRead()) {
			return true;
		}
		return false;
	}
	
	/**
	 * ソート用配列の初期化
	 * 
	 */
	private static void init() {
		for (int i = 0; i < MAX_COUNT; i++){
			array[i] = 0;
		}
		nElements= 0;
	}

	/**
	 * ソート用要素の値チェック
	 * 
	 * @param value 
	 */
	private static boolean withInTheLimits(long value) {
		return (LIMIT_MIN >= 0 && value <= LIMIT_MAX);
	}

	/**
	 * ソート用要素のinsert
	 * 
	 * @param value 
	 */
	private static boolean insert(long value) {
		if (nElements >= MAX_COUNT)
			return false;
		array[nElements] = value;
		nElements++;
		return true;
	}

	/**
	 * Quickソート処理(再帰)
	 * 
	 * @param left ソート範囲左端
	 * @param right ソート範囲右端
	 */
	private static void quickSort(int left, int right) {
		int size = right-left+1;
		if (size < 100) {
			insertionSort(left, right);	// ソート対象が少量の場合は挿入ソートの方が早い
		} else {
			long median = getMedianAndSort3Points(left, right);
			int partitionPos = partitionIntoTwo(left, right, median);
			quickSort(left, partitionPos-1);
			quickSort(partitionPos+1, right);
		}
	}

	/**
	 * 左端、右端、中央の三点の中の中央値を取得する。また、この３点でソートを行う
	 * 
	 * @param left ソート範囲左端
	 * @param right ソート範囲右端
	 * @return 中央値
	 */
	private static long getMedianAndSort3Points(int left, int right) {
		int center = (left+right)/2;
		
		if(!isSortedValue(array[left], array[center]))
		   swap(left, center);

		if(!isSortedValue(array[left], array[right]))
		   swap(left, right);

		if(!isSortedValue(array[center], array[right]))
		   swap(center, right);

		return array[center];
	}

	/**
	 * ２つの値を比較しソートが必要かどうか判断する
	 * 
	 * @param val1
	 * @param val2
	 * @return 
	 */
	private static boolean isSortedValue(long val1, long val2) {
		boolean isSortedValue = false;
		
		if (isOrderByDesc && val1 >= val2) {
			isSortedValue = true;
		} else if (!isOrderByDesc && val1 <= val2) {
			isSortedValue = true;
		}
		return isSortedValue;
	}


	/**
	 * 分割値を基準とした値のグルーピング処理を行う
	 *  この処理により分割の左右に分割値よりも小さい、大きいグループに値の位置を交換する。
	 *  但し、この値グループの中はソートされない。
	 *  このそれぞれのグループの中で再帰的に本処理を実行する事でソートを実現する
	 * 
	 * @param left  対象配列左端
	 * @param right 対象配列右端
	 * @param median 中央値
	 * @return 中央値位置
	 */
	private static int partitionIntoTwo(int left, int right, long median) {
		int center = (left+right) / 2;

		// 中央値の退避
		int medianPos = right - 1;
		swap(center, medianPos);

		int leftPtr = left;
		int rightPtr = medianPos;
		
		while(true) {
			while(leftPtr < right && isSortedValue(array[++leftPtr], median));
			while(rightPtr > left && isSortedValue(median, array[--rightPtr]));
			if(leftPtr >= rightPtr)
			   break;
			else
			   swap(leftPtr, rightPtr);
		}
		
		// 退避した中央値を正しい位置に挿入
		swap(leftPtr, medianPos);
		
		return leftPtr;
	}

	/**
	 * 値交換処理
	 * 
	 * @param index1 交換する値が格納されているindex1
	 * @param index2  交換する値が格納されているindex2
	 */
	private static void swap(int index1, int index2) {
	  long temp = array[index1];
	  array[index1] = array[index2];
	  array[index2] = temp;
	}
	
	/**
	 * 挿入ソート処理
	 * 
	 * @param left
	 * @param right 
	 */
	private static void insertionSort(int left, int right) {
		int insertPos;
		long markVal;

		for (int markPos = left+1; markPos <= right; markPos++) {
			markVal = array[markPos];	// 挿入対象の値
			insertPos = markPos;

			// 挿入位置検索＆値を右にずらす
			while (insertPos > left && !isSortedValue(array[insertPos-1], markVal)) {
				array[insertPos] = array[insertPos-1];
				insertPos--;
			}
			array[insertPos] = markVal;
		}
	}

}
