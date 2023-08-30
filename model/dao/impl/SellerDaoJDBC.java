package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	// Forçando a Injeção de dependencia
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;

	}

	@Override
	public void insert(Seller obj) {
		ResultSet rs = null;
		try {
			PreparedStatement st = conn.prepareStatement("INSERT INTO seller\r\n"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) \r\n" + "VALUES \r\n" + "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS); //Ao final da query retornar o valor dos ids gerados
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate(); //Semrpe lembrr que o update afeta as linhas e tem que chamar um int

			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys(); 
				if (rs.next()) { //Se houver linha captamos o id com o rs.getInt
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("Unexpected error, no rows affected!");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void update(Seller bbj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName \r\n" + "FROM seller INNER JOIN department \r\n"
							+ "ON seller.DepartmentId = department.Id \r\n" + "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			// Testando se minha consulta trouxe resultado e se sim o if faz passar para
			// posição um se obtiver resultado
			if (rs.next()) {
				// Pegando o resultado e transformando em objetos
				Department dep = instantiateDepartment(rs);

				Seller obj = instantiateSeller(rs, dep);
				return obj;

			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName \r\n" + "FROM seller INNER JOIN department \r\n"
							+ "ON seller.DepartmentId = department.Id\r\n" + "ORDER BY Name");
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			rs = st.executeQuery();

			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}

			return list; // Move o return para fora do loop
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName \r\n"
					+ "FROM seller INNER JOIN department \r\n" + "ON seller.DepartmentId = department.Id\r\n"
					+ "WHERE DepartmentId = ?\r\n" + "ORDER BY Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();
			// Controlando a criação do department, fazendo a associação somente a um objeto
			// na memoria
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {
				// Mapeando para verificiar se um departmentId é nulo e se for eu instancio ele
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				return list;
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	// Criando metodos auxiliare para instanciar os objetos
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

}
