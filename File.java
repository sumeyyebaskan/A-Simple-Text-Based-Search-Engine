package data;

public class File <C, D extends Number>{
	private C fileName;
	private D count;
	public File(C fileName, D count) {
		super();
		this.fileName = fileName;
		this.count = count;
	}
	public C getFileName() {
		return fileName;
	}
	public void setFileName(C fileName) {
		this.fileName = fileName;
	}
	public D getCount() {
		return count;
	}
	public void setCount(D count) {
		this.count = count;
	}
	
}
