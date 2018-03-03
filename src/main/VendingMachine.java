package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class VendingMachine {

	private Map<Coin, Integer> coinReturn;
	private Map<Coin, Integer> insertedCoins;
	private Map<Product, Integer> dispensedProduct;
	private Map<Product, Integer> productInventory;
	private Map<Coin, Integer> coinInventory;

	private Map<Product, Double> productPrices;
	private Map<Coin, Double> coinValue;
	private ArrayList<Coin> CoinListByDescendingValue;
	private Set<Coin> acceptedCoins;

	private boolean productPressed;
	private double productPrice;
	private boolean productPurchased;
	private boolean productSoldOut;

	public VendingMachine() {
		coinReturn = new TreeMap<Coin, Integer>();
		insertedCoins = new TreeMap<Coin, Integer>();
		dispensedProduct = new TreeMap<Product, Integer>();

		setUpProductPrices();
		setUpCoinValue();
		setUpAcceptedCoins();
		setUpCoinListByDescendingValue();

		productInventory = new TreeMap<Product, Integer>();
		coinInventory = new TreeMap<Coin, Integer>();
	}

	private void setUpProductPrices() {
		productPrices = new TreeMap<Product, Double>();
		productPrices.put(Product.COLA, (double) 1);
		productPrices.put(Product.CHIPS, 0.5);
		productPrices.put(Product.CANDY, (double) 0.65);
	}

	private void setUpCoinValue() {
		coinValue = new TreeMap<Coin, Double>();
		coinValue.put(Coin.PENNY, 0.01);
		coinValue.put(Coin.NICKEL, 0.05);
		coinValue.put(Coin.DIME, 0.10);
		coinValue.put(Coin.QUARTER, 0.25);
	}

	private void setUpAcceptedCoins() {
		acceptedCoins = new TreeSet<Coin>();
		acceptedCoins.add(Coin.NICKEL);
		acceptedCoins.add(Coin.DIME);
		acceptedCoins.add(Coin.QUARTER);
	}

	private void setUpCoinListByDescendingValue() {
		CoinListByDescendingValue = new ArrayList<Coin>();
		CoinListByDescendingValue.add(Coin.QUARTER);
		CoinListByDescendingValue.add(Coin.DIME);
		CoinListByDescendingValue.add(Coin.NICKEL);
		CoinListByDescendingValue.add(Coin.PENNY);
	}

	public String getDisplay() {
		if (productSoldOut) {
			productSoldOut = false;
			return "SOLD OUT";
		}

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
		moveAllCoinsFromOriginToDestination(insertedCoins, coinReturn);
		insertedCoins.clear();
	}

	public void selectProduct(Product product) {

		Map<Coin, Integer> coinsToBeReturned = new TreeMap<Coin, Integer>();

		if (!productInventory.containsKey(product)) {
			productSoldOut = true;
			return;
		}

		productPressed = true;
		productPrice = productPrices.get(product);
		if (insertedCoinValue() >= productPrice) {
			double remainder = insertedCoinValue() - productPrice;
			remainder = roundCents(remainder);

			remainder = generateChange(CoinListByDescendingValue,
					insertedCoins, coinsToBeReturned, remainder);

			if (remainder != 0) {
				Map<Coin, Integer> coinsReturnedFromInventory = new TreeMap<Coin, Integer>();

				remainder = generateChange(CoinListByDescendingValue,
						coinInventory, coinsReturnedFromInventory, remainder);

				if (remainder != 0) {
					moveAllCoinsFromOriginToDestination(
							coinsReturnedFromInventory, coinInventory);
					coinsReturnedFromInventory.clear();
				} else {
					moveAllCoinsFromOriginToDestination(
							coinsReturnedFromInventory, coinsToBeReturned);
				}

			}
			if (remainder != 0) {
				moveAllCoinsFromOriginToDestination(coinsToBeReturned,
						insertedCoins);
				coinsToBeReturned.clear();

				remainder = insertedCoinValue() - productPrice;
				remainder = roundCents(remainder);

				Collections.reverse(CoinListByDescendingValue);
				remainder = generateChange(CoinListByDescendingValue,
						insertedCoins, coinsToBeReturned, remainder);
				Collections.reverse(CoinListByDescendingValue);
			}

			if (remainder == 0) {
				purchasedProduct(product, coinsToBeReturned);

			} else {
				moveAllCoinsFromOriginToDestination(coinsToBeReturned,
						insertedCoins);
			}
		}
	}

	private void purchasedProduct(Product product,
			Map<Coin, Integer> coinsToBeReturned) {
		productPurchased = true;
		int dispencedProductCount = 0;
		if (dispensedProduct.containsKey(product))
			dispencedProductCount = dispensedProduct.get(product);
		dispensedProduct.put(product, dispencedProductCount + 1);

		int productCount = productInventory.get(product);
		productCount--;
		if (productCount > 0)
			productInventory.put(product, productCount);
		else
			productInventory.remove(product);

		moveAllCoinsFromOriginToDestination(coinsToBeReturned, coinReturn);
		moveAllCoinsFromOriginToDestination(insertedCoins, coinInventory);
		insertedCoins.clear();
	}

	private double generateChange(ArrayList<Coin> orderedCoinList,
			Map<Coin, Integer> coinSource, Map<Coin, Integer> coinsToReturned,
			double remainder) {

		for (Coin coin : orderedCoinList) {
			while (remainder >= coinValue.get(coin)
					&& coinSource.containsKey(coin)) {

				remainder = roundCents(remainder - coinValue.get(coin));
				int coinReturnCount = 0;
				if (coinsToReturned.containsKey(coin))
					coinReturnCount = coinsToReturned.get(coin);
				coinsToReturned.put(coin, coinReturnCount + 1);

				int coinInsertedCount = coinSource.get(coin);
				coinInsertedCount--;
				if (coinInsertedCount > 0)
					coinSource.put(coin, coinInsertedCount);
				else
					coinSource.remove(coin);
			}
		}
		return remainder;
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

	private void moveAllCoinsFromOriginToDestination(Map<Coin, Integer> origin,
			Map<Coin, Integer> destination) {

		for (Coin c : origin.keySet()) {
			if (destination.containsKey(c))
				destination.put(c, destination.get(c) + origin.get(c));
			else
				destination.put(c, origin.get(c));
		}
	}

	public void serviceProductInventory(Map<Product, Integer> newInventory) {
		for (Product p : newInventory.keySet()) {
			if (productInventory.containsKey(p))
				productInventory.put(p,
						productInventory.get(p) + newInventory.get(p));
			else
				productInventory.put(p, newInventory.get(p));
		}
	}

	public void serviceCoinInventory(TreeMap<Coin, Integer> coinInventoryToAdd) {
		for (Coin c : coinInventoryToAdd.keySet()) {
			if (coinInventory.containsKey(c))
				coinInventory.put(c,
						coinInventory.get(c) + coinInventoryToAdd.get(c));
			else
				coinInventory.put(c, coinInventoryToAdd.get(c));
		}
	}
}