package fileHandle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class AlgorithmResult {
	
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 * 
	 */
	public String[] GetSpO2Result(String fileName) throws IOException
	{
		String[] result = null;
		File resultFile = new File("src/algorithmdata/SpO2/" + fileName);
		if(!resultFile.exists())
			return null;
		
		BufferedReader bfresultFile = new BufferedReader(new FileReader(resultFile));
		String bfData = bfresultFile.readLine();
		result = bfData.split(",");
		bfresultFile.close();
		return result;
	}
}
