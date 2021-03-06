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

	private VendingMachineState vendingMachineState;
	private double productPrice;

	public VendingMachine() {
		vendingMachineState = VendingMachineState.NORMAL_STATE;

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
		String display;

		switch (vendingMachineState) {
		case PRODUCT_SOLDOUT:
			vendingMachineState = VendingMachineState.NORMAL_STATE;
			display = "SOLD OUT";
			break;
		case PRODUCT_PURCHASED:
			vendingMachineState = VendingMachineState.NORMAL_STATE;
			display = "THANK YOU";
			break;
		case PRODUCT_PRESSED:
			vendingMachineState = VendingMachineState.NORMAL_STATE;
			display = "PRICE $" + String.format("%.2f", productPrice);
			break;
		default:
			if (insertedCoinValue() != 0)
				display = String.format("%.2f", insertedCoinValue());
			else if (needsExactChange())
				display = "EXACT CHANGE ONLY";
			else
				display = "INSERT COIN";
			break;
		}

		return display;
	}

	private boolean needsExactChange() {
		boolean exactChangeNeeded = true;
		if (coinInventory.containsKey(Coin.NICKEL)) {
			exactChangeNeeded = !(coinInventory.get(Coin.NICKEL) > 1);
			if (exactChangeNeeded)
				exactChangeNeeded = !(coinInventory.containsKey(Coin.DIME));
		}
		return exactChangeNeeded;
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
		Map<Coin, Integer> insertedCoinsAndCoinInventoryJoined = new TreeMap<Coin, Integer>();

		moveAllCoinsFromOriginToDestination(insertedCoins,
				insertedCoinsAndCoinInventoryJoined);
		moveAllCoinsFromOriginToDestination(coinInventory,
				insertedCoinsAndCoinInventoryJoined);

		if (!productInventory.containsKey(product)) {
			vendingMachineState = VendingMachineState.PRODUCT_SOLDOUT;
			return;
		}

		vendingMachineState = VendingMachineState.PRODUCT_PRESSED;
		productPrice = productPrices.get(product);
		if (insertedCoinValue() >= productPrice) {
			double remainder = roundCents(insertedCoinValue() - productPrice);

			remainder = generateChange(CoinListByDescendingValue,
					insertedCoinsAndCoinInventoryJoined, coinsToBeReturned,
					remainder);

			if (remainder != 0) {

				remainder = roundCents(insertedCoinValue() - productPrice);

				moveAllCoinsFromOriginToDestination(coinsToBeReturned,
						insertedCoinsAndCoinInventoryJoined);
				coinsToBeReturned.clear();

				Collections.reverse(CoinListByDescendingValue);
				remainder = generateChange(CoinListByDescendingValue,
						insertedCoinsAndCoinInventoryJoined, coinsToBeReturned,
						remainder);
				Collections.reverse(CoinListByDescendingValue);
			}

			if (remainder == 0) {
				purchasedProduct(product, coinsToBeReturned);
				coinInventory = insertedCoinsAndCoinInventoryJoined;

			}
		}
	}

	private void purchasedProduct(Product product,
			Map<Coin, Integer> coinsToBeReturned) {
		vendingMachineState = VendingMachineState.PRODUCT_PURCHASED;
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