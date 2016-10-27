import java.util.ArrayList;

import org.openrdf.query.algebra.evaluation.function.string.Substring;

public class Requete {
	
	String requete;
	ArrayList<String> argumentWhere; 
	ArrayList<String> argumentSelect; 

	
	public Requete(String r){
		requete = r; 
		argumentWhere = new ArrayList<String>();
		argumentSelect = new ArrayList<String>();
	}
	

	//Parsage de la requete
	public void parsageRequete(){
		String ligne = requete;
		String select = null; 
		String where = null; 
		
		int i = 0;
		for(String str : ligne.split(" WHERE ")){
			if(i == 0){
				select = str;
			}
			else{
				where = str;
			}
			i++; 
		}
		
		//Traitement sur le select
		traitementSelect(select);
		
		//Traitement sur le where
		traitementWhere(where);
		
		//Verification de la requete
		//Voir si un arg de select appartient à un where
		
	}
	
	
	public void traitementSelect(String select){
		//On enleve le mot select pour recuperer le variable
		String args = null;
		for(String str : select.split("SELECT")){
			args = str; 
		}
		
		//liste des variables
		if(args.contains(",")){
			for(String a : args.split(",")){
				argumentSelect.add(a); 
			}
		}
		else{
			argumentSelect.add(args);
		}
		
	}
	
	
	public void traitementWhere(String where){
		System.out.println("where " + where);
		//Decomposer et remplir la liste
		//Problème avec le . car obliger de mettre espace avant et après
		for(String s : where.split(" . ")){
			if(s.contains("{")){
				s = s.substring(1, s.length());
			}
			else if(s.contains("}")){
				s = s.substring(0, s.length() - 1);
			}
			argumentWhere.add(s);
			System.out.println("s " + s);
		}
	}
	
	
}
