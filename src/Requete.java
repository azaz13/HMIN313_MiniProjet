import java.util.ArrayList;

import org.openrdf.query.algebra.evaluation.function.string.Substring;

public class Requete {
	
	String requete;
	ArrayList<String> argumentWhere; 
	ArrayList<String> argumentSelect;
	ArrayList<String> var;
	boolean reqMalFormee = false;

	
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
		verificationRequete();
	}
	
	
	public void traitementSelect(String select){
		//On enleve le mot select pour recuperer le variable
		String args = null;
		for(String str : select.split("SELECT")){
			args = str; 
			System.out.println(str);
		}
		
		//liste des variables
		if(args.contains(",")){
			for(String a : args.split("[,]")){
				//test si il y a un espace devant on ne le prend pas
				if (a.substring(0, 1).equals(" ")) {
					argumentSelect.add(a.substring(1));
				}
				 
			}
		}
		else{
			if (args.substring(0, 1).equals(" ")) {
				argumentSelect.add(args.substring(1));
			}
			
		}
		
	}
	
	
	public void traitementWhere(String where){
		System.out.println("where " + where);
		//Decomposer et remplir la liste
		for(String s : where.split("[ ]*[.][ ]*")){
			if(s.contains("{")){
				s = s.substring(1, s.length());
			}
			if(s.contains("}")){
				s = s.substring(0, s.length() - 1);
			}
			argumentWhere.add(s);
			System.out.println("s " + s);
		}
	}
	
	
	public void verificationRequete(){
		ArrayList<String> dif = new ArrayList<String>();
		//------------------------------------------------------------
		// on commence par verifier qu'on a  pas ecrit un truc du genre select ?x, ?x ...  
		for (String varSelect : argumentSelect) {
			if (dif.isEmpty()) {
				dif.add(varSelect);
			}
			else{
				for (int j= 0; j< dif.size(); j++) {
					if (dif.contains(varSelect)) {
						System.out.println("ERR: "+dif.get(j)+" a au moins 2 occurences.");
						reqMalFormee = true;
					}
				}
				if (!reqMalFormee)
				{
					dif.add(varSelect);
				}
			}
		}
		//---------------------------------------------------------------
		boolean test2;
		for (String varSelect : argumentSelect) {
			test2 = false;
			for (String varWhere : argumentWhere) {
				for (String string : varWhere.split("[ ]")) {
					if (string.subSequence(0, 1).equals("?")) {
						if (varSelect.equals(string)) {
							test2= true;
						}
					}
				}
			}
			if (!test2) {
				reqMalFormee = true;
				System.out.println("ERR: La variable "+varSelect +" dans le SELECT n'existe pas des le WHERE.");
					
			}
		}
		//-----------------------------------------------------------------
	}
	
}
