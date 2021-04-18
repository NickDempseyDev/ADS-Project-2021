import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileReader;

public class CSVLoader {
	private BufferedReader reader;

	CSVLoader(String filename) {
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (Exception e) {

		}
	}
}
