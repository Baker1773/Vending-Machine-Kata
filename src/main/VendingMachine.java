package main;

import java.util.Map;
import java.util.TreeMap;

public class VendingMachine {

	private double amount;
	private Map<Coin, Integer> coinReturn;
	private Map<Coin, Integer> insertedCoins;
	private Map<Product, Integer> dispensedProduct;
	private TreeMap<Product, Double> productPrices;

	boolean productPressed;
	double productPrice;
	boolean productPurchased;

	public VendingMachine() {
		amount = 0;
		coinReturn = new TreeMap<Coin, Integer>();
		insertedCoins = new TreeMap<Coin, Integer>();
		dispensedProduct = new TreeMap<Product, Integer>();
		productPrices = new TreeMap<Product, Double>();
		productPrices.put(Product.COLA, (double) 1);
		productPrices.put(Product.CHIPS, 0.5);
		productPrices.put(Product.CANDY, (double) 0.65);

	}

	public String getDisplay() {
		if (productPurchased) {
			productPurchased = false;
			productPressed = false;
			return "THANK YOU";
		}

		if (productPressed) {
			productPressed = false;
			return "PRICE $" + String.format("%.2f", productPrice);
		}

		if (amount != 0)
			return String.format("%.2f", amount);
		return "INSERT COIN";
	}

	public void insertCoin(Coin coin) {

		int coinCount = 0;
		if (insertedCoins.containsKey(coin))
			coinCount = insertedCoins.get(coin);

		switch (coin) {

		case NICKEL:
			amount += 0.05;
			insertedCoins.put(Coin.NICKEL, coinCount + 1);
			break;

		case DIME:
			amount += 0.10;
			insertedCoins.put(Coin.DIME, coinCount + 1);
			break;

		case QUARTER:
			amount += 0.25;
			insertedCoins.put(Coin.QUARTER, coinCount + 1);
			break;

		case PENNY:
			int pennyCount = 0;
			if (coinReturn.containsKey(Coin.PENNY))
				pennyCount = coinReturn.get(Coin.PENNY);
			coinReturn.put(Coin.PENNY, pennyCount + 1);
			break;

		default:
			break;
		}
	}

	public Map<Coin, Integer> emptyCoinReturn() {
		Map<Coin, Integer> coinReturned = new TreeMap<Coin, Integer>();
		coinReturned.putAll(coinReturn);
		coinReturn.clear();
		return coinReturned;
	}

	public void pressReturnCoinButton() {
		for (Coin c : insertedCoins.keySet()) {
			if (coinReturn.containsKey(c))
				coinReturn.put(c, insertedCoins.get(c) + coinReturn.get(c));
			else
				coinReturn.put(c, insertedCoins.get(c));
		}
		insertedCoins.clear();
		amount = 0;
	}

	public void selectProduct(Product product) {
		int dispencedProductCount = 0;
		if (dispensedProduct.containsKey(product))
			dispencedProductCount = dispensedProduct.get(product);

		productPressed = true;
		productPrice = productPrices.get(product);
		if (amount == productPrice) {
			productPurchased = true;
			amount = 0;
			dispensedProduct.put(product, dispencedProductCount + 1);
		} else if (amount > productPrice) {
			coinReturn.put(Coin.QUARTER, 1);
		}
	}

	public Map<Product, Integer> getDispensedProducts() {
		Map<Product, Integer> retreivedProducts = new TreeMap<Product, Integer>();
		retreivedProducts.putAll(dispensedProduct);
		dispensedProduct.clear();
		return retreivedProducts;
	}

}
