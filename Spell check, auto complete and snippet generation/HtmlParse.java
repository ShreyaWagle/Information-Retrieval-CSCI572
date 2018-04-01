import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import org.apache.tika.language.LanguageIdentifier;

public class HtmlParse {

	public static void main(final String[] args) throws IOException,
			SAXException, TikaException {
		
		
		String dirPath = "C:\\Users\\shrey\\Downloads\\WP\\WP\\";
		File dir = new File(dirPath);
		int count = 0;		

		
		File opFile = new File("big.txt");
	    // creates the file
	    opFile.createNewFile();
	    // creates a FileWriter Object
	    FileWriter writer = new FileWriter(opFile);
		
		// detecting the file type
		

		
		for(File file: dir.listFiles()){
			//System.out.println(dirPath+"\\\\"+file.getName());
			
			BodyContentHandler handler = new BodyContentHandler(-1);
			Metadata metadata = new Metadata();
			FileInputStream inputstream = new FileInputStream(new File(dirPath+"\\\\"+file.getName()));
			ParseContext pcontext = new ParseContext();

			// Html parser
			HtmlParser htmlparser = new HtmlParser();
			htmlparser.parse(inputstream, handler, metadata, pcontext);
			System.out.println("Contents of the document:" + handler.toString().replaceAll("\\s+", " "));
			LanguageIdentifier object = new LanguageIdentifier(handler.toString());
			//System.out.println(object.getLanguage());
			
			if(object.getLanguage().equals("en")){
			writer.write(handler.toString().replaceAll("\\s+", " "));
			writer.write("\n");
			}
			
			System.out.println(++count);
		}
        
		/*
		FileInputStream inputstream = new FileInputStream(new File("./0001cdf5-6966-4649-92f2-2092d26d958f.html"));
		ParseContext pcontext = new ParseContext();

		// Html parser
		HtmlParser htmlparser = new HtmlParser();
		htmlparser.parse(inputstream, handler, metadata, pcontext);
		System.out.println("Contents of the document:" + handler.toString().replaceAll("\\s+", " "));
		writer.write(handler.toString().replaceAll("\\s+", " "));
		*/
		
		
		
		writer.flush();
	    writer.close();
		
		/*
		System.out.println("Metadata of the document:");
		String[] metadataNames = metadata.names();

		for (String name : metadataNames) {
			System.out.println(name + ":   " + metadata.get(name));
		}
		*/
		
	}
}