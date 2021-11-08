package com.infy.infosys.UserServices.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.infosys.UserServices.dto.BuyerDTO;
import com.infy.infosys.UserServices.dto.CartDTO;
import com.infy.infosys.UserServices.dto.SellerDTO;
import com.infy.infosys.UserServices.entity.Buyer;
import com.infy.infosys.UserServices.entity.Cart;
import com.infy.infosys.UserServices.entity.Seller;
import com.infy.infosys.UserServices.entity.Wishlist;
import com.infy.infosys.UserServices.exception.UserException;
import com.infy.infosys.UserServices.repository.BuyerRepository;
import com.infy.infosys.UserServices.repository.CartRepository;
import com.infy.infosys.UserServices.repository.SellerRepository;
import com.infy.infosys.UserServices.repository.WishlistRepository;
import com.infy.infosys.UserServices.utility.PrimaryKey;
import com.infy.infosys.UserServices.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service(value = "userService")
@Transactional
public class UserServiceImpl {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static int b;
	private static int s;
	
	static {
		b=100;
		s=100;
	}
	
	@Autowired
	private BuyerRepository bRepo;
	
	@Autowired
	private SellerRepository sRepo;

	@Autowired
	private WishlistRepository wRepo;
	
	@Autowired
	private CartRepository cRepo;
	
	
	
	
	//all buyers
	public List<BuyerDTO> getAllBuyers(){
		List<Buyer> b=(List<Buyer>) bRepo.findAll();
		List<BuyerDTO> bDTOs= new ArrayList<>();
		for(Buyer bb:b) {
			BuyerDTO bDTO=BuyerDTO.valueOf(bb);
			bDTOs.add(bDTO);
		}
		logger.info("Buyer Details:{}",bDTOs);
		return bDTOs;
	}
	

	//all sellers
	public List<SellerDTO> getAllSellers(){
		List<Seller> s=(List<Seller>) sRepo.findAll();
		List<SellerDTO> sDTOs=new ArrayList<>();
		for(Seller ss:s) {
			SellerDTO sDTO=SellerDTO.valueOf(ss);
			sDTOs.add(sDTO);
			
		}
		logger.info("Seller Details are:{}", sDTOs);
		return sDTOs;
	}
	
	
	public String buyerRegistration(BuyerDTO buyerDTO) throws UserException {		
		Validator.validate_buyer(buyerDTO);
		
		Buyer bb = bRepo.findByPhoneNumber(buyerDTO.getPhoneNumber());
		
		if(bb != null)
			throw new UserException("BuyerSerivce.BUYER_ALREADY_EXISTS");
		
		String id = "B" + b++;
		
		bb = new Buyer();
		
		bb.setBuyerId(id);
		bb.setEmail(buyerDTO.getEmail());
		bb.setName(buyerDTO.getName());
		bb.setPhoneNumber(buyerDTO.getPhoneNumber());
		bb.setPassword(buyerDTO.getPassword());
		bb.setIsActive(buyerDTO.getIsActive());
		bb.setIsPrivileged(buyerDTO.getIsPrivileged());
		bb.setRewardPoints(buyerDTO.getRewardPoints());
		
		bRepo.save(bb);
		
		return bb.getBuyerId();
	}


	public String sellerRegistration(SellerDTO sellerDTO) throws UserException {
		
		Validator.validate_seller(sellerDTO);
		
		Seller ss = sRepo.findByPhoneNumber(sellerDTO.getPhoneNumber());
		
		if(ss != null)
			throw new UserException("SellerService.SELLER_ALREADY_EXISTS");
		
		String id = "S" + s++;
		
		ss = new Seller();
		
		ss.setEmail(sellerDTO.getEmail());
		ss.setSellerId(id);
		ss.setName(sellerDTO.getName());
		ss.setPassword(sellerDTO.getPassword());
		ss.setIsActive(sellerDTO.getIsActive());
		ss.setPhoneNumber(sellerDTO.getPhoneNumber());
		
		sRepo.save(ss);
		
		return ss.getSellerId();
	}


	public String buyerLogin(String email, String password) throws UserException {
	
		if(!Validator.validateEmail(email))
			throw new UserException("UserServiceImpl.ENTER_VALID_MAIL");
		
		Buyer b = bRepo.findByEmail(email);
		
		
		if(b == null)
			throw new UserException("Buyer.WRONG_CREDENTIALS");
		
		if(!b.getPassword().equals(password))
			throw new UserException("Buyer.WRONG_CREDENTIALS");
		
		b.setIsActive("True");
			
		bRepo.save(b);
		
		return "Logged in successfully";
	}


	public String sellerLogin(String email, String password) throws UserException {

		if(!Validator.validateEmail(email))
			throw new UserException("Seller.ENTER_VALID_MAIL");
		
		Seller seller = sRepo.findByEmail(email);
		
		
		if(seller == null)
			throw new UserException("Seller.WRONG_CREDENTIALS");
		
		if(!seller.getPassword().equals(password))
			throw new UserException("Seller.WRONG_CREDENTIALS");
		
		seller.setIsActive("True");
			
		sRepo.save(seller);
		
		return "Logged in successfully";
	}


	public String deleteBuyer(String id){
		
		Buyer b = bRepo.findByBuyerId(id);
		
		bRepo.delete(b);
		
		return "Account successfully deleted";
	}


	public String deleteSeller(String id) {
		
		Seller s = sRepo.findBySellerId(id);
		
		sRepo.delete(s);
		
		return "Account successfully deleted";
	}


	public String wishlistService(String prodId, String buyerId) {
		
		PrimaryKey cust = new PrimaryKey(prodId,buyerId);
	
		Wishlist w = new Wishlist();
		
		w.setCustomId(cust);
		
		wRepo.save(w);
		
		return "Added Successfully to Wishlist";
	}
	

	public String cartService(String prodId, String buyerId, Integer quantity) {
		
		PrimaryKey cust = new PrimaryKey(prodId,buyerId);
	
		Cart cart = new Cart();
		
		cart.setCustomPK(cust);
		
		cart.setQuantity(quantity);
		
		cRepo.save(cart);
		
		return "Added Successfully to Cart";
	}

	public List<CartDTO> getCartProducts(String id) throws UserException {
		
		List<Cart> list = cRepo.findByCustomPKBuyerId(id);
		
		if(list.isEmpty())
			throw new UserException("Buyer.NO_ITEMS");
		
		List<CartDTO> li = new ArrayList<>();
		
		for(Cart cart : list)
		{
			CartDTO cartDTO = new CartDTO();
			
			cartDTO.setBuyerId(cart.getCustomPK().getBuyerId());
			cartDTO.setProdId(cart.getCustomPK().getProdId());
			cartDTO.setQuantity(cart.getQuantity());
			
			li.add(cartDTO);
		}
		
		return li;
	}


	public String removeFromCart(String buyerId, String prodId) throws UserException {
		
		Cart cart = cRepo.findByCustomPKBuyerIdAndCustomPKProdId(buyerId, prodId);
		
		if(cart==null)
			throw new UserException("Buyer.NO_ITEMS");
		
		cRepo.deleteByCustomPKBuyerIdAndCustomPKProdId(buyerId, prodId);
		
		return "The product deleted successfully";
	}


	public String updateRewardPoint(String buyerId, Integer rewPoints) throws UserException {
		
		Buyer buyer = bRepo.findByBuyerId(buyerId);
		
		if(buyer==null)
			throw new UserException("Buyer not found");
		
		buyer.setRewardPoints(rewPoints.toString());
		
		bRepo.save(buyer);
		
		return "Reward Points Updated for buyer Id : "+ buyer.getBuyerId();
	}

}