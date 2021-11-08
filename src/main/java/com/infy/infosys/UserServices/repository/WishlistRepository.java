package com.infy.infosys.UserServices.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.infosys.UserServices.entity.Wishlist;
import com.infy.infosys.UserServices.utility.PrimaryKey;

public interface WishlistRepository extends CrudRepository<Wishlist, PrimaryKey> {

}
