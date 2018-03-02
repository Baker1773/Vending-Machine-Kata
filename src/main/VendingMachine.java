package main;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class VendingMachine {

	private Map<Coin, Integer> coinReturn;
	private Map<Coin, Integer> insertedCoins;
	private Map<Product, Integer> dispensedProduct;

	private Map<Product, Double> productPrices;
	private Map<Coin, Double> coinValue;
	private ArrayList<Coin> CoinListByDescendingValue;
	private Set<Coin> acceptedCoins;

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

		acceptedCoins = new TreeSet<Coin>();
		acceptedCoins.add(Coin.NICKEL);
		acceptedCoins.add(Coin.DIME);
		acceptedCoins.add(Coin.QUARTER);

		CoinListByDescendingValue = new ArrayList<Coin>();
		CoinListByDescendingValue.add(Coin.QUARTER);
		CoinListByDescendingValue.add(Coin.DIME);
		CoinListByDescendingValue.add(Coin.NICKEL);
		CoinListByDescendingValue.add(Coin.PENNY);
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

		if (acceptedCoins.contains(coin)) {
			int coinCount = 0;
			if (insertedCoins.containsKey(coin))
				coinCount = insertedCoins.get(coin);
			insertedCoins.put(coin, coinCount + 1);
		} else {
			int coinCount = 0;
			if (coinReturn.containsKey(coin))
				coinCount = coinReturn.get(coin);
			coinReturn.put(coin, coinCount + 1);
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

		Map<Coin, Integer> coinsToBeReturned = new TreeMap<Coin, Integer>();

		productPressed = true;
		productPrice = productPrices.get(product);
		if (insertedCoinValue() >= productPrice) {
			double remainder = insertedCoinValue() - productPrice;
			remainder = roundCents(remainder);

			for (Coin coin : CoinListByDescendingValue) {
				while (remainder >= coinValue.get(coin)
						&& insertedCoins.containsKey(coin)) {
					remainder = roundCents(remainder - coinValue.get(coin));
					int coinReturnCount = 0;
					if (coinsToBeReturned.containsKey(coin))
						coinReturnCount = coinsToBeReturned.get(coin);
					coinsToBeReturned.put(coin, coinReturnCount + 1);

					int coinInsertedCount = insertedCoins.get(coin);
					coinInsertedCount--;
					if (coinInsertedCount > 0)
						insertedCoins.put(coin, coinInsertedCount);
					else
						insertedCoins.remove(coin);
				}
			}

			if (remainder == 0) {
				productPurchased = true;
				dispensedProduct.put(product, dispencedProductCount + 1);
				for (Coin c : coinsToBeReturned.keySet()) {
					if (coinReturn.containsKey(c))
						coinReturn.put(c,
								coinReturn.get(c) + coinsToBeReturned.get(c));
					else
						coinReturn.put(c, coinsToBeReturned.get(c));
				}
				insertedCoins.clear();
			} else {
				for (Coin c : coinsToBeReturned.keySet()) {
					if (insertedCoins.containsKey(c))
						insertedCoins.put(c, insertedCoins.get(c)
								+ coinsToBeReturned.get(c));
					else
						insertedCoins.put(c, coinsToBeReturned.get(c));
				}
			}
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
