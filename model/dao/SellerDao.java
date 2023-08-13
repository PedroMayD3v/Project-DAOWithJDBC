package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	void insert(SellerDao obj);
	void update(SellerDao obj);
	void deleteById(Integer id);
	SellerDao findById(Integer id);
	List<Seller> findAll(Integer id);
	
}
