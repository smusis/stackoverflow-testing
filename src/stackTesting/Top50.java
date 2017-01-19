package stackTesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class Top50 {
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	public static void main(String[] args) throws Exception {
		//getData();
		//getQuestions();
		createDoc();
	}

	public static void getData() throws Exception{
		final BufferedReader br=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\Tags\\TestQuestions.txt"));
		String name= "";
		HashSet<String> quesId=new HashSet<String>();

		String line="";
		while ((line = br.readLine()) != null){
			quesId.add(line.trim());
		}
		br.close();

		final BufferedReader brUp=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\Tags\\upVotes.txt"));
		TreeMap<String, String> upVotes=new TreeMap<String,String>();
		while ((line = brUp.readLine()) != null){
			String[] split=line.split(",");
			upVotes.put(split[0].trim(),split[1].trim());
		}
		brUp.close();

		final BufferedReader brDown=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\Tags\\downVotes.txt"));
		TreeMap<String, String> downVotes=new TreeMap<String,String>();
		while ((line = brDown.readLine()) != null){
			String[] split=line.split(",");
			downVotes.put(split[0].trim(),split[1].trim());
		}
		brDown.close();		

		TreeMap<String, Integer> finalScore=new TreeMap<String, Integer>();
		ValueComparator bvc=new ValueComparator(finalScore);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

		for(String str:quesId){
			int score=0;
			String answerCount="",favCount="",commentCount="";
			try {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

				String sql="";
				//Updation
				//System.out.println("sql");
				sql="select * from testposts where Id=?";
				preparedStatement=connect.prepareStatement(sql);

				preparedStatement.setInt(1, Integer.parseInt(str));
				resultSet=preparedStatement.executeQuery();
				String Body="";
				//System.out.println(AnsId);
				while (resultSet.next()) {
					answerCount = resultSet.getString("AnswerCount");
					commentCount = resultSet.getString("CommentCount");
					favCount = resultSet.getString("FavoriteCount");
				}		
				// writeResultSet(resultSet);
				connect.close();
			} catch (Exception e) {
				throw e;
			}

			score=(3*Integer.parseInt(upVotes.get(str)))-(25*Integer.parseInt(downVotes.get(str)))+(10*Integer.parseInt(commentCount))
					+Integer.parseInt(answerCount)+Integer.parseInt(favCount);

			finalScore.put(str, score);
		}

		sorted_map.putAll(finalScore);
		int count=0;
		String body="",title="";
		
		for(Entry<String, Integer> entry: sorted_map.entrySet()){
			try {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

				String sql="";
				//Updation
				//System.out.println("sql");
				sql="select * from testposts where Id=?";
				preparedStatement=connect.prepareStatement(sql);

				preparedStatement.setInt(1, Integer.parseInt(entry.getKey()));
				resultSet=preparedStatement.executeQuery();
				
				//System.out.println(AnsId);
				while (resultSet.next()) {
					body = resultSet.getString("Body");
					title = resultSet.getString("Title");
				}		
				// writeResultSet(resultSet);
				connect.close();
			} catch (Exception e) {
				throw e;
			}
			
			PrintWriter outUp=new PrintWriter(new File("E:\\Research Projects\\CMU Emp. project\\Tags\\Top50\\"+entry.getKey()+".txt"));
			outUp.write(title+"\n");
			outUp.write(body);
			outUp.close();
			
			count++;
			if(count==50){
				break;
			}
		}
	}
	
	
	public static void getQuestions() throws Exception{
		final BufferedReader br=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\Tags\\scores.txt"));
		String name= "";
		HashSet<String> quesId=new HashSet<String>();

		String line="";
		while ((line = br.readLine()) != null){
			quesId.add(line.trim());
		}
		br.close();
		

		String body="",title="";
		for(String str:quesId){
			try {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

				String sql="";
				//Updation
				//System.out.println("sql");
				sql="select * from testposts where Id=?";
				preparedStatement=connect.prepareStatement(sql);

				preparedStatement.setInt(1, Integer.parseInt(str));
				resultSet=preparedStatement.executeQuery();
				
				//System.out.println(AnsId);
				while (resultSet.next()) {
					body = resultSet.getString("Body");
					title = resultSet.getString("Title");
				}		
				// writeResultSet(resultSet);
				connect.close();
			} catch (Exception e) {
				throw e;
			}
			
			PrintWriter outUp=new PrintWriter(new File("E:\\Research Projects\\CMU Emp. project\\Tags\\Top50\\"+str+".txt"));
			outUp.write(title+"\n");
			outUp.write(body);
			outUp.close();
			
		}
	}
	
	
	//Create a word doc for the top 50 questions
		public static void createDoc()throws Exception 
		{
			//Blank Document
			XWPFDocument document= new XWPFDocument(); 
			//Write the Document in file system
			FileOutputStream out = new FileOutputStream(
					new File("E:\\Research Projects\\CMU Emp. project\\Tags/Top50.docx"));
			//final PrintStream printStream = new PrintStream(out);

			final File folder = new File("E:\\Research Projects\\CMU Emp. project\\Tags\\Top50");
			final List<File> fileList = Arrays.asList(folder.listFiles());

			for(int i=0;i<fileList.size();i++)
			{
				final BufferedReader br = new BufferedReader(new FileReader(fileList.get(i))); 
				String line="";
				String content ="";

				XWPFParagraph paragraph = document.createParagraph();			

				//XSSFFont defaultFont= run.createFont();

				while ((line= br.readLine())!= null ) { 
					XWPFRun run=paragraph.createRun();
					//XWPFTable table = document.createTable();
					boolean className=false;
					if(line.toLowerCase().contains("assert")){
						run.setBold(true);
						run.setText(line);	
						run.addBreak();
					}
					else{
						if(line.contains("ClassName")){
							className=true;
						}
						run.setText(line);
						if(className){
							run.addBreak();
						}
						run.addBreak();
					}
					//				XWPFTableRow tableRowOne = table.getRow(0);
					//				tableRowOne.getCell(0).setText(line);
				}

				XWPFRun run1=paragraph.createRun();
				//run1.addBreak();
				run1.setText("====================");
				//run.addBreak();
				run1.setText("====================");
				//run1.addBreak();
				//run.addBreak();

				//printStream.print(content);
				//byte[] contentInBytes = content.getBytes(Charset.forName("UTF-8"));

				//content=content+"========================";
				//content=content+"========================";
				//out.write(contentInBytes);
				//out.flush();
				//break;
			}


			document.write(out);
			//printStream.close();
			out.close();

			System.out.println(
					"createdocument.docx written successully");
		}
}


