import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;

public class CSVLoader 
{
	private BufferedReader reader;

	//Reads in the file as an ArrayList
	CSVLoader(String filename) 
	{
		try
		{
			reader = new BufferedReader(new FileReader(filename));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<String> readFile() 
	{
		if(reader != null)
		{
			ArrayList<String> list = new ArrayList<String>();
			try
			{
				String line;
				while ((line = reader.readLine()) != null) 
				{
					list.add(line);
				}
				reader.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.exit(-1);
			}
			return list;
		}
		return new ArrayList<String>();
	}
}
