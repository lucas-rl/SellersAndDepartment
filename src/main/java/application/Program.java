package application;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	public static void main(String[] args) throws SQLException {

		SellerDao sellerDao = DaoFactory.createSellerDao();

		Department department = new Department(2, "vendas");

		List<Seller> lista = sellerDao.findByDepartmente(department);

		for (Seller vendedor : lista) {
			System.out.println(vendedor);
		}

		System.out.println("\n" + "-".repeat(10) + "\n");

		List<Seller> lista2 = sellerDao.findAll();
		for (Seller vendedor : lista2) {
			System.out.println(vendedor);
		}

		System.out.println("\n" + "-".repeat(10) + "\n");
		
		Seller vendedor = sellerDao.findById(2);
		System.out.println(vendedor);
		
		System.out.println("\n" + "-".repeat(10) + "\n");
		
		//Date data = new Date();
		//data = null;
		//Seller vendedor2 = new Seller(50, "Bob Brown", "bob@gmail.com", data, 1000.0, department);
		//sellerDao.insert(vendedor2);

		//sellerDao.deleteById(7);
		
		//sellerDao.updateSalary(-1537.90, 5);
		vendedor = sellerDao.findById(5);
		System.out.println(vendedor);
		
	}
}
