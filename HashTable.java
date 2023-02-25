package data;


public class HashTable<K, V extends Number> {
	private TableEntry<K, V>[] hashtable;
	private int size;
	private int usedSpot;
	private int colcount;
	private double loadFactor;
	private static final int defaultSize = 2477;
	private static final double defaultLoadFactor = 0.5;
	private boolean resize;

	class TableEntry<S, T extends Number> {
		private S key;
		private T value;

		public TableEntry(S key, T value) {
			super();
			this.key = key;
			this.value = value;
		}

		public S getKey() {
			return key;
		}

		public void setKey(S key) {
			this.key = key;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

	}

	@SuppressWarnings("unchecked")
	public HashTable() {
		this.colcount = 0;
		this.size = defaultSize;
		this.loadFactor = defaultLoadFactor;
		this.usedSpot = 0;
		hashtable = new TableEntry[defaultSize];
		usedSpot = 0;

	}

	@SuppressWarnings("unchecked")
	public HashTable(int size, double loadFactor) {
		this.colcount = 0;
		boolean flag = isPrime(size);
		if (!flag)
			size = getNextPrime(size);
		this.size = size;
		this.loadFactor = loadFactor;
		this.usedSpot = 0;
		hashtable = new TableEntry[size];
	}

	public boolean isResize() {
		return resize;
	}

	public void setResize(boolean resize) {
		this.resize = resize;
	}

	public int getColcount() {
		return colcount;
	}

	public void setColcount() {
		this.colcount = this.colcount + 1;
	}

	public double getLoadFactor() {
		return loadFactor;
	}

	public void setLoadFactor(double loadFactor) {
		this.loadFactor = loadFactor;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getUsedSpot() {
		return usedSpot;
	}

	public void setUsedSpot(int usedSpot) {
		this.usedSpot = usedSpot;
	}

	public void upUsedSpot() {// increases used spot by one
		this.usedSpot = this.usedSpot + 1;
	}

	public boolean isPrime(int num) {// checks if the number is prime
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++) {
			if ((num % i) == 0) {
				prime = false;
				break;
			}
		}
		return prime;
	}

	public int getNextPrime(int num) {// gets the next prime number
		if (num <= 1)
			return 2;
		else if (isPrime(num))
			return num;
		boolean found = false;
		while (!found) {
			num++;
			if (isPrime(num))
				found = true;
		}
		return num;
	}

	@SuppressWarnings("unchecked")
	public void hash(K word, String funcType) {
		int hashcode = 0;
		if (funcType == "SSF") {// if hashtable is created with SSF
			for (int i = 0; i < ((String) ((Word<K, V>) word).getWord()).length(); i++) {// gets word's every letter
																							// individually and computes
																							// hashcode
				hashcode += ((String) ((Word<K, V>) word).getWord()).charAt(i) - 96;// by adding them up
			}
		} else {// if hashtable is created with PAF
			int length = ((String) ((Word<K, V>) word).getWord()).length();// gets word length
			for (int i = 0; i < length; i++) {
				hashcode += (((String) ((Word<K, V>) word).getWord()).charAt(i) - 96) * Math.pow(7, length - i - 1);// computes
																													// every
																													// letters
																													// hashcode
																													// and
																													// adds
																													// them
																													// up
			}

		}
		((Word<K, V>) word).setHashcode(hashcode);// sets hashcode
		int index = 0;
		index = hashcode % this.getSize();// calculates hashindex

		((Word<K, V>) word).setIndex(index);// sets hashindex
	}

	public boolean isFull() {// checks if the hashtable is too full
		boolean flag = false;
		double load = (double) usedSpot / (double) this.getSize();
		if (load >= this.getLoadFactor())
			flag = true;
		return flag;
	}

	@SuppressWarnings("unchecked")
	public void rehash(String hashfunc, String probingtype) {
		resize = true;
		TableEntry<K, V>[] oldTable = hashtable;// stores old hashtable
		int oldSize = hashtable.length;
		int newSize = getNextPrime(2 * oldSize);// computes new hashtable size
		hashtable = new TableEntry[newSize];// creates new hashtable
		this.setSize(newSize);
		usedSpot = 0;
		this.setUsedSpot(usedSpot);
		for (int i = 0; i < oldSize; i++) {// loads new hashtable
			if (oldTable[i] != null) {
				hash(oldTable[i].getKey(), hashfunc);
				add(oldTable[i].getKey(), String.valueOf(oldTable[i].getValue()), probingtype, hashfunc);

			}
		}
		resize = false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void add(K key, String fileName, String probingtype, String hashfunc) {
		if (isFull()) {// checks if hashtable is too full
			rehash(hashfunc, probingtype);
		}
		int index = ((Word<K, V>) key).getIndex();// gets key's index
		if (hashtable[index] == null) {// if hashindex is null puts the word in that location
			if (resize) {// if it is in rehash mode to avoid incorrect information
				hashtable[index] = new TableEntry(key, Integer.valueOf(fileName));
				usedSpot++;

			} else {
				hashtable[index] = new TableEntry(key, 1);// adds word to the hashtable
				((Word<K, V>) key).setIn(true);
				usedSpot++;

			}

		} else if (hashtable[index] != null// if hashindex is not empty and stores the word we are looking for
				&& ((Word<K, V>) hashtable[index].getKey()).getWord().equals(((Word<K, V>) key).getWord())) {

			int new_value = (Integer) hashtable[index].getValue();
			K new_key = hashtable[index].getKey();
			if (!resize) {// if it is in rehash mode to avoid incorrect information
				new_value = (Integer) hashtable[index].getValue() + 1;
				((Word<K, V>) new_key).addInfo(new File(fileName, 1));

			}
			hashtable[index] = new TableEntry(new_key, new_value);
			((Word<K, V>) key).setIn(true);
		} else if (hashtable[index] != null// if hashindex is not empty and not the word we are looking for
				&& !((Word<K, V>) hashtable[index].getKey()).getWord().equals(((Word<K, V>) key).getWord())) {

			probing(key, probingtype);// probes and changes the words index
			add(key, fileName, probingtype, hashfunc);// tries to add word to hashtable

		}

	}

	@SuppressWarnings("unchecked")
	public void probing(K key, String type) {
		int new_index = -1;
		if (type.equals("DH")) {//if probing type is DH
			new_index = (31 - (((Word<K, V>) key).getHashcode() % 31));
			new_index = (new_index + ((Word<K, V>) key).getIndex()) % size;
		} else {//if probing type is LP
			new_index = (((Word<K, V>) key).getIndex() + 1) % size;

		}

		((Word<K, V>) key).setIndex(new_index);//sets new index

	}

	@SuppressWarnings("unchecked")
	public void display() {//displays the hashtable
		for (int i = 0; i < this.getSize(); i++) {
			if (this.hashtable[i] != null) {
				System.out.println();
				System.out.println();
				System.out.println(i + "    " + ((Word<K, V>) this.hashtable[i].getKey()).getWord() + "    "
						+ this.hashtable[i].getValue());
				((Word<K, V>) this.hashtable[i].getKey()).display();//displays the key's fileList
			}
		}
	}

	@SuppressWarnings("unchecked")
	public int search(K word, String probingType, String hashType) {
		hash(word, hashType);
		boolean found = false;
		boolean flag = false;
		int index = 0;
		while (!flag) {
			if (hashtable[((Word<K, V>) word).getIndex()] != null// if hashindex is not empty and the word we look for
					&& ((Word<K, V>) hashtable[((Word<K, V>) word).getIndex()].getKey()).getWord()
							.equals(((Word<K, V>) word).getWord())) {
				found = true;
				flag = true;
				word = (K) hashtable[((Word<K, V>) word).getIndex()];
				((Word<K, V>) ((HashTable<K, V>.TableEntry<K, V>) word).getKey()).setIn(true);

			} else if (hashtable[((Word<K, V>) word).getIndex()] == null) {// if hashindex is null this means word is
																			// not found
				found = false;
				((Word<K, V>) word).setIn(false);
				flag = true;
			} else {// if hashindex is not empty and not the word we look for
				colcount++;
				probing(word, probingType);// updates the index
			}
		}
		if (found)// if found sets the index correctly
			index = (Integer) ((Word<K, V>) ((TableEntry<K, V>) word).getKey()).getIndex();
		return index;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String mostRelated(String wordd1, String wordd2, String wordd3, int howmanyfiles) {
		Word<String, File> word1 = new Word<String, File>(wordd1, null);
		Word<String, File> word2 = new Word<String, File>(wordd2, null);
		Word<String, File> word3 = new Word<String, File>(wordd3, null);
		// to get the word from the hashtable
		if (search((K) word1, "DH", "PAF") != 0)
			word1 = (Word<String, File>) hashtable[search((K) word1, "DH", "PAF")].getKey();
		if (search((K) word2, "DH", "PAF") != 0)
			word2 = (Word<String, File>) hashtable[search((K) word2, "DH", "PAF")].getKey();
		if (search((K) word3, "DH", "PAF") != 0)
			word3 = (Word<String, File>) hashtable[search((K) word3, "DH", "PAF")].getKey();

		double max = 0;
		String mrFileName = null;
		for (int i = 1; i <= howmanyfiles; i++) {
			String fileNumber = String.format("%03d", i);
			double relNum = 0;
			double count1 = 0;
			double count2 = 0;
			double count3 = 0;
			// gets count number in desired file
			if (word1.isIn()) {
				count1 = word1.fileCount(fileNumber);

			}
			if (word2.isIn()) {
				count2 = word2.fileCount(fileNumber);

			}
			if (word3.isIn()) {
				count3 = word3.fileCount(fileNumber);

			}
			// to calculate relative number of the file
			double mean = (count1 + count2 + count3) / 3;
			double maxNum = Math.max(count1, Math.max(count2, count3));
			double minNum = Math.min(count1, Math.min(count2, count3));
			relNum = ((mean - ((maxNum - minNum) / 3)) * (count1 + count2 + count3)) + (count1 + count2 + count3);

			if (relNum > max) {// if it is more relevant from the previous file
				max = relNum;
				mrFileName = fileNumber;

			}

		}
		return mrFileName;
	}
}
