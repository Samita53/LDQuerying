import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {

	public static void main(String[] args) throws IOException{
		 WriteFile obj = new WriteFile();
		 
		 obj.appendFile("samita");
		 obj.appendFile("bai");
	}
	
	public static void appendFile(String str) 
			  throws IOException {
			    //String str = "World";
			    BufferedWriter writer = new BufferedWriter(new FileWriter("/home/samita/benchmarkQueriesNew/My_Queries/Query.log", true));
			    writer.append(' ');
			    writer.append(str);
			     
			    writer.close();
			}

}
