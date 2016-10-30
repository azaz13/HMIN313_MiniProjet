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
	ArrayList<Integer> argsWhereUtilise; //0 pas utilisé 1 utilisé

	
	public Requete(String r, Dictionnaire d, Indexation in){
		requete = r; 
		argumentWhere = new ArrayList<String>();
		argumentSelect = new ArrayList<String>();
		dico = d; 
		indexation = in; 
		traductionWhere = new ArrayList<ArrayList<String>>();
		argsWhereUtilise = new ArrayList<Integer>();
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
		
		//Boolean à traiter
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
			
			argsWhereUtilise.add(0);
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
		//La variable dans le select n'est pas dans le where
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
	
	
	public boolean traductionWhere(){
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
						
			//Test si on connait le predicat ou pas 
			if(predicat == null){
				return false; 
			}
			//Test si on connait l'objet ou pas
			if(objet.equals("-1")){
				return false; 
			}
			
			//Ajout dans la liste
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
			
			String l = ""; 
			for(int k =0; k < ligne.size(); k++){
				l += ligne.get(k) + " ";
			}
			System.out.println(l);
		}
		return true;
	}
	
	
	//Evaluer la requete
	public ArrayList<String> evaluationRequete(ArrayList<Integer> tabInit){
		//Rien dans le Where
		if(traductionWhere.size() == 0){
			return null;
		}
		
		int premier = -1; 
		int second = -1; 
		int positionPremier = -1; 
		int positionSecond = -1; 
		
		ArrayList<Integer> resInt = new ArrayList<Integer>();
		
		//Parcours de la liste pour recuperer le premier et le second dans l'ordre decroissant
		for(int i = 0; i < traductionWhere.size(); i++){
			ArrayList<String> liste = traductionWhere.get(i);
			//On a pas utilisé ce where
			if(argsWhereUtilise.get(i).equals(0)){
				//Si c'est different de -1 c'est qu'on a un objet
				if(!liste.get(3).equals("-1")){
					if(premier == -1){
						premier = Integer.parseInt(liste.get(3));
						positionPremier = i; 

					}
					//On a un premier
					else{
						int c = Integer.parseInt(liste.get(3));
						if(c < premier){
							second = premier; 
							premier = c;
							positionSecond = positionPremier;
							positionPremier = i;
						}
						else if( c < second){
							second = c; 
							positionSecond = i; 
						}
					}
				}	
			
			}
			
		}
		
		System.out.println(premier + " " + positionPremier + " " + second + " " + positionSecond);
		
		//Resultat du where qui a le plus petit nombre de sujet	
		String predicat1 = traductionWhere.get(positionPremier).get(0); 
		int objet1 = Integer.parseInt(traductionWhere.get(positionPremier).get(1)); 
		ArrayList<Integer> l1 = indexation.rechercheByPredicatObjet(predicat1, objet1);
		argsWhereUtilise.set(positionPremier, 1);
		
		//Variable pour la liste qui se positionn
		ArrayList<Integer> l2 = null; 
		if(tabInit != null){
			l2 = tabInit; 
		}
		else if(positionSecond != -1){			
			String predicat2 = traductionWhere.get(positionSecond).get(0); 
			int objet2 = Integer.parseInt(traductionWhere.get(positionSecond).get(1)); 
			l2 = indexation.rechercheByPredicatObjet(predicat2, objet2);
			argsWhereUtilise.set(positionSecond, 1);
		}
		
		ArrayList<String> resultat = new ArrayList<String>();

		if(l2 != null){
			//On compare les résultats
			for(int i =0; i < l1.size(); i++){
				if(l2.contains(l1.get(i))){
					resInt.add(l1.get(i));
					String sujet = dico.getDictionnaireByKey(l1.get(i)); 
					resultat.add(sujet);
				}
			}
		}
		
		//On a pas de résultat
		if(resultat.size() == 0){
			resultat.add("La requete n'a pas de résultats");
			return resultat; 
		}
		
		if(argsWhereUtilise.contains(0)){
			resultat = evaluationRequete(resInt); 
		}

		return resultat;
	}
	
	
}
