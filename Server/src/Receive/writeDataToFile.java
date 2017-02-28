package Receive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class writeDataToFile {
	private static String Path="C:\\Users\\lenovo\\Desktop\\test.txt";
	private static Object Data ;
	
	public writeDataToFile(String Path,Object Data){
		writeDataToFile.Path=Path;
		writeDataToFile.Data=Data;
	}
	
	public writeDataToFile(Object Data){
		writeDataToFile.Data=Data;
	}
	
	public void writeReceiveDataToFile(){
			File docFile = new File(Path);
			try {
			docFile.createNewFile();
			FileOutputStream txtfile = new FileOutputStream(docFile);
			PrintStream p = new PrintStream(txtfile);
			p.print(Data);
			txtfile.close();
			p.close();
			} catch (IOException e) {
			e.printStackTrace();
			}
	}

}
