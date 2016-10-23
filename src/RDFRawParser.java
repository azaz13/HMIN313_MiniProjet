import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

public final class RDFRawParser {
	
	static FileWriter fw; 
	static BufferedWriter bw;
	static PrintWriter fichierSortie; 
	
	private static class RDFListener extends RDFHandlerBase {

		@Override
		public void handleStatement(Statement st) {
			System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate() + "\t "
					+ st.getObject());
			fichierSortie.print(st.getSubject() + "\t" + st.getPredicate() + "\t"
					+ st.getObject() + "\n");
		}

	};

	public static void main(String args[]) throws IOException {

		fw = new FileWriter("resultat.txt");
		bw = new BufferedWriter(fw);
		fichierSortie = new PrintWriter(bw);
				
		Reader reader = new FileReader(
				"University0_0.owl");

		org.openrdf.rio.RDFParser rdfParser = Rio
				.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());
		try {
			rdfParser.parse(reader, "");
		} catch (Exception e) {

		}

		try {
			reader.close();
			fichierSortie.close();
		} catch (IOException e) {
		}

	}

}