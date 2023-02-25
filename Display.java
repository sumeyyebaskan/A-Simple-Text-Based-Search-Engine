package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Display {
	@SuppressWarnings("rawtypes")
	HashTable<Word, Integer> hashtable = null;
	String[] searchWords = null;
	ArrayList<String> stopWords = new ArrayList<String>();

	public void display() {
		getSearchWords();
		getStopWords();
		insertHash("PAF", "DH", 0.5, 2477, 100);
		getInput(100);

	}
	
	
	public void getSearchWords() {//gets search words from search.txt and stores them in searchWords string array
		try {
			File searchFile = new File("search.txt"); // to open file
			BufferedReader br = new BufferedReader(new FileReader(searchFile)); // buffer reader for read file
			searchWords = br.lines().toArray(String[]::new); // storing stop words
			br.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void getStopWords() {//gets stop words from stop_words_en.txt and stores them in stopWords array-list
		try {
			File stopWordsFile = new File("stop_words_en.txt"); // to open
			try (// file
					Scanner scan = new Scanner(stopWordsFile)) {
				while (scan.hasNextLine()) {
					String word = scan.nextLine();
					if (word != "") {
						stopWords.add(word);
					}

				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	@SuppressWarnings("rawtypes")
	// creates hashtable with desired size and load factor. then inserts words to hashtable with 2 different parameters
	public void insertHash(String hashfunc, String probingtype, double loadFactor, int size, int howmanyfile) {
		hashtable = new HashTable<Word, Integer>(size, loadFactor);
		for (int i = 1; i <= howmanyfile; i++) {//decides how many file is going to be read

			String fileNumber = String.format("%03d", i);//to get the 3 digit file number
			File f1 = new File(fileNumber + ".txt");
			try (Scanner dataReader = new Scanner(f1)) {
				while (dataReader.hasNext()) {//read word by word and while it has word to read continues
					String wordd = dataReader.next().toLowerCase(Locale.ENGLISH);

					if (!stopWords.contains(wordd)) {//if the word is not a stop word

						wordd = wordd.replaceAll("[^a-zA-Z]", "");//removes delimiters
						if (wordd != "") {//if word is not null
							data.File<String, Integer> fileInfo = new data.File<String, Integer>(fileNumber, 1);//stores which file and how many times it occurs 
							Word<String, File> word = new Word<String, File>(wordd, fileInfo);//generates key type's variable
							hashtable.hash(word, hashfunc);//to set hashcode and hasindex
							hashtable.add(word, fileNumber, probingtype, hashfunc);//to add hashtable
						}

					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	public void getInput(int howmanyfiles) {
		System.out.println("What kind of file you are looking for?");
		try (Scanner input = new Scanner(System.in)) {
			String word1 = input.nextLine().toLowerCase(Locale.ENGLISH);
			String[] words = word1.split(" ");
			String FileName = hashtable.mostRelated(words[0], words[1], words[2], howmanyfiles);//finds the most related file
			System.out.println("Most related file is: " + FileName + ".txt");
		}
	}


}
