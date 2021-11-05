package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(sdf.parse("22/04/1985").getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartament().getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			e.getMessage();
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void updateSalary(Double raise, Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET BaseSalary = BaseSalary + ? "
					+ "WHERE "
					+ "Id = ?");

			st.setDouble(1, raise);
			st.setInt(2, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st1 = null;
		
		try {
			st1 =conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			st1.setInt(1, id);
			st1.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st1);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st1 = null;
		ResultSet rs1 = null;
		PreparedStatement st2 = null;
		ResultSet rs2 = null;
		
		try {
			
			
			
			
			
			
			st2 = conn.prepareStatement("SELECT * FROM seller WHERE Id = ?");
			st2.setInt(1, id);
			rs2 = st2.executeQuery();
			rs2.next();
			
			st1 =conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
			st1.setInt(1, rs2.getInt("DepartmentId"));
			
			rs1 = st1.executeQuery();
			rs1.next();
			
			Department departamento = new Department(rs1.getInt("Id"), rs1.getString("Name"));
			
			Seller vendedor = new Seller(rs2.getInt("Id"),
											rs2.getString("Name"),
											rs2.getString("Email"),
											rs2.getDate("BirthDate"),
											rs2.getDouble("BaseSalary"),
											departamento);
			
			
			
			return vendedor;
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st1);
			DB.closeResultSet(rs1);
			DB.closeStatement(st2);
			DB.closeResultSet(rs2);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st1 = null;
		ResultSet rs1 = null;
		PreparedStatement st2 = null;
		ResultSet rs2 = null;
		
		try {
			
			st1 =conn.prepareStatement("SELECT * FROM department");
			
			rs1 = st1.executeQuery();
			
			List<Department> departments = new ArrayList<>();
			while(rs1.next()) {
				departments.add(new Department(rs1.getInt("Id"), rs1.getString("Name")));
			}
			
			st2 = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs2 = st2.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			//pensar sobre
			while (rs2.next()) {
				for (Department departamento : departments) {	
					if (rs2.getInt("DepartmentId") == departamento.getId()) {
						list.add( new 
								Seller(rs2.getInt("Id"),
										rs2.getString("Name"),
										rs2.getString("Email"),
										rs2.getDate("BirthDate"),
										rs2.getDouble("BaseSalary"),
										departamento)
								);
					}
				}
			}
			return list;
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st1);
			DB.closeResultSet(rs1);
			DB.closeStatement(st2);
			DB.closeResultSet(rs2);
		}
	}

	@Override
	public List<Seller> findByDepartmente(Department department) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			while (rs.next()) {
				if (rs.getInt("DepartmentId") == department.getId()) {
					list.add( new 
							Seller(rs.getInt("Id"),
									rs.getString("Name"),
									rs.getString("Email"),
									rs.getDate("BirthDate"),
									rs.getDouble("BaseSalary"),
									department)
							);
				}
			}
			return list;
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
