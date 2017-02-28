package fileHandle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.mathworks.toolbox.javabuilder.MWException;

public class SplitFile {
	
	/* 
	 * 解析measdatainfo.txt文件，将measdata.txt分割到不同的文件中
	 */
	public static void SplitFileToDifferentDir() throws IOException, MWException
	{
		File fileReadInfo = new File("src/srcdata/measdatainfo.txt");
		File fileReadData = new File("src/srcdata/measdata.txt");
		File fileWrite;
		// 两个文件都存在
		if(fileReadInfo.exists() && fileReadData.exists()){
			// 读取info的值
			BufferedReader inInfo = new BufferedReader(new FileReader(fileReadInfo));
			BufferedReader inData = new BufferedReader(new FileReader(fileReadData));
			String lineInfo = inInfo.readLine();
			while(lineInfo != null) // 能从信息文件中读取到行
			{
				// 用逗号分隔字符串
				// 1,162,128,11,1,2016-06-20 15:32:21,1,5890
				// 0  1   2   3 4          5          6  7 
				// 3 是医生id，4是病人id
				lineInfo =  lineInfo.replaceAll(":", "~");
 				String[] arrInfoSplit = lineInfo.split(",");
 				String fileName = arrInfoSplit[3]+","+arrInfoSplit[4]+","+arrInfoSplit[5]+".txt";
				switch(arrInfoSplit[1]) { // 根据标识符决定写入的文件
				case "161":fileWrite = new File("src/srcdata/Tempr/" + fileName); break;// 体温
				case "162":fileWrite = new File("src/srcdata/ECG/" + fileName); break;// 心电
				case "163":fileWrite = new File("src/srcdata/BP/" + fileName); break;// 血压
				case "166":fileWrite = new File("src/srcdata/SpO2/"+ "bf_handle_"+fileName); break;// 血氧
				default:lineInfo = inInfo.readLine();continue;	// 直接进入下次读取
				}
				long startLine = Long.parseLong(arrInfoSplit[6])-1;
				long endLine = Long.parseLong(arrInfoSplit[7])+startLine;
				// 判断目录是否存在，不存在则创建
				if(!fileWrite.getParentFile().exists())
					fileWrite.getParentFile().mkdirs();
				// 写入数据
				FileOutputStream fos =  new FileOutputStream(fileWrite); 
 				for(long i = startLine; i < endLine; ++i)
 				{
 					String lineData = inData.readLine();
 					 // 写入文件
	       			fos.write(lineData.getBytes());
	       			fos.write("\r\n".getBytes());
 				}
       			fos.close();
       			
       			if(arrInfoSplit[1].equals("166")) // 如果是血氧，进一步处理
       			{
       				SpO2ChangeDataOrder(fileWrite,fileName);
       				//使用算法处理文件
       				SpO2Algorithm(fileName);
       			}
       			
       			if(arrInfoSplit[1].equals("162")) // 如果是心电，进一步处理
       			{
       				
       			}
 				// 读取下一行数据
 				lineInfo = inInfo.readLine();
				
			}
			inInfo.close();
			inData.close();
		}		
	}
	
	/**
	 * 
	 * @param fileBfHandle
	 * @param fileName
	 * @throws IOException
	 * 生成算法用文件和画图用文件
	 */
	private static void SpO2ChangeDataOrder(File fileBfHandle,String fileName) throws IOException
	{
		File fileWrite_afHandle; // 算法用文件
		File fileWrite_draw;     // 画图用文件
		int i = 0;
		String[] dataTemp = new String[18];
		//读入数据
		BufferedReader bfDataFile = new BufferedReader(new FileReader(fileBfHandle));
		fileWrite_afHandle = new File("src/srcdata/SpO2/algorithm_" + fileName);
		fileWrite_draw = new File("src/srcdata/SpO2/" + fileName);
		// 写入数据
		FileOutputStream fos =  new FileOutputStream(fileWrite_afHandle);
		FileOutputStream fos_draw =  new FileOutputStream(fileWrite_draw);
		
		 // 写入文件
		fos_draw.write("SpO2".getBytes());
		fos_draw.write("\r\n".getBytes());
		
		String bfData = bfDataFile.readLine();
		while(bfData != null)
		{
			dataTemp[i] = bfData;
			i++;
			if(i == 18) // 正好一组，写入
			{
				i = 0;
				for(int j = 2; j < 10; ++j)
 				{
 					 // 写入算法用文件
	       			fos.write(dataTemp[j].getBytes());
	       			fos.write("\r\n".getBytes());
	       			fos.write(dataTemp[j+8].getBytes());
	       			fos.write("\r\n".getBytes());
	       			
					 // 写入画图用文件
	       			fos_draw.write(dataTemp[j].getBytes());
	       			fos_draw.write("\r\n".getBytes());
	       			fos_draw.write(dataTemp[j+8].getBytes());
	       			fos_draw.write("\r\n".getBytes());
 				}				
			}
			bfData = bfDataFile.readLine();
		}
		
		bfDataFile.close();
		fos.close();
		fos_draw.close();
		fileBfHandle.delete();
	}
	
	/**
	 * 
	 * @param fileName
	 * @throws IOException
	 * 调用处理算法对采集到的文件进行处理
	 * @throws MWException 
	 */
	private static void SpO2Algorithm(String fileName) throws IOException, MWException
	{
		String fileAlgorithmPath = "src/srcdata/SpO2/algorithm_" + fileName;
		fileAlgorithmPath = fileAlgorithmPath.replace("/", "\\");
		
		SpO2parameters.Class1 c1 = new SpO2parameters.Class1();
		
		// 得到了算法的结果
		Object[] result_HRVparameters= null;
		Object[] result_SpO2= null;
		result_HRVparameters = c1.HRVparameters(11,fileAlgorithmPath);
		result_SpO2 = c1.SpO2_Val_acVdc(1,fileAlgorithmPath);
		
		
		// 将结果写入到文件,用逗号隔开，依次为
		// SpO2,HI_RATE,LO_RATE,MEAN_RATE,SDNN,R_MSSD,LF,HF,TP, lfnorm ,hfnorm,lf_hf
		File AlgorithmResult = new File("src/algorithmdata/SpO2/" + fileName);
		FileOutputStream fos =  new FileOutputStream(AlgorithmResult);
		
		fos.write(result_SpO2[0].toString().getBytes());
		fos.write(",".getBytes());
		
		
		for(int i = 0; i < 11; ++i)
		{
			fos.write(result_HRVparameters[i].toString().getBytes());
			if(i != 10)
				fos.write(",".getBytes());
		}
		
		fos.close();
		// 删除算法用文件
		File fileAlgorithm = new File("src/srcdata/SpO2/algorithm_" + fileName);
		fileAlgorithm.delete();

	}
}
