package data;

import java.util.ArrayList;
import java.util.Iterator;

public class Word<A extends Object, B>{
	
	@SuppressWarnings("rawtypes")
	private ArrayList<File> fileList = new ArrayList<File>();
	private A word;
	private int index;
	private int hashcode;
	boolean in;
	
	@SuppressWarnings("rawtypes")
	public Word(A word, File<?, ?> fileInfo) {
		this.in = false;
		this.word = word;
		this.fileList = new ArrayList<File>();
		this.fileList.add(fileInfo);
	}

	public A getWord() {
		return word;
	}
	public void setWord(A word) {
		this.word = word;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<File> getFileList() {
		return fileList;
	}
	@SuppressWarnings("rawtypes")
	public void setFileList(ArrayList<File> fileList) {
		this.fileList = fileList;
	}

	public int getHashcode() {
		return hashcode;
	}

	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isIn() {
		return in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}
	
	@SuppressWarnings("rawtypes")
	public int fileCount(String fileName) {// returnes the desired file's count
		Iterator<File> itr = this.getFileList().iterator();
		int count = 0;
		while(itr.hasNext()) {
			Object info = itr.next();
			if(((File) info).getFileName().equals(fileName)) {
				count = (Integer)((File) info).getCount();
			}
		}
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInfo(File<?, ?> fileInfo) {//adds info to fileList
		Iterator<File> itr = this.getFileList().iterator();
		boolean flag = false;
		while(itr.hasNext()) {
			Object info = itr.next();
			if(((File<?, ?>) info).getFileName() == fileInfo.getFileName()) {
				int new_count = (int)((File<?, ?>) info).getCount() + 1 ;
				((File<?, Integer>) info).setCount(new_count);
				flag = true;
				break;
			}
		}
		if(!flag) {
			this.fileList.add(fileInfo);

		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void display() {//displays the fileList
		Iterator<File> itr = this.getFileList().iterator();
		while(itr.hasNext()) {
			File<?, ?> a = itr.next();
			System.out.println(a.getFileName() + ".txt       " + a.getCount());
		}

	}
	
}
