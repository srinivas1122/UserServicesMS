package com.infy.infosys.UserServices.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infy.infosys.UserServices.utility.PrimaryKey;

@Entity
@Table(name = "wishlist")
public class Wishlist {
	
	@EmbeddedId
	private PrimaryKey customId;

	public PrimaryKey getCustomId() {
		return customId;
	}

	public void setCustomId(PrimaryKey customId) {
		this.customId = customId;
	}

}
