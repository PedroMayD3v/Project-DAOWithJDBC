package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
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
		
		System.out.println("======TESTE 2 ======");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("======TESTE 3 ======");
		list = sellerDao.findAll();
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("=======TESTE 4 =======");
		Seller newSeller = new Seller(null, "Greg", "Greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New Id = " + newSeller.getId());
		
	}

}
