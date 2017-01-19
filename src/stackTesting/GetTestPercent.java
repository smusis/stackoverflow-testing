package stackTesting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GetTestPercent {
	public static void main(String[] args) throws Exception {
		getData();
	}

	public static void getData() throws Exception{
		String line="";
		final BufferedReader br=new BufferedReader(new FileReader("E:\\Research Projects\\CMU Emp. project\\stackoverflow.com-Posts\\Posts.xml"));
		String name= "";
		ArrayList<String> quesId=new ArrayList<String>();
		TreeMap<String, Integer> testQuestions=new TreeMap<String, Integer>();
		TreeMap<String, Integer> allQuestions=new TreeMap<String, Integer>();

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
						//System.out.println(rowId);

						String PostTypeId= eElement.getAttribute("PostTypeId");
						String ParentID = eElement.getAttribute("ParentId");
						String CreationDate=eElement.getAttribute("CreationDate");

						String Tags=eElement.getAttribute("Tags");

						boolean lang=false;
						String str[]=Tags.split(">");
						for(int z=0;z<str.length;z++){
							//System.out.println(str[z]);
							if(str[z].replaceAll("<","").replaceAll(">","").contains("test")){
								lang=true;
								if(!quesId.contains(rowId.trim())){
									quesId.add(rowId.trim());
									//System.exit(1);
								}
							}
						}
						//System.exit(1);
						//System.out.println(PostTypeId);
						//if(PostTypeId.equals("1") && !lang){
						//	continue;
						//}

						//if(PostTypeId.equals("2") && !quesId.contains(ParentID.trim())){
						//	System.out.println("continue");
						//	continue;
						//}
						
						if(PostTypeId.equals("2")){
							continue;
						}
						
						//System.out.println(CreationDate);
						//System.out.println(PostTypeId);

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

						//System.out.println(temp);
						if(temp.equals("")){
							continue;
						}

						//System.out.println(temp);
						if(allQuestions.containsKey(temp)){
							allQuestions.put(temp, allQuestions.get(temp)+1);
						}
						else{
							allQuestions.put(temp, 1);
						}

						if(lang){
							if(testQuestions.containsKey(temp)){
								testQuestions.put(temp, testQuestions.get(temp)+1);
							}
							else{
								testQuestions.put(temp, 1);
							}
						}
					}
				}	
			}
			catch(Exception e){
				continue;
			}

			//System.out.println("All "+allQuestions.size());
			//System.out.println("Test "+testQuestions.size());
			
		}

		PrintWriter outUp=new PrintWriter(new File("E:/Research Projects/CMU Emp. project/Tags/Percent.txt"));

		outUp.write("All Questions\n");
		for(Entry<String, Integer> entry:allQuestions.entrySet()){
			outUp.write(entry.getKey()+","+entry.getValue()+"\n");
		}

		outUp.write("\nTest Questions\n");
		for(Entry<String, Integer> entry:testQuestions.entrySet()){
			outUp.write(entry.getKey()+","+entry.getValue()+"\n");
		}

		outUp.close();
	}
}
