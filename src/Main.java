import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) throws IOException {
		String nom = "resultat.txt";
		
		//Dictionnaire
		Dictionnaire d = new Dictionnaire();
		d.creationDictionnaire(nom);
		System.out.println("Dictionnaire créé");
		
		//Indexation et Statistique
		Indexation indexation = new Indexation(d, nom);
		indexation.creationIndex();
		System.out.println("Indexation faite");
		
		//Lecture de la requête
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir une requête :");
		String str = sc.nextLine();
		//System.out.println("Vous avez saisi : " + str);
		
		//Optimisation de la requête
		Requete requete = new Requete(str, d, indexation);
		boolean b = requete.parsageRequete();
		if(b){
			ArrayList<String> resultat = requete.evaluationRequete(null);
			if(resultat != null){
				for(int i = 0; i < resultat.size(); i++){
					System.out.println(resultat.get(i));
				}
			}

		}
		System.out.println("**Fini**");
		
	}

}
