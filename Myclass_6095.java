//Md Rakibul Hasan pp1_6095

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class Myclass_6095 {

	static int N = 0;
	static Integer[] T;
	static char[] A;
	static int index_A;

	public static void main(String args[]) throws IOException {
		processBuffer(args);
	}

    static void processBuffer(String sysArgs[]) {
		String fileName = sysArgs[0];
		try {
			parseInputFile(fileName);
		}
		catch(FileNotFoundException e) {
			System.out.print("File Not Found Exception");
		}
	}

	static void createHashTable() {
		T = new Integer[N];
		A = new char[15*N];
		index_A = 0;
	}

	static void printHashTable() {
		System.out.print("A: ");

		for(int i=0;i<index_A;i++) {
			if(A[i] == '\0') {
				System.out.print("\\");
			}
			else {
				System.out.print(A[i]);
			}
		}
		System.out.println();

		System.out.println("T");
		for(int i=0;i<N;i++) {
			System.out.print(i + ":");
			if(isSlotEmpty(i)) {
				System.out.println();
			} 
			else {
				System.out.println(" " + T[i]);
			}
		}

	}

	static int getHashValue(String word) {
		int hash = -2;
		for (int i = 0; i < word.length(); i++){
    		char c = word.charAt(i);
    		hash += (int)c;   
    	}
    	return hash % N;
	}

	static boolean isSlotEmpty(int index) {
		return T[index] == null || T[index] == -1;
	}

	static boolean isWordFound(int index, String word) {
		int i = 0;
		for (; i < word.length(); i++) {
			if(word.charAt(i) !=  A[i+index]) {
				return false;
			}
		}
		return A[i+index] == '\0';
	}

	static void updateHash(String word) {
		if(word.length() <= 0) {
			return;
		}

		if(word.charAt(0) == '*') {
			for (int j = 0; j < word.length(); j++){
    			A[index_A++] = word.charAt(j);
	   		}
	    	A[index_A++] = '\0';
	    	return;
		}

		int i = 1;
		int probeIndex = getHashValue(word);
		while(!isSlotEmpty(probeIndex) && i < N) {
			probeIndex = (getHashValue(word) + i * i) % N;
			i++;
		}

		T[probeIndex] = index_A;
		for (int j = 0; j < word.length(); j++){
    		A[index_A++] = word.charAt(j);
   		}
    	A[index_A++] = '\0';
	}

	static void doubleTandA() {
		N = 2 * N;
		T = new Integer[N];
		char[] A1 = A;
		int indexA1 = index_A;
		A = new char[15*N];
		index_A = 0;

		String word = "";
		for(int i=0;i<indexA1;i++) {
			if(A1[i] == '\0') {
				updateHash(word);
				word = "";
			}
			else {
				word += A1[i];
			}
		}

	}

	static void insertWord(String word) {
		int i = 1;
		int probeIndex = getHashValue(word);
		while(!isSlotEmpty(probeIndex) && i < N) {
			probeIndex = (getHashValue(word) + i * i) % N;
			i++;
		}

		if(i >= N) {
			//System.out.println("overflow occurs");
			doubleTandA();
			insertWord(word);
		}
		else {
			T[probeIndex] = index_A;
			for (int j = 0; j < word.length(); j++){
    			A[index_A++] = word.charAt(j);
    		}
    		A[index_A++] = '\0';
		}
 	}

 	static int searchWordInTable(String word) {
 		int probeIndex = getHashValue(word);
		int i = 1;
		while(T[probeIndex] != null && i < N) {
			if(T[probeIndex] < 0 || !isWordFound(T[probeIndex], word)) { //deleted
				probeIndex = (getHashValue(word) + i * i) % N;
				i++;
			}
			else {
				return probeIndex;
			}
		}
		return -1;
 	}

	static void searchWord(String word) {
		System.out.print(word + " ");
		int index = searchWordInTable(word);
		if(index < 0) {
			System.out.println("not found");
		}
		else {
			System.out.println("found at slot " + index);
		}
	}

	static void deleteWord(String word) {
		System.out.print(word + " ");
		int index = searchWordInTable(word);
		if(index < 0) {
			System.out.println("not found");
		}
		else {
			System.out.println("deleted from slot " + index);
			int startIndex = T[index];
			for (int i = 0; i < word.length(); i++){
				A[startIndex+i] = '*';
			}
			T[index] = -1;
		}
	}

	static void parseInputFile(String fileName) throws FileNotFoundException {
		
		int command = 0;

		Scanner scanner = new Scanner(new File(fileName));
		while(scanner.hasNextInt()) {
			command = scanner.nextInt();
			if(command == 14) {
				N = scanner.nextInt();
				createHashTable();
			}
			else if(command == 15) {
				scanner.nextLine();
				//comment
			} 
			else if(command == 13) {
				printHashTable();
			}
			else {
				String word = scanner.next();
				if(command == 10) {
					insertWord(word);
				}
				else if(command == 11) {
					deleteWord(word);
				}
				else if(command == 12) {
					searchWord(word);
				}
			}
		}

		scanner.close();

	}

}