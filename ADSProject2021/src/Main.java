import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		CSVLoader c = new CSVLoader("./res/transfers.txt");
		
		ArrayList<String> list = c.readFile();
		
		list.forEach(arr -> {
			System.out.println(arr);
		});
	}

}
