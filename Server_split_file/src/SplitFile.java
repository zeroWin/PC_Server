
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class SplitFile {
	public static void main(String[] args) throws IOException{
		File fileReadInfo = new File("src/newdata/measdatainfo.txt");
		File fileReadData = new File("src/newdata/measdata.txt");
		File fileWrite;
		// �����ļ�������
		if(fileReadInfo.exists() && fileReadData.exists()){
			// ��ȡinfo��ֵ
			BufferedReader inInfo = new BufferedReader(new FileReader(fileReadInfo));
			BufferedReader inData = new BufferedReader(new FileReader(fileReadData));
			String lineInfo = inInfo.readLine();
			while(lineInfo != null) // �ܴ���Ϣ�ļ��ж�ȡ����
			{
				// �ö��ŷָ��ַ���
				// 1,162,128,11,1,2016-06-20 15:32:21,1,5890
				// 0  1   2   3 4          5          6  7 
				lineInfo =  lineInfo.replaceAll(":", "~");
 				String[] arrInfoSplit = lineInfo.split(",");
				switch(arrInfoSplit[1]) { // ���ݱ�ʶ������д����ļ�
				case "162":fileWrite = new File("src/newdata/ECG/"+arrInfoSplit[5]+".txt"); break;// �ĵ�
				case "166":fileWrite = new File("src/newdata/SpO2/"+arrInfoSplit[5]+".txt"); break;// Ѫ��
				default:lineInfo = inInfo.readLine();continue;	// ֱ�ӽ����´ζ�ȡ
				}
				long startLine = Long.parseLong(arrInfoSplit[6])-1;
				long endLine = Long.parseLong(arrInfoSplit[7])+startLine;
				// �ж�Ŀ¼�Ƿ���ڣ��������򴴽�
				if(!fileWrite.getParentFile().exists())
					fileWrite.getParentFile().mkdirs();
				// д������
				FileOutputStream fos =  new FileOutputStream(fileWrite); 
 				for(long i = startLine; i < endLine; ++i)
 				{
 					String lineData = inData.readLine();
 					 // д���ļ�
	       			fos.write(lineData.getBytes());
	       			fos.write("\r\n".getBytes());
 				}
       			fos.close();
 				// ��ȡ��һ������
 				lineInfo = inInfo.readLine();
				
			}
			inInfo.close();
			inData.close();
		}
		// ��ȡ���һ������
		String SpO2FileName = null;
		String EcgFileName = null;
		File EcgFileDir = new File("src/newdata/ECG");
		File SpO2FileDir = new File("src/newdata/SpO2");
		if(EcgFileDir.exists()) // EcgĿ¼����
		{
			File[] EcgFiles = EcgFileDir.listFiles();
			if(EcgFiles.length != 0)
				EcgFileName = EcgFiles[EcgFiles.length-1].getName();
		}
		if(SpO2FileDir.exists()) // SpO2Ŀ¼����
		{
			File[] SpO2Files = SpO2FileDir.listFiles();
			if(SpO2Files.length != 0)
				SpO2FileName = SpO2Files[SpO2Files.length-1].getName();
		}
		// SpO2FileName��EcgFileName�������µ��ļ�
		System.out.println(EcgFileName);
		System.out.println(SpO2FileName);
		System.out.println("over");
	}	
}