import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class Dictionnaire {
	Hashtable<Integer, String> dictionnaire; 
	Hashtable<String, String> predicat; 
	int compteur;
	int compteurPredicat; 

	
	Dictionnaire(){
		dictionnaire = new Hashtable<Integer, String>();
		predicat = new Hashtable<String, String>();
		compteur = 0;
		compteurPredicat = 0; 
	}
	
	
	//Retourne tout le dictionnaire
	public Hashtable<Integer, String> getDictionnaire(){
		return dictionnaire;
	}
	
	
	//Retourne la valeur en fonction de la cle dans dictionnaire
	public String getDictionnaireByKey(int v){
		return dictionnaire.get(v);
	}
	
	
	//Retourne la cle en fonction de la valeur dans dictionnaire
	public int getDictionnaireByValue(String v){
		for(int i= 0; i < dictionnaire.size(); i++){
			if(dictionnaire.get(i).equals(v)){
				return i;
			}
		}
		return -1;
	}
	
	
	//Retourne la valeur en fonction de la cle dans predicat
	public String getPredicatByKey(String v){
		return predicat.get(v);
	}
	
	
	//Retourne la cle en fonction de la valeur dans predicat
	public String getPredicatByValue(String v){
		for(int i= 0; i < predicat.size(); i++){
			if(predicat.get("p"+i).equals(v)){
				return "p"+i;
			}
		}
		return null;
	}
	
	
	//Creation du dictionnaire en fonction du nom de fichier
	public void creationDictionnaire(String fichier) throws IOException{
		//Lecture du fichier
		InputStream ipsData = new FileInputStream(fichier);
		InputStreamReader ipsrData = new InputStreamReader(ipsData);
		BufferedReader brData = new BufferedReader(ipsrData);
		String ligne = null;
		
		//parcours du fichier
		while( (ligne = brData.readLine()) != null){
			int i = 0; 
			//Decoupage de la ligne
			for(String spo : ligne.split("\t")){
				
				//Predicat
				if(i == 1){
					if(!predicat.containsValue(spo)){
						//ajout dans predicat
						 predicat.put("p"+compteurPredicat, spo);
						 compteurPredicat++;
					}
				}
				//sujet ou objet
				else{
					/*if(i == 2){
						spo = spo.substring(0, spo.length()-2);
						//spo = spo.replace(" \\.", "");
						//System.out.println("**" + spo + "**");
					}*/
					
					if(!dictionnaire.containsValue(spo)){
						//ajout dans diko
						 dictionnaire.put(compteur, spo);
						 compteur++;
					}
				}
				i++;
					
			}
		}
		brData.close();
		ipsrData.close();
		ipsData.close();
	}
}
