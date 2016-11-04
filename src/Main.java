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
		long debutDictionnaire = System.currentTimeMillis();
		Dictionnaire d = new Dictionnaire();
		d.creationDictionnaire(nom);
		System.out.println("Temps d'execution pour la création du dictionnaire : ");
		System.out.println(System.currentTimeMillis()-debutDictionnaire);
		System.out.println("Dictionnaire créé");
		
		//Indexation et Statistique
		long debutIndexation = System.currentTimeMillis();
		Indexation indexation = new Indexation(d, nom);
		indexation.creationIndex();
		System.out.println("Temps d'execution pour la création de l'index : ");
		System.out.println(System.currentTimeMillis()-debutIndexation);
		System.out.println("Indexation faite");
		
		//Lecture de la requête
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir une requête :");
		String str = sc.nextLine();
		//System.out.println("Vous avez saisi : " + str);
		
		//Optimisation de la requête
		long debutRequete = System.currentTimeMillis();
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
		System.out.println("Temps d'execution pour l'execution de la requete : ");
		System.out.println(System.currentTimeMillis()-debutRequete);
		System.out.println("**Fini**");
		
	}

}
