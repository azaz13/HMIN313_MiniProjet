import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class Indexation {
	Dictionnaire dico; 
	String nomFichier;
	Hashtable<String, ArrayList<ArrayList<Integer>>> index; 
	Hashtable<String, ArrayList<ArrayList<Integer>>> stat; 

	
	public Indexation(Dictionnaire d, String nom){
		dico = d;
		nomFichier = nom;	
		index = new Hashtable<String, ArrayList<ArrayList<Integer>>>(); 
		stat = new Hashtable<String, ArrayList<ArrayList<Integer>>>(); 

	}
	
	
	//Retourne la liste des objets sujets pour un predicat donné
	public ArrayList<ArrayList<Integer>> getTabByPredicat(String po){
		return index.get(po);		
	}
	
	
	//Retourne les sujets suivant un predicat et un objet
	public ArrayList<Integer> rechercheByPredicatObjet(String predicat, int objet){
		ArrayList<Integer> listeSujet = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> listeObjetSujet = index.get(predicat);
		ArrayList<Integer> liste; 
		if(listeObjetSujet == null){
			return null;
		}
		for(int i = 0; i < listeObjetSujet.size(); i++){
			liste = listeObjetSujet.get(i);
			Integer objetVoulu = liste.get(0);
			if(objetVoulu == objet){
				listeSujet.add(liste.get(1));
			}
		}
		
		return listeSujet;
	}
	
	
	//Retourne la liste des objets associés au nombre de sujet dans stat
	public ArrayList<ArrayList<Integer>> rechercheByPredicatInStat(String predicat){
		return stat.get(predicat);		
	}
	
	
	//Creation des index et des statistiques
	public void creationIndex() throws IOException{
		//Lecture du fichier resultat
		InputStream ipsData = new FileInputStream(nomFichier);
		InputStreamReader ipsrData = new InputStreamReader(ipsData);
		BufferedReader brData = new BufferedReader(ipsrData);
		String ligne = null;
		
		String predicat = null; 
		int sujet= -1; 
		int objet = -1;
		
		//parcours du fichier
		while( (ligne = brData.readLine()) != null){
			
			int i = 0; 			
			//Decoupage de la ligne
			for(String spo : ligne.split("\t")){
				
				//Predicat
				if(i == 1){
					predicat = dico.getPredicatByValue(spo);
				}
				//sujet
				else if(i == 0){
					sujet = dico.getDictionnaireByValue(spo);
				}
				//objet
				else{
					objet = dico.getDictionnaireByValue(spo);
				}
				i++;
			}
			
			//Liste d'objet sujet
			ArrayList<Integer> os = new ArrayList<Integer>();
			os.add(objet);
			os.add(sujet);
			
			//On recupere les listes 
			ArrayList<ArrayList<Integer>> listeIndex = index.get(predicat);
			ArrayList<ArrayList<Integer>> listeStat = stat.get(predicat);
			
			//Le predicat n'est pas dans la liste
			if(listeIndex == null){
				listeIndex = new ArrayList<ArrayList<Integer>>();
				listeIndex.add(os);
				index.put(predicat, listeIndex);
			}
			//Le predicat est dans la hashtable
			else{
				boolean b = listeIndex.contains(os);
				if(!b){
					listeIndex.add(os);
				}
			}
			
			//Liste objet nbSujet
			ArrayList<Integer> statObjet = new ArrayList<Integer>();
			statObjet.add(objet);
			statObjet.add(1);
			
			//Le predicat n'est pas dans la liste
			if(listeStat == null){
				listeStat = new ArrayList<ArrayList<Integer>>();
				listeStat.add(statObjet);
				stat.put(predicat, listeStat);
			}
			//Le predicat est dans la liste
			else{
				boolean trouver = false;
				
				//On parcourt la liste pour trouver l'objet voulu 
				for(int j = 0; j < listeStat.size(); j++){
					ArrayList<Integer> lStat = listeStat.get(j);
					if(!lStat.isEmpty()){
						if(lStat.get(0) == objet){
							lStat.set(1, lStat.get(1) + 1);
							trouver = true;
						}
					}
				}
				//On n'a pas trouvé l'objet voulu donc on le rajoute
				if(!trouver){
					listeStat.add(statObjet);
				}
			}

			
			//Remplir la hashtable
			/*int taille = 0;
			Integer[][] tab1;
			if(index.get(predicat) != null){
				tab1 = index.get(predicat);
				taille = tab1.length;
			}
			else{
				tab1 = new Integer[10000000][2];
			}
			tab1[taille][0] = objet;
			tab1[taille][1] = sujet;
			index.put(predicat, tab1);*/
			
			predicat = null; 
			sujet = -1; 
			objet = -1;
			
		}
				
	}
	

}
