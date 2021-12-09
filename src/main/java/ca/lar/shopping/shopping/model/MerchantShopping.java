package ca.lar.shopping.shopping.model;

import lombok.Data;

@Data
public class MerchantShopping{
	
	private Long id;
	private ShoppingOption shoppingOption;
	
	
	public MerchantShopping(Long id, ShoppingOption shoppingOption) {
		super();
		this.id = id;
		this.shoppingOption = shoppingOption;
	}
	
}

