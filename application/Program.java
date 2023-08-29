package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		/*
		 * Projeto DAO Com JDBC O padrão DAO (Data Access Object) é um padrão de projeto
		 * de software Utilizado para separar a lógica de acesso a dados do restante da
		 * aplicação.
		 */

		System.out.println("===== TESTE 1 =====");

		// SellerDao pode instanciar daofactory por que o daofactory os implementa de
		// certa forma
		SellerDao sellerDao = DaoFactory.createSellerDao(); // Injeção de Dependecia
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
	}

}
