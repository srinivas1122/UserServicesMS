package com.infy.infosys.UserServices.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.infosys.UserServices.entity.Cart;
import com.infy.infosys.UserServices.utility.PrimaryKey;

public interface CartRepository extends CrudRepository<Cart, PrimaryKey> {
	
	
	public List<Cart> findByCustomPKBuyerId(String id); 
	
	public void deleteByCustomPKBuyerIdAndCustomPKProdId(String buyId,String prodId);
	
	public Cart findByCustomPKBuyerIdAndCustomPKProdId(String buyId,String ProdId);

}
