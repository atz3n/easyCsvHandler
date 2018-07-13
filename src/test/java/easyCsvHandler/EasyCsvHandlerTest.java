package easyCsvHandler;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


public class EasyCsvHandlerTest {
	
	final static String CSV_FILE = "./testFile.csv";
	
	final int numOfRecords = 10;
	final int numOfColumns = 4;
	
	String[] header = new String[numOfColumns];
	String[][] records = new String[numOfRecords][numOfColumns];
	
	
	
	@Before
	public void init() {
		deleteTestFile();
		initHeader();
		initRecords();
	}
	
	
	private void initHeader() {
		for(int j = 0 ; j < numOfColumns ; j++) {
			header[j] = "header[" + j + "]";
		}
	}
	
	private void initRecords() {
		for(int i = 0 ; i < numOfRecords ; i++) {
			for(int j = 0 ; j < numOfColumns ; j++) {
				records[i][j] = "record[" + i + "][" + j + "]";
			}
		}
	}
	
	private static void deleteTestFile() {
		try {
			Files.deleteIfExists(Paths.get(CSV_FILE));
		} catch (IOException e) {}
	}

	
	
	@AfterClass
	public static void deinit() {
		deleteTestFile();
	}
	
	
	
	@Test
	public void testCsvFileCreation() {
		try {
			createFile();
			assertTrue("CSV File should be created", Files.exists(Paths.get(CSV_FILE)));
			
		} catch (Exception e) {
			assertTrue("Error in CSV File creation test", false);
		}
	}
	
	
	private void createFile() throws Exception {
		List<String[]> recordList = new ArrayList<>();
		for(int i = 0 ; i < numOfRecords ; i++) recordList.add(records[i]);

		EasyCsvHandler handler = new EasyCsvHandler();
		EasyCsvFile csvFile = new EasyCsvFile(header, recordList);
		
		handler.createCsvFile(CSV_FILE, csvFile);
	}
	
	
	
	@Test
	public void testCsvFileParsing() {
		try {
			boolean testPassed = true;
			createFile();

			
			EasyCsvFile csvFile = parseFile();

			
			if(header.length != csvFile.header.length) testPassed = false;

			for(int i = 0 ; i < csvFile.header.length ; i++) {
				if(!csvFile.header[i].equals(header[i])) {
					testPassed = false;
					break;
				}
			}
			
			assertTrue("Parsed CSV File should have the same header as " + CSV_FILE, testPassed);
			
			
			for(int i = 0 ; i < numOfRecords ; i++) {
				String[] parsedRecord = csvFile.records.get(i);
				String[] setRecord = records[i];
				
				if(parsedRecord.length != setRecord.length) testPassed = false;
				
				for(int j = 0 ; j < numOfColumns ; j++) {
					if(!setRecord[j].equals(parsedRecord[j])) testPassed = false;
					break;
				}
			}
			
			assertTrue("Parsed CSV File should have the same records as " + CSV_FILE, testPassed);
			
			
		} catch (Exception e) {
			assertTrue("Error in CSV File parsing test", false);
		}
	}
	
	
	private EasyCsvFile parseFile() throws IOException {
		EasyCsvHandler handler = new EasyCsvHandler();
		return handler.parseCsvFile(CSV_FILE);
	}
	
	
	
	@Test
	public void testRecordFindingWithHeaderIndex() {
		try {
			boolean testPassed = true;
			int headerIndex = 2;
			int rowNumber = 3;
			
			
			createFile();
			EasyCsvFile csvFile = parseFile();
			
			
			List<String[]> foundRecords = EasyCsvHandler.findRecords(csvFile, headerIndex, "");
			assertTrue("Found records should be 0", foundRecords.size() == 0);
			
			
			String element = "record[" + rowNumber + "][" + headerIndex + "]";
			
			foundRecords = EasyCsvHandler.findRecords(csvFile, headerIndex, element);
			assertTrue("Found records should be 1", foundRecords.size() == 1);
			
			
			String[] setRecord = records[rowNumber];
			String[] foundRecord = foundRecords.get(0);
			
			for (int i = 0; i < setRecord.length; i++) {
				if(!setRecord[i].equals(foundRecord[i])) {
					testPassed = false;
					break;
				}
			}
			
			assertTrue("Found record should be the same as set record", testPassed);
			
			
			records[rowNumber + 1][headerIndex] = element;
			
			createFile();
			csvFile = parseFile();
			
			foundRecords = EasyCsvHandler.findRecords(csvFile, headerIndex, element);
			
			for(int i = 0 ; i < numOfRecords ; i++) {
				foundRecord = csvFile.records.get(i);
				setRecord = records[i];
				
				if(foundRecord.length != setRecord.length) testPassed = false;
				
				for(int j = 0 ; j < numOfColumns ; j++) {
					if(!setRecord[j].equals(foundRecord[j])) testPassed = false;
					break;
				}
			}
			
			assertTrue("Found records should be the same as set records", testPassed);
			
		} catch (Exception e) {
			assertTrue("Error in record finding test", false);
		}
	}
	
	
	
	@Test
	public void testRecordFindingWithHeaderString() {
		try {
			boolean testPassed = true;
			int headerIndex = 2;
			int rowNumber = 3;
			
			
			createFile();
			EasyCsvFile csvFile = parseFile();
			
			
			String headerString = "header[" + rowNumber + "][" + headerIndex + "]";
			List<String[]> foundRecords = EasyCsvHandler.findRecords(csvFile, headerString, "");
			
			assertTrue("Found records should be 0", foundRecords.size() == 0);
			
			
			String element = "record[" + rowNumber + "][" + headerIndex + "]";
			
			foundRecords = EasyCsvHandler.findRecords(csvFile, headerIndex, element);
			assertTrue("Found records should be 1", foundRecords.size() == 1);
			
			
			String[] setRecord = records[rowNumber];
			String[] foundRecord = foundRecords.get(0);
			
			for (int i = 0; i < setRecord.length; i++) {
				if(!setRecord[i].equals(foundRecord[i])) {
					testPassed = false;
					break;
				}
			}
			
			assertTrue("Found record should be the same as set record", testPassed);
			
			
			records[rowNumber + 1][headerIndex] = element;
			
			createFile();
			csvFile = parseFile();
			
			foundRecords = EasyCsvHandler.findRecords(csvFile, headerIndex, element);
			
			for(int i = 0 ; i < numOfRecords ; i++) {
				foundRecord = csvFile.records.get(i);
				setRecord = records[i];
				
				if(foundRecord.length != setRecord.length) testPassed = false;
				
				for(int j = 0 ; j < numOfColumns ; j++) {
					if(!setRecord[j].equals(foundRecord[j])) testPassed = false;
					break;
				}
			}
			
			assertTrue("Found records should be the same as set records", testPassed);
			
		} catch (Exception e) {
			assertTrue("Error in record finding test", false);
		}
	}
	
	
	@Test
	public void testHeaderIndexResolving(){
		try {
			int headerIndex = 2;
			
			
			createFile();
			EasyCsvFile csvFile = parseFile();
			
			
			int foundHeaderIndex = EasyCsvHandler.getHeaderIndex(csvFile, "header[" + headerIndex + "]");
			assertTrue("Header index should be " + headerIndex, headerIndex == foundHeaderIndex);
			
			foundHeaderIndex = EasyCsvHandler.getHeaderIndex(csvFile.header, "header[" + headerIndex + "]");
			assertTrue("Header index should be " + headerIndex, headerIndex == foundHeaderIndex);
			
		} catch (Exception e) {
			assertTrue("Error in header index resolving test", false);
		}
	}
}
