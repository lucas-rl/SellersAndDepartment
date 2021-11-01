package application;

import java.sql.SQLException;
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
		
		System.out.println(lista.get(0).getDepartament().equals(lista.get(1).getDepartament()));
		
	}
}
