import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class Main {

	
	public static void main(String[] args) throws IOException {
		String nom = "resultat.txt";
		
		//Dictionnaire
		Dictionnaire d = new Dictionnaire();
		d.creationDictionnaire(nom);
		/*System.out.println("Sujet value " + d.getDictionnaireByKey(0));
		System.out.println("Sujet key " + d.getDictionnaireByValue("http://www.University0.edu"));
		System.out.println("*************************");
		System.out.println("Predicat value " + d.getPredicatByKey("p0"));
		System.out.println("Predicat key " + d.getPredicatByValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));*/
		System.out.println("Dictionnaire créé");
		
		//Indexation
		Indexation index = new Indexation(d, nom);
		index.creationIndex();
		ArrayList<ArrayList<Integer>> os = index.getTabByPredicat("p0");
		System.out.println("Indexation faite");

		/*System.out.println("taille os " + os.size());
		for(int i = 0; i < os.size(); i++){
			System.out.println(os.get(i) + " ");
		}*/
		
		//Recherche en fonction d'un predicat et d'un objet
		/*ArrayList<Integer> sujet = index.rechercheByPredicatObjet("p0", 1);
		for(int i = 0; i < sujet.size(); i++){
			System.out.println(sujet.get(i) + " ");

		}*/
		
		//Statistique
		//index.creationStatistique();
		
		ArrayList<ArrayList<Integer>> liste = index.rechercheByPredicatInStat("p0");
		System.out.println(liste.size());
		for(int i = 0; i < liste.size(); i++){
			System.out.println(liste.get(i) + " ");
		}
		
		
		
	}

}
