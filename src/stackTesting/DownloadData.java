package stackTesting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DownloadData {
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	public static void main(String[] args) throws Exception {
		//getData();
		//getDataLine();
		testData();
	}


	public static void getData() throws Exception{
		String line="";
		final BufferedReader br=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\stackoverflow.com-Posts\\Posts.xml"));
		String name= "";
		while ((line = br.readLine()) != null){
			try{
				//System.out.println(line);
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
						/*if(Integer.parseInt(rowId)<=6327609){
							continue;
						}*/
						//System.out.println(line);
						String PostTypeId= eElement.getAttribute("PostTypeId");
						String ParentID = eElement.getAttribute("ParentID");
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
						if(!Body.contains("test")||!Body.contains("Test")){
							continue;
						}
						String OwnerUserId=eElement.getAttribute("OwnerUserId");
						String LastEditorUserId=eElement.getAttribute("LastEditorUserId");
						String LastEditorDisplayName=eElement.getAttribute("LastEditorDisplayName");
						String LastEditDate=eElement.getAttribute("LastEditDate");
						String LastActivityDate=eElement.getAttribute("LastActivityDate");
						String CommunityOwnedDate=eElement.getAttribute("CommunityOwnedDate");
						String ClosedDate=eElement.getAttribute("ClosedDate");
						String Title=eElement.getAttribute("Title");
						String Tags=eElement.getAttribute("Tags");
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

	public static void getDataLine() throws Exception{
		String line="";
		//System.out.println(fileList.get(j));
		final BufferedReader br=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\stackoverflow.com-Posts\\Posts.xml"));
		String name= "";
		while ((line = br.readLine()) != null){
			System.out.println();
			System.out.println(line);
			if(line.contains("<row Id")){
				System.out.println(line.indexOf("row Id"));
				String rowId=(line.substring(line.indexOf("row Id"),line.indexOf("PostTypeId"))).trim().replace("row Id=", "").replace("\"", "");
				String PostTypeId="",AcceptedAnswerId="";
				if(line.contains("AcceptedAnswerId")){
					PostTypeId=line.substring(line.indexOf("PostTypeId"),line.indexOf("AcceptedAnswerId")).trim().replace("PostTypeId=", "").replace("\"", "");
				}
				else{
					PostTypeId=line.substring(line.indexOf("PostTypeId"),line.indexOf("CreationDate")).trim().replace("PostTypeId=", "").replace("\"", "");
				}
				if(line.contains("AcceptedAnswerId")){
					AcceptedAnswerId=line.substring(line.indexOf("AcceptedAnswerId"),line.indexOf("CreationDate")).trim().replace("AcceptedAnswerId=", "").replace("\"", "");
				}
				String CreationDate=line.substring(line.indexOf("CreationDate"),line.indexOf("Score")).trim().replace("CreationDate=", "").replace("\"", "");
				String Score=line.substring(line.indexOf("Score"),line.indexOf("ViewCount")).trim().replace("Score=", "").replace("\"", "");
				String ViewCount=line.substring(line.indexOf("ViewCount"),line.indexOf("Body")).trim().replace("ViewCount=", "").replace("\"", "");
				String Body=line.substring(line.indexOf("Body"),line.indexOf("OwnerUserId")).trim().replace("Body=\"", "").
						replace("\"", "").replaceAll("&lt;", " ").replaceAll("p&gt;", " ").replaceAll("&#xA;", " ")
						.replaceAll("&gt;", " ").replaceAll("/code", " ").replaceAll("/", "");
				String OwnerUserId=line.substring(line.indexOf("OwnerUserId"),line.indexOf("LastEditorUserId")).trim().replace("OwnerUserId=", "").replace("\"", "");
				String LastEditorUserId=line.substring(line.indexOf("LastEditorUserId"),line.indexOf("LastEditorDisplayName")).trim().replace("LastEditorUserId=", "").replace("\"", "");
				String LastEditorDisplayName=line.substring(line.indexOf("LastEditorDisplayName"),line.indexOf("LastEditDate")).trim().replace("LastEditorDisplayName=", "").replace("\"", "");
				String LastEditDate=line.substring(line.indexOf("LastEditDate"),line.indexOf("LastActivityDate")).trim().replace("LastEditDate=", "").replace("\"", "");
				String LastActivityDate=line.substring(line.indexOf("LastActivityDate"),line.indexOf("Title")).trim().replace("LastActivityDate=", "").replace("\"", "");
				String Title=line.substring(line.indexOf("Title"),line.indexOf("Tags")).trim().replace("Title=", "").replace("\"", "");
				String Tags=line.substring(line.indexOf("Tags"),line.indexOf("AnswerCount")).trim().replace("Tags=", "").replace("\"", "")
						.replaceAll("&lt;", " ").replaceAll("&gt;", " ");
				String AnswerCount=line.substring(line.indexOf("AnswerCount"),line.indexOf("CommentCount")).trim().replace("AnswerCount=", "").replace("\"", "");
				String CommentCount=line.substring(line.indexOf("CommentCount"),line.indexOf("FavoriteCount")).trim().replace("CommentCount=", "").replace("\"", "");
				String FavoriteCount="",CommunityOwnedDate="";
				if(line.contains("CommunityOwnedDate")){
					FavoriteCount=line.substring(line.indexOf("FavoriteCount"),line.indexOf("CommunityOwnedDate")).trim().replace("FavoriteCount=", "").replace("\"", "");
					CommunityOwnedDate=line.substring(line.indexOf("CommunityOwnedDate"),line.indexOf("/>",line.indexOf("CommunityOwnedDate"))).trim().replace("CommunityOwnedDate=", "").replace("\"", "");
				}else{
					FavoriteCount=line.substring(line.indexOf("FavoriteCount"),line.indexOf("/>")).trim().replace("FavoriteCount=", "").replace("\"", "");
				}

				System.out.println(Body);
				/*inputData(rowId, PostTypeId, AcceptedAnswerId, CreationDate, Score, ViewCount, Body,
						OwnerUserId, LastEditorUserId, LastEditorDisplayName, LastEditDate, LastActivityDate, 
						Title, Tags, AnswerCount, CommentCount, FavoriteCount, CommunityOwnedDate);*/
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
			sql="Insert into stackoverflownew.posts values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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


	//Checking the file Votes.xml
	public static void testData() throws Exception{
		String line="";
		final BufferedReader br=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\stackoverflow.com-Votes\\Votes.xml"));
		String name= "";
		while ((line = br.readLine()) != null){
			try{
				System.out.println(line);
//				if(line.contains("<row Id")){
//					File file=new File("E:/Research Projects/CMU Emp. project\\stackoverflow.com-Votes/temp.xml");
//					FileWriter fstream = new FileWriter(file, false); //true tells to append data.
//					BufferedWriter out = new BufferedWriter(fstream);
//					//out.write("bugcoverageabove20<- c(");
//					out.write(line);
//					out.close();
//					System.exit(1);
//				}
			}
			catch (Exception e) {
				throw e;
			}
		}
	}
}
