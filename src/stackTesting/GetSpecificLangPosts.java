package stackTesting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.text.html.HTML.Tag;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GetSpecificLangPosts {
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private static ResultSet resultSet1 = null;
	public static void main(String[] args) throws Exception {
		//getData();
		//getTestFiles();
		//getTop2000TestFiles();
		//getTemporalTestFiles();
		//getMobileTestFiles();
		getTestTagsFiles();
	}

	//Extract Q&As from the xml dump file
	public static void getData() throws Exception{
		String line="";
		final BufferedReader br=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\stackoverflow.com-Posts\\Posts.xml"));
		String name= "";
		ArrayList<String> quesId=new ArrayList<String>();

		while ((line = br.readLine()) != null){
			//System.out.println(line);
			try{
				if(line.contains("<row Id")){
					File file=new File("E:/Research Projects/CMU Emp. project\\stackoverflow.com-Posts/temp.xml");
					FileWriter fstream = new FileWriter(file, false); //true tells to append data.
					BufferedWriter out = new BufferedWriter(fstream);
					//out.write("bugcoverageabove20<- c(");
					out.write(line);
					out.close();


					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					dbf.setNamespaceAware(true); 
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.parse("E:\\Research Projects\\CMU Emp. project\\stackoverflow.com-Posts\\temp.xml");
					Element root = doc.getDocumentElement();

					NodeList nodeList = doc.getElementsByTagName("row");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						Element eElement = (Element) node;

						//System.out.println(nodeList.getLength());
						String rowId= eElement.getAttribute("Id");
						System.out.println(rowId);
						/*if(Integer.parseInt(rowId)==2117747){
							continue;
						}*/
						/*if(Integer.parseInt(rowId)<=12624252){
							continue;
						}*/

						String PostTypeId= eElement.getAttribute("PostTypeId");
						String ParentID = eElement.getAttribute("ParentId");
						String AcceptedAnswerId= eElement.getAttribute("AcceptedAnswerId");
						String CreationDate=eElement.getAttribute("CreationDate");
						String Score=eElement.getAttribute("Score");
						String ViewCount=eElement.getAttribute("ViewCount");
						String Body=eElement.getAttribute("Body").replaceAll("<p>", " ").replaceAll("</p>"," ")
								.replaceAll("<code>"," ").replaceAll("</code>", " ").replaceAll("<blockquote>"," ")
								.replaceAll("</blockquote>"," ").replaceAll("<pre>", " ").replaceAll("</pre>", " ")
								.replaceAll("<ul>"," ").replaceAll("</ul>"," ").replaceAll("<li>", " ").replaceAll("</li>", " ")
								.replaceAll("<strong>", " ").replaceAll("</strong>"," ").replaceAll("&lt;", " ")
								.replaceAll("<P>"," ").replaceAll("</P>"," ").replaceAll("<h1>"," ").replaceAll("</h1>"," ")
								.replaceAll("&gt;"," ").replaceAll("<em>"," ").replaceAll("</em>"," ").replaceAll("<br>"," ")
								.replaceAll("&amp", " ").replaceAll("<ol>", " ").replaceAll("</ol>", " ").replaceAll("<hr>", " ");
						String OwnerUserId=eElement.getAttribute("OwnerUserId");
						String LastEditorUserId=eElement.getAttribute("LastEditorUserId");
						String LastEditorDisplayName=eElement.getAttribute("LastEditorDisplayName");
						String LastEditDate=eElement.getAttribute("LastEditDate");
						String LastActivityDate=eElement.getAttribute("LastActivityDate");
						String CommunityOwnedDate=eElement.getAttribute("CommunityOwnedDate");
						String ClosedDate=eElement.getAttribute("ClosedDate");
						String Title=eElement.getAttribute("Title");
						String Tags=eElement.getAttribute("Tags");

						boolean lang=false;
						String str[]=Tags.split(">");
						for(int z=0;z<str.length;z++){

							if(str[z].replaceAll("<","").replaceAll(">","").equals("java")||
									str[z].replaceAll("<","").replaceAll(">","").equals("c#")){
								lang=true;
								if(!quesId.contains(rowId.trim())){
									quesId.add(rowId.trim());
								}
							}
						}
						//System.out.println(PostTypeId);
						if(PostTypeId.equals("1") && !lang){
							continue;
						}

						if(PostTypeId.equals("2") && !quesId.contains(ParentID.trim())){
							continue;
						}
						//System.out.println(line);
						//System.out.println(Tags);
						String AnswerCount=eElement.getAttribute("AnswerCount");
						String CommentCount=eElement.getAttribute("CommentCount");
						String FavoriteCount=eElement.getAttribute("FavoriteCount");

						inputData(rowId, PostTypeId, ParentID, AcceptedAnswerId, CreationDate, Score, ViewCount, Body, 
								OwnerUserId, LastEditorUserId, LastEditorDisplayName, LastEditDate, LastActivityDate, 
								CommunityOwnedDate, ClosedDate, Title, Tags, AnswerCount, CommentCount, FavoriteCount);
						//System.out.println(Body);
						//System.exit(1);
					}
				}	
			}
			catch(Exception e){
				continue;
			}
		}

	}

	public static void inputData(String rowId,String PostTypeId, String ParentID, String AcceptedAnswerId,String CreationDate,
			String Score,String ViewCount,String Body,String OwnerUserId,String LastEditorUserId,
			String LastEditorDisplayName,String LastEditDate,String LastActivityDate,String CommunityOwnedDate, 
			String ClosedDate,String Title,String Tags, String AnswerCount,String CommentCount,String FavoriteCount) throws Exception{
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

			String sql="";

			//Updation
			sql="Insert into stackoverflownew.langposts1 values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			preparedStatement=connect.prepareStatement(sql);

			//System.out.println(AcceptedAnswerId);
			preparedStatement.setInt(1, Integer.parseInt(rowId));
			preparedStatement.setInt(2, Integer.parseInt(PostTypeId));
			preparedStatement.setString(3, (ParentID));
			preparedStatement.setString(4, (AcceptedAnswerId));
			preparedStatement.setString(5, CreationDate);
			preparedStatement.setString(6, (Score));
			preparedStatement.setString(7, (ViewCount));
			preparedStatement.setString(8, Body);
			preparedStatement.setString(9, (OwnerUserId));
			preparedStatement.setString(10, (LastEditorUserId));
			preparedStatement.setString(11, LastEditorDisplayName);
			preparedStatement.setString(12, LastEditDate);
			preparedStatement.setString(13, LastActivityDate);
			preparedStatement.setString(14, CommunityOwnedDate);
			preparedStatement.setString(15, ClosedDate);
			preparedStatement.setString(16, Title);
			preparedStatement.setString(17, Tags);
			preparedStatement.setString(18, (AnswerCount));
			preparedStatement.setString(19, (CommentCount));
			preparedStatement.setString(20, (FavoriteCount));

			// Result set get the result of the SQL query
			preparedStatement
			.executeUpdate();

			// writeResultSet(resultSet);
			connect.close();



		} catch (Exception e) {
			throw e;
		}
	}

	//Get All the questions which contain word test & get accepted answers for those ques
	public static void getTestFiles() throws Exception{
		try {
			for(int i=1;i<28922943;i=i+1000){
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

				String sql="";
				//Updation
				sql="select Id,Body,AcceptedAnswerId from langposts1 where PostTypeId=1 and Id>="+i+" and Id<="+(i+1000);

				statement = connect.createStatement();
				// Result set get the result of the SQL query
				resultSet = statement
						.executeQuery(sql);
				while (resultSet.next()) {
					String QuesId = resultSet.getString("Id");
					String Body = resultSet.getString("Body");
					//System.out.println(Body);
					if(Body.contains("test")|| Body.contains("Test")){
						String AcceptedAnswerId = resultSet.getString("AcceptedAnswerId");
						String AnsBody="";
						if(!AcceptedAnswerId.equals("") && !AcceptedAnswerId.equals(null)){
							AnsBody=getAnswerFiles(AcceptedAnswerId);
						}
						if(!AnsBody.equals("")){
							File file=new File("E:/Research Projects/CMU Emp. project/Test Questions/"+QuesId+".txt");
							FileWriter fstream = new FileWriter(file, false); //true tells to append data.
							BufferedWriter out = new BufferedWriter(fstream);
							//out.write("bugcoverageabove20<- c(");
							out.write(Body);
							out.write("\n");
							out.write(AnsBody);
							out.close();
						}
					}
				}		
				// writeResultSet(resultSet);
				connect.close();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	//Get the body of answers by giving answer id
	public static String getAnswerFiles(String AnsId) throws Exception{
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

			String sql="";
			//Updation
			System.out.println("sql");
			sql="select * from langposts1 where Id=?";
			preparedStatement=connect.prepareStatement(sql);

			preparedStatement.setInt(1, Integer.parseInt(AnsId.trim()));
			resultSet1=preparedStatement.executeQuery();
			String Body="";
			System.out.println(AnsId);
			while (resultSet1.next()) {
				Body = resultSet1.getString("Body");
			}		
			// writeResultSet(resultSet);
			connect.close();
			return Body;
		} catch (Exception e) {
			throw e;
		}
	}

	//Get top 2000 test files
	public static void getTop2000TestFiles() throws Exception{
		final File folder = new File("E:/Research Projects/CMU Emp. project/Test Questions");
		final List<File> fileList = Arrays.asList(folder.listFiles());
		String line="";

		ArrayList<String> viewCount=new ArrayList<String>();
		TreeMap<String, Integer> viewCountID=new TreeMap<String, Integer>();
		for(int i=0;i<fileList.size();i++)
		{
			String[] strSplit=fileList.get(i).toString().split("\\\\");
			viewCount.add(strSplit[strSplit.length-1].replace(".txt", ""));
		}

		try {
			for (String str:viewCount) {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

				String sql="";
				//Updation
				sql="select AcceptedAnswerId,ViewCount from langposts1 where PostTypeId=1 and Id ="+str;

				statement = connect.createStatement();
				// Result set get the result of the SQL query
				resultSet = statement
						.executeQuery(sql);
				while (resultSet.next()) {
					String ViewCount = resultSet.getString("ViewCount");
					String AcceptedAnswerId = resultSet.getString("AcceptedAnswerId");
					viewCountID.put(str+"=="+AcceptedAnswerId, Integer.parseInt(ViewCount));
				}		
				// writeResultSet(resultSet);
				connect.close();
			}
		} catch (Exception e) {
			throw e;
		}

		ValueComparator bvc =  new ValueComparator(viewCountID);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		sorted_map.putAll(viewCountID);

		int count=0;
		for (Map.Entry<String, Integer> entry : sorted_map.entrySet()) {
			String[] ansSplit=entry.getKey().split("==");
			String quesId=ansSplit[0];
			String ansId=ansSplit[1];
			String Body=getAnswerFiles(ansId);
			File file=new File("E:/Research Projects/CMU Emp. project/Top2000/"+quesId+".txt");
			FileWriter fstream = new FileWriter(file, false); //true tells to append data.
			BufferedWriter out = new BufferedWriter(fstream);
			//out.write("bugcoverageabove20<- c(");
			out.write(Body);
			out.close();
			count++;
			if(count==2000){
				System.exit(1);
			}
		}
	}
	
	//Get test files divided by time line
		public static void getTemporalTestFiles() throws Exception{
			final File folder = new File("E:/Research Projects/CMU Emp. project/Test Questions");
			final List<File> fileList = Arrays.asList(folder.listFiles());
			String line="";

			ArrayList<String> quesID=new ArrayList<String>();
			for(int i=0;i<fileList.size();i++)
			{
				String[] strSplit=fileList.get(i).toString().split("\\\\");
				quesID.add(strSplit[strSplit.length-1].replace(".txt", ""));
			}
			
			try {
				for (String str:quesID) {
					// This will load the MySQL driver, each DB has its own driver
					Class.forName("com.mysql.jdbc.Driver");
					// Setup the connection with the DB
					connect = DriverManager
							.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

					String sql="";
					//Updation
					sql="select AcceptedAnswerId,Body,CreationDate from langposts1 where PostTypeId=1 and Id ="+str;

					statement = connect.createStatement();
					// Result set get the result of the SQL query
					resultSet = statement
							.executeQuery(sql);
					while (resultSet.next()) {
						String Body = resultSet.getString("Body");
						String AcceptedAnswerId = resultSet.getString("AcceptedAnswerId");
						String CreationDate = resultSet.getString("CreationDate");
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
						Date date = format.parse(CreationDate);
						String temp="";
						if(date.after(format.parse("2009-01-01T00:00:00.000"))&&date.before(format.parse("2009-06-30T23:59:59.000"))){
							temp="Jan-Jun09";
						}
						if(date.after(format.parse("2009-07-01T00:00:00.000"))&&date.before(format.parse("2009-12-31T23:59:59.000"))){
							temp="Jul-Dec09";
						}
						if(date.after(format.parse("2010-01-01T00:00:00.000"))&&date.before(format.parse("2010-06-30T23:59:59.000"))){
							temp="Jan-Jun10";
						}
						if(date.after(format.parse("2010-07-01T00:00:00.000"))&&date.before(format.parse("2010-12-31T23:59:59.000"))){
							temp="Jul-Dec10";
						}
						if(date.after(format.parse("2011-01-01T00:00:00.000"))&&date.before(format.parse("2011-06-30T23:59:59.000"))){
							temp="Jan-Jun11";
						}
						if(date.after(format.parse("2011-07-01T00:00:00.000"))&&date.before(format.parse("2011-12-31T23:59:59.000"))){
							temp="Jul-Dec11";
						}
						if(date.after(format.parse("2012-01-01T00:00:00.000"))&&date.before(format.parse("2012-06-30T23:59:59.000"))){
							temp="Jan-Jun12";
						}
						if(date.after(format.parse("2012-07-01T00:00:00.000"))&&date.before(format.parse("2012-12-31T23:59:59.000"))){
							temp="Jul-Dec12";
						}
						if(date.after(format.parse("2013-01-01T00:00:00.000"))&&date.before(format.parse("2013-06-30T23:59:59.000"))){
							temp="Jan-Jun13";
						}
						if(date.after(format.parse("2013-07-01T00:00:00.000"))&&date.before(format.parse("2013-12-31T23:59:59.000"))){
							temp="Jul-Dec13";
						}
						if(date.after(format.parse("2014-01-01T00:00:00.000"))&&date.before(format.parse("2014-06-30T23:59:59.000"))){
							temp="Jan-Jun14";
						}
						if(date.after(format.parse("2014-07-01T00:00:00.000"))&&date.before(format.parse("2014-12-31T23:59:59.000"))){
							temp="Jul-Dec14";
						}
						
						if(temp.equals("")){
							continue;
						}
						String AnsBody=getAnswerFiles(AcceptedAnswerId);
						File file=new File("E:/Research Projects/CMU Emp. project/Temporal/"+temp+"/"+str+".txt");
						FileWriter fstream = new FileWriter(file, false); //true tells to append data.
						BufferedWriter out = new BufferedWriter(fstream);
						//out.write("bugcoverageabove20<- c(");
						out.write(Body);
						out.write("\n");
						out.write(AnsBody);
						out.close();
					}		
					// writeResultSet(resultSet);
					connect.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
			
		//Get the test files which have tags related to mobile development & also divide the questions based on time
		public static void getMobileTestFiles() throws Exception{
			final File folder = new File("E:/Research Projects/CMU Emp. project/Test Questions");
			final List<File> fileList = Arrays.asList(folder.listFiles());
			String line="";

			ArrayList<String> quesID=new ArrayList<String>();
			for(int i=0;i<fileList.size();i++)
			{
				String[] strSplit=fileList.get(i).toString().split("\\\\");
				quesID.add(strSplit[strSplit.length-1].replace(".txt", ""));
			}
			
			try {
				for (String str:quesID) {
					// This will load the MySQL driver, each DB has its own driver
					Class.forName("com.mysql.jdbc.Driver");
					// Setup the connection with the DB
					connect = DriverManager
							.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

					String sql="";
					//Updation
					sql="select AcceptedAnswerId,Body,Tags,CreationDate from langposts1 where PostTypeId=1 and Id ="+str;

					statement = connect.createStatement();
					// Result set get the result of the SQL query
					resultSet = statement
							.executeQuery(sql);
					while (resultSet.next()) {
						String Body = resultSet.getString("Body");
						String AcceptedAnswerId = resultSet.getString("AcceptedAnswerId");
						String Tags = resultSet.getString("Tags");
						String[] split=Tags.split("><");
						System.out.println(str);
						boolean mob=false;
						for(int j=0;j<split.length;j++){
							String tagName=split[j].replaceAll("<", "").replaceAll(">", "").trim();
							if(tagName.equals("android")||tagName.equals("bada")||tagName.equals("blackberry")
									||tagName.equals("iphone")||tagName.equals("ios")||tagName.equals("java-me")||
									tagName.equals("phonegap")||tagName.equals("symbian")||tagName.equals("tizen")||
									tagName.equals("webos")||tagName.equals("windows-phone"))
							mob=true;
						}
						if(!mob){
							continue;
						}
						
						String AnsBody=getAnswerFiles(AcceptedAnswerId);
						File file=new File("E:/Research Projects/CMU Emp. project/Mobile/"+str+".txt");
						FileWriter fstream = new FileWriter(file, false); //true tells to append data.
						BufferedWriter out = new BufferedWriter(fstream);
						//out.write("bugcoverageabove20<- c(");
						out.write(Body);
						out.write("\n");
						out.write(AnsBody);
						out.close();
						
						String CreationDate = resultSet.getString("CreationDate");
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
						Date date = format.parse(CreationDate);
						String temp="";
						if(date.after(format.parse("2009-01-01T00:00:00.000"))&&date.before(format.parse("2009-06-30T23:59:59.000"))){
							temp="Jan-Jun09";
						}
						if(date.after(format.parse("2009-07-01T00:00:00.000"))&&date.before(format.parse("2009-12-31T23:59:59.000"))){
							temp="Jul-Dec09";
						}
						if(date.after(format.parse("2010-01-01T00:00:00.000"))&&date.before(format.parse("2010-06-30T23:59:59.000"))){
							temp="Jan-Jun10";
						}
						if(date.after(format.parse("2010-07-01T00:00:00.000"))&&date.before(format.parse("2010-12-31T23:59:59.000"))){
							temp="Jul-Dec10";
						}
						if(date.after(format.parse("2011-01-01T00:00:00.000"))&&date.before(format.parse("2011-06-30T23:59:59.000"))){
							temp="Jan-Jun11";
						}
						if(date.after(format.parse("2011-07-01T00:00:00.000"))&&date.before(format.parse("2011-12-31T23:59:59.000"))){
							temp="Jul-Dec11";
						}
						if(date.after(format.parse("2012-01-01T00:00:00.000"))&&date.before(format.parse("2012-06-30T23:59:59.000"))){
							temp="Jan-Jun12";
						}
						if(date.after(format.parse("2012-07-01T00:00:00.000"))&&date.before(format.parse("2012-12-31T23:59:59.000"))){
							temp="Jul-Dec12";
						}
						if(date.after(format.parse("2013-01-01T00:00:00.000"))&&date.before(format.parse("2013-06-30T23:59:59.000"))){
							temp="Jan-Jun13";
						}
						if(date.after(format.parse("2013-07-01T00:00:00.000"))&&date.before(format.parse("2013-12-31T23:59:59.000"))){
							temp="Jul-Dec13";
						}
						if(date.after(format.parse("2014-01-01T00:00:00.000"))&&date.before(format.parse("2014-06-30T23:59:59.000"))){
							temp="Jan-Jun14";
						}
						if(date.after(format.parse("2014-07-01T00:00:00.000"))&&date.before(format.parse("2014-12-31T23:59:59.000"))){
							temp="Jul-Dec14";
						}
						
						if(temp.equals("")){
							continue;
						}
						
						File file1=new File("E:/Research Projects/CMU Emp. project/MobileTemporal/"+temp+"/"+str+".txt");
						FileWriter fstream1 = new FileWriter(file1, false); //true tells to append data.
						BufferedWriter out1 = new BufferedWriter(fstream1);
						//out.write("bugcoverageabove20<- c(");
						out1.write(Body);
						out1.write("\n");
						out1.write(AnsBody);
						out1.close();
					}		
					// writeResultSet(resultSet);
					connect.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		
		
		public static void getTestTagsFiles() throws Exception{
			final File folder = new File("E:/Research Projects/CMU Emp. project/Test Questions");
			final List<File> fileList = Arrays.asList(folder.listFiles());
			String line="";

			ArrayList<String> quesID=new ArrayList<String>();
			for(int i=0;i<fileList.size();i++)
			{
				String[] strSplit=fileList.get(i).toString().split("\\\\");
				quesID.add(strSplit[strSplit.length-1].replace(".txt", ""));
			}
			
			try {
				for (String str:quesID) {
					// This will load the MySQL driver, each DB has its own driver
					Class.forName("com.mysql.jdbc.Driver");
					// Setup the connection with the DB
					connect = DriverManager
							.getConnection("jdbc:mysql://localhost/stackoverflownew","root","mysql");

					String sql="";
					//Updation
					sql="select AcceptedAnswerId,Body,Tags,CreationDate from langposts1 where PostTypeId=1 and Id ="+str;

					statement = connect.createStatement();
					// Result set get the result of the SQL query
					resultSet = statement
							.executeQuery(sql);
					while (resultSet.next()) {
						String Body = resultSet.getString("Body");
						String AcceptedAnswerId = resultSet.getString("AcceptedAnswerId");
						String Tags = resultSet.getString("Tags");
						String[] split=Tags.split("><");
						System.out.println(str);
						boolean mob=false;
						for(int j=0;j<split.length;j++){
							String tagName=split[j].replaceAll("<", "").replaceAll(">", "").trim();
							if(tagName.contains("test"))
							mob=true;
						}
						if(!mob){
							continue;
						}
						
						String AnsBody=getAnswerFiles(AcceptedAnswerId);
						File file=new File("E:/Research Projects/CMU Emp. project/TestTags/"+str+".txt");
						FileWriter fstream = new FileWriter(file, false); //true tells to append data.
						BufferedWriter out = new BufferedWriter(fstream);
						//out.write("bugcoverageabove20<- c(");
						out.write(Body);
						out.write("\n");
						out.write(AnsBody);
						out.close();
					}		
					// writeResultSet(resultSet);
					connect.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
}

class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;
	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.    
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}