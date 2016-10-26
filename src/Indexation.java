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
	
	
	public ArrayList<ArrayList<Integer>> rechercheByPredicatInStat(String predicat){
		return stat.get(predicat);		
	}
	
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
			
			//On recupere la liste
			ArrayList<ArrayList<Integer>> liste = index.get(predicat);
			ArrayList<ArrayList<Integer>> listeStat = stat.get(predicat);
			
			//Le predicat n'est pas dans la liste
			if(liste == null){
				liste = new ArrayList<ArrayList<Integer>>();
				liste.add(os);
				index.put(predicat, liste);
				
				//ListeStat
				listeStat = new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> statObjet = new ArrayList<Integer>();
				statObjet.add(objet);
				statObjet.add(1);
				listeStat.add(statObjet);
				stat.put(predicat, listeStat);
				
			}
			//Le predicat est dans la hashtable
			else{
				boolean b = liste.contains(os);
				if(!b){
					liste.add(os);
				}
				
				boolean trouver = false;
				for(int j = 0; j < listeStat.size(); j++){
					ArrayList<Integer> lStat = listeStat.get(j);
					System.out.println(lStat.isEmpty());
					if(lStat.get(0) == objet){
						lStat.set(1, lStat.get(1) + 1);
						trouver = true;
						System.out.println(predicat + "objet trouvé");
					}
				}
				if(!trouver){
					//listeStat = new ArrayList<ArrayList<Integer>>();
					ArrayList<Integer> statObjet = new ArrayList<Integer>();
					os.add(objet);
					os.add(1);
					listeStat.add(statObjet);
					//stat.put(predicat, listeStat);
					System.out.println(predicat  + " objet non trouvé");
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
	
	
	
	//Creation des statistiques
	/*public void creationStatistique(){
		
		Iterator i = index.keySet().iterator();
		 
		while (i.hasNext())
		{
		    String predicat = (String)i.next();		    
		    ArrayList<ArrayList<Integer>> valeur = index.get(predicat);
		    for(int j = 0; j < dico.objet.size(); j++){
		    	
		    	
		    	if(j == 0){
		    		
		    	}
		    	else{
		    		
		    	}
		    }
		    
		    
		    //System.out.println(clef + " " +valeur);
		}
	}*/
	
	

}
