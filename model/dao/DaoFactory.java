package model.dao;

import model.dao.impl.SellerDaoJDBC;

/*O Metodo Recebe Uma Interface e Retorna Uma Classe Que Implementa A Interface
Ao Instanciar Uma Interface Acontece a Injeção de Dependencia*/

//Esse macete esconde os metodos de acesso ao banco de dados, tudo fica encapsulado, esse é um dos pilares do DAO

public class DaoFactory {

	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();
		
	}


	
	
	
	
}
