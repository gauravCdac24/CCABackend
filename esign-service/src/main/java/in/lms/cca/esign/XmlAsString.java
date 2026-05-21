package in.lms.cca.esign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class XmlAsString {
	
	public String xmlAsString(String path) {
		// our XML file for this example
		File xmlFile = new File(path);
		String xml2String = null;
		
		try {
			// Let's get XML file as String using BufferedReader
			// FileReader uses platform's default character encoding
			// if you need to specify a different encoding, use InputStreamReader
			
			Reader fileReader = new FileReader(xmlFile);
			BufferedReader bufReader = new BufferedReader(fileReader);

			StringBuilder sb = new StringBuilder();
			String line = bufReader.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = bufReader.readLine();
			}
			xml2String = sb.toString();
			System.out.println("XML to String using BufferedReader : ");
			//System.out.println(xml2String);

			bufReader.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return xml2String;

	}
}