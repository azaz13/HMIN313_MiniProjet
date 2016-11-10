import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) throws IOException {
		String nom = "100K.rdf";
		
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
		
		final Collection<File> all = new ArrayList<File>();
	    findFilesRecursively(new File("requetes"), all, ".queryset");   
	    
	    for (File file : all) {
	    	String fichierRequete = file.getName();
	    	System.out.println(fichierRequete);
			InputStream ipsData = new FileInputStream("./requetes/" +fichierRequete);
			InputStreamReader ipsrData = new InputStreamReader(ipsData);
			BufferedReader brData = new BufferedReader(ipsrData);
			String ligne = null;
			
			//while(execution){
			while((ligne = brData.readLine()) != null){
				//Lecture de la requete
				//Scanner sc = new Scanner(System.in);
				System.out.println("************");
				System.out.println(ligne);
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
			brData.close();

	    }
		
		System.out.println("**Fini**");
		
		//Fermeture
		fichierSortie.close();
		bw.close();
		fw.close();

	}
	
	
	//Methode qui permet de parser un repertoire et de retrouver ses fichiers
	private static void findFilesRecursively(File file, Collection<File> all, final String extension) {
	    //Liste des fichiers correspondant a l'extension souhaitee
	    final File[] children = file.listFiles(new FileFilter() {
	    	public boolean accept(File f) {
	                return f.getName().endsWith(extension) ;
	            }}
	    );
	    if (children != null) {
	        //Pour chaque fichier recupere, on appelle a nouveau la methode
	        for (File child : children) {
	            all.add(child);
	            findFilesRecursively(child, all, extension);
	        }
	    }
	}
	

}
