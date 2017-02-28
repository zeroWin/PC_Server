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
	 * 返回算法处理的结果，数组内容依次为
	 * SpO2,HI_RATE, LO_RATE, MEAN_RATE, SDNN, R_MSSD, LF ,HF,TP, lfnorm ,hfnorm,lf_hf
	 */
	public static String[] GetSpO2Result(String fileName) throws IOException
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
	
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 * 返回算法处理的结果，数组内容依次为
	 * HI_RATE, LO_RATE, MEAN_RATE, SDNN, R_MSSD, LF ,HF,TP, lfnorm ,hfnorm,lf_hf
	 */
	public static String[] GetECGResult(String fileName) throws IOException
	{
		String[] result = null;
		File resultFile = new File("src/algorithmdata/ECG/" + fileName);
		if(!resultFile.exists())
			return null;
		
		BufferedReader bfresultFile = new BufferedReader(new FileReader(resultFile));
		String bfData = bfresultFile.readLine();
		result = bfData.split(",");
		bfresultFile.close();
		return result;
	}
}
