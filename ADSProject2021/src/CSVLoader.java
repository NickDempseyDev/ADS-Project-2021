import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;

public class CSVLoader {
	private static final String CSV_DELIMITER = ",";
	private BufferedReader reader;

	CSVLoader(String filename) {
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (Exception e) {

		}
	}

	public ArrayList<String[]> readFileAsArrays() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(CSV_DELIMITER);
				list.add(values);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return list;
	}

	public ArrayList<String> readFile() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return list;
	}
}
