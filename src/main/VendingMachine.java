package main;

import java.util.Map;
import java.util.TreeMap;

public class VendingMachine {

	private Map<Coin, Integer> coinReturn;
	private Map<Coin, Integer> insertedCoins;
	private Map<Product, Integer> dispensedProduct;

	private TreeMap<Product, Double> productPrices;
	private TreeMap<Coin, Double> coinValue;

	private boolean productPressed;
	private double productPrice;
	private boolean productPurchased;

	public VendingMachine() {
		coinReturn = new TreeMap<Coin, Integer>();
		insertedCoins = new TreeMap<Coin, Integer>();
		dispensedProduct = new TreeMap<Product, Integer>();

		productPrices = new TreeMap<Product, Double>();
		productPrices.put(Product.COLA, (double) 1);
		productPrices.put(Product.CHIPS, 0.5);
		productPrices.put(Product.CANDY, (double) 0.65);

		coinValue = new TreeMap<Coin, Double>();
		coinValue.put(Coin.PENNY, 0.01);
		coinValue.put(Coin.NICKEL, 0.05);
		coinValue.put(Coin.DIME, 0.10);
		coinValue.put(Coin.QUARTER, 0.25);

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

		if (insertedCoinValue() != 0)
			return String.format("%.2f", insertedCoinValue());
		return "INSERT COIN";
	}

	private double insertedCoinValue() {
		double total = 0;
		for (Coin coin : insertedCoins.keySet()) {
			total += coinValue.get(coin) * insertedCoins.get(coin);
		}
		return total;
	}

	public void insertCoin(Coin coin) {

		int coinCount = 0;
		if (insertedCoins.containsKey(coin))
			coinCount = insertedCoins.get(coin);

		switch (coin) {

		case NICKEL:
			insertedCoins.put(Coin.NICKEL, coinCount + 1);
			break;

		case DIME:
			insertedCoins.put(Coin.DIME, coinCount + 1);
			break;

		case QUARTER:
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
	}

	public void selectProduct(Product product) {
		int dispencedProductCount = 0;
		if (dispensedProduct.containsKey(product))
			dispencedProductCount = dispensedProduct.get(product);

		productPressed = true;
		productPrice = productPrices.get(product);
		if (insertedCoinValue() == productPrice) {
			productPurchased = true;
			insertedCoins.clear();
			dispensedProduct.put(product, dispencedProductCount + 1);
		} else if (insertedCoinValue() > productPrice) {
			double remainder = insertedCoinValue() - productPrice;
			remainder = roundCents(remainder);

			while (remainder >= 0.25) {
				remainder -= 0.25;
				remainder = roundCents(remainder);
				int coinCount = 0;
				if (coinReturn.containsKey(Coin.QUARTER))
					coinCount = coinReturn.get(Coin.QUARTER);
				coinReturn.put(Coin.QUARTER, coinCount + 1);
			}
			while (remainder >= 0.10) {
				remainder -= 0.10;
				remainder = roundCents(remainder);
				coinReturn.put(Coin.DIME, 1);
			}

			if (remainder == (double) 0.05)
				coinReturn.put(Coin.NICKEL, 1);
			productPurchased = true;
			dispensedProduct.put(product, dispencedProductCount + 1);
			insertedCoins.clear();
		}
	}

	private double roundCents(double cents) {
		return 0.01 * Math.round(cents * 100);
	}

	public Map<Product, Integer> getDispensedProducts() {
		Map<Product, Integer> retreivedProducts = new TreeMap<Product, Integer>();
		retreivedProducts.putAll(dispensedProduct);
		dispensedProduct.clear();
		return retreivedProducts;
	}

}
