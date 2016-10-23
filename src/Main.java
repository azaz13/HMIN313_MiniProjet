import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class Main {

	
	public static void main(String[] args) throws IOException {
		
		//Indexation
		Dictionnaire d = new Dictionnaire();
		d.creationDictionnaire("resultat.txt");
		System.out.println("Sujet value " + d.getDictionnaireByKey(0));
		System.out.println("Sujet key " + d.getDictionnaireByValue("http://www.University0.edu"));
		System.out.println("*************************");
		System.out.println("Predicat value " + d.getPredicatByKey("p0"));
		System.out.println("Predicat key " + d.getPredicatByValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
		
		//
		
		
	}

}
