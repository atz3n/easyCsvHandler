package easyCsvHandler;

import java.util.List;

public class EasyCsvFile {
	
	public String[] header;
	public List<String[]> records;
	
	
	public EasyCsvFile() {}

	public EasyCsvFile(String[] header, List<String[]> records) {
		this.header = header;
		this.records = records;
	}
}
