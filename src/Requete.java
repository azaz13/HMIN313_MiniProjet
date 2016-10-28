import java.util.ArrayList;

import org.openrdf.query.algebra.evaluation.function.string.Substring;

public class Requete {
	
	String requete;
	ArrayList<String> argumentWhere; 
	ArrayList<String> argumentSelect;
	ArrayList<String> var;
	ArrayList<ArrayList<String>> traductionWhere; 
	boolean reqMalFormee = false;
	Dictionnaire dico; 
	Indexation indexation; 

	
	public Requete(String r, Dictionnaire d, Indexation in){
		requete = r; 
		argumentWhere = new ArrayList<String>();
		argumentSelect = new ArrayList<String>();
		dico = d; 
		indexation = in; 
		traductionWhere = new ArrayList<ArrayList<String>>();
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
		
		//Traduction du where
		traductionWhere();
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
				//test si il y a un espace devant on ne le prend pas
				if (a.substring(0, 1).equals(" ")) {
					argumentSelect.add(a.substring(1));
				}
				else{
					argumentSelect.add(a);
				}
			}
		}
		else{
			if (args.substring(0, 1).equals(" ")) {
				argumentSelect.add(args.substring(1));
			}
			else{
				argumentSelect.add(args);
			}
		}		
	}
	
	
	public void traitementWhere(String where){
		//Decomposer et remplir la liste
		for(String s : where.split(" \\. ")){ //"[ ]*[.][ ]*"
			if(s.contains("{")){
				s = s.substring(1, s.length());
			}
			if(s.contains("}")){
				s = s.substring(0, s.length() - 1);
			}
			
			argumentWhere.add(s);
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
	
	
	public void traductionWhere(){
		String predicat = null; 
		String objet = null; 
		String sujet = null; 
		int nbSujet = -1;
		int objetInt = -1; 
		
		for(int i = 0; i < argumentWhere.size(); i++){
			int j = 0; 
			for(String spo : argumentWhere.get(i).split(" ")){
				
				//Predicat
				if(j == 1){
					predicat = dico.getPredicatByValue(spo);
				}
				//sujet
				else if(j == 0){
					sujet =  spo;
				}
				//objet
				else{
					if(!spo.contains("?")){
						objetInt = dico.getDictionnaireByValue(spo);
						objet = Integer.toString(objetInt);
					}
					else{
						objet = spo; 
					}	
				}
				j++;				
				
			}
			
			System.out.println("traductionWhere " + predicat + " " + objet + " " + sujet);
			
			//Test si on connait le predicat ou pas 
			//Si predicat == null
			//Si on ne connait pas l'objet
			
			//ajout dans la liste
			ArrayList<String> ligne = new ArrayList<String>();
			ligne.add(predicat); 
			ligne.add(objet); 
			ligne.add(sujet); 
			if(objet.contains("?")){
				ligne.add("-1");
			}
			else{
				ligne.add(Integer.toString(indexation.rechercheByPredicatObjetInStat(predicat, objetInt))); 
			}
			
			traductionWhere.add(ligne); 
			
		}
		
		
		
		
	}
	
	
}
