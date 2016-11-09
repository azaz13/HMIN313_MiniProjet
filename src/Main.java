import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) throws IOException {
		String nom = "resultat.txt";
		String fichierRequete = "exempleRequete.txt";
		
		//Creation du fichier pour avec le temps d'execution
		FileWriter fw = new FileWriter("tempsExecution.csv"); 
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter fichierSortie = new PrintWriter(bw); 
		
		//Dictionnaire
		long debutDictionnaire = System.currentTimeMillis();
		Dictionnaire d = new Dictionnaire();
		d.creationDictionnaire(nom);
		long finDictionnaire = System.currentTimeMillis()-debutDictionnaire; 
		System.out.println("Temps d'execution pour la creation du dictionnaire : " + finDictionnaire);
		fichierSortie.print("Dictionnaire , " + finDictionnaire + "\n");
		System.out.println("Dictionnaire cree");
		
		//Indexation et Statistique
		long debutIndexation = System.currentTimeMillis();
		Indexation indexation = new Indexation(d, nom);
		indexation.creationIndex();
		long finIndexation = System.currentTimeMillis()-debutIndexation;
		System.out.println("Temps d'execution pour la creation de l'index : " + finIndexation);
		fichierSortie.print("Indexation , " + finIndexation + "\n");
		System.out.println("Indexation faite");
		
		boolean execution = true; 
		int compteur = 1;
		
		InputStream ipsData = new FileInputStream(fichierRequete);
		InputStreamReader ipsrData = new InputStreamReader(ipsData);
		BufferedReader brData = new BufferedReader(ipsrData);
		String ligne = null;
		
		//while(execution){
		while((ligne = brData.readLine()) != null){
			//Lecture de la requete
			//Scanner sc = new Scanner(System.in);
			System.out.println("************");
			//System.out.println("Pour arreter d'executer des requetes taper : STOP");
			//System.out.println("Veuillez saisir une requete : ");
			//String str = sc.nextLine();
			
			//On veut arreter 
			/*if(str.equals("STOP")){
				execution = false;
			}
			else{*/
				//Optimisation de la requete
				long debutRequete = System.currentTimeMillis();
				Requete requete = new Requete(ligne, d, indexation); //str
				boolean b = requete.parsageRequete();
				if(b){
					//Creation du fichier pour avec le resultat des requetes
					String nomFichier = "resultat" + compteur + ".csv";
					FileWriter fwr = new FileWriter(nomFichier); 
					BufferedWriter bwr = new BufferedWriter(fwr);
					PrintWriter fichierSortieResultat = new PrintWriter(bwr);
					fichierSortieResultat.print(ligne + "\n"); //str

					ArrayList<String> resultat = requete.evaluationRequete(null);
					if(resultat != null){
						for(int i = 0; i < resultat.size(); i++){
							//System.out.println(resultat.get(i));
							fichierSortieResultat.print(resultat.get(i) + "\n");
						}
					}
					
					//Fermeture fichier
					fichierSortieResultat.close();
					bwr.close();
					fwr.close();

				}
				else{
					System.out.println("ERREUR");
				}
				long finRequete = System.currentTimeMillis()-debutRequete;
				System.out.println("Temps d'execution pour l'execution de la requete " + compteur + " : " + finRequete);
				fichierSortie.print("Requete " + compteur + " , " + finRequete + "\n");
				compteur++;
			//}
			System.out.println("************ \n");
		}

		System.out.println("**Fini**");
		
		//Fermeture
		fichierSortie.close();
		bw.close();
		fw.close();

		
	}

}
