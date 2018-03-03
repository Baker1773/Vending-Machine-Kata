package main;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class VendingMachineTest {

	VendingMachine vendingMachine;

	@Before
	public void setUp() {
		vendingMachine = new VendingMachine();
		TreeMap<Product, Integer> newInventory = new TreeMap<Product, Integer>();
		newInventory.put(Product.COLA, 5);
		newInventory.put(Product.CHIPS, 5);
		newInventory.put(Product.CANDY, 5);
		vendingMachine.serviceProductInventory(newInventory);
	}

	@Test
	public void whenNoCoinsAreInsertedVendingMachinedisplaysINSERTCOIN() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsNickelByDisplayingCurrentAmount() {
		vendingMachine.insertCoin(Coin.NICKEL);
		assertEquals("0.05", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsDimeByDisplayingCurrentAmount() {
		vendingMachine.insertCoin(Coin.DIME);
		assertEquals("0.10", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsQuarterByDisplayingCurrentAmount() {
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.25", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsMultipleDimesByDisplayingCurrentAmount() {
		vendingMachine.insertCoin(Coin.DIME);
		assertEquals("0.10", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.DIME);
		assertEquals("0.20", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsMultipleNickelsByDisplayingCurrentAmount() {
		vendingMachine.insertCoin(Coin.NICKEL);
		assertEquals("0.05", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.NICKEL);
		assertEquals("0.10", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineAcceptsMultipleQuartersByDisplayingCurrentAmount() {
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.25", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.50", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineDoesNotAcceptPennyWhenNoOtherCoinsAreInserted() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		vendingMachine.insertCoin(Coin.PENNY);
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineDoesNotAcceptPennyWhenOtherCoinsAreInserted() {
		vendingMachine.insertCoin(Coin.QUARTER);
		assertEquals("0.25", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.PENNY);
		assertEquals("0.25", vendingMachine.getDisplay());
	}

	@Test
	public void whenAPennyIsRejectedItIsPlacedInTheCoinReturn() {
		vendingMachine.insertCoin(Coin.PENNY);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void whenTwoPenniesAreInsertedBothAreInTheCoinReturn() {
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.insertCoin(Coin.PENNY);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(2, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void newVendingMachinesWillHaveAnEmptyCoinReturn() {
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(0, coinsReturned.size());
	}

	@Test
	public void whenCoinReturnIsCheckedItIsEmptied() {
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.insertCoin(Coin.PENNY);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(2, (int) coinsReturned.get(Coin.PENNY));
		coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(0, coinsReturned.size());
	}

	@Test
	public void insertingAQuarterThenPressingTheReturnButtonWillReturnAQuarter() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void pressingTheReturnButtonWillReturnMultipleCoins() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(3, coinsReturned.size());
		assertEquals(2, (int) coinsReturned.get(Coin.QUARTER));
		assertEquals(3, (int) coinsReturned.get(Coin.DIME));
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void coinsReturnedFromCoinReturnButtonWillCombineWithRejectedPennies() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.insertCoin(Coin.PENNY);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(4, coinsReturned.size());
		assertEquals(2, (int) coinsReturned.get(Coin.QUARTER));
		assertEquals(3, (int) coinsReturned.get(Coin.DIME));
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
		assertEquals(2, (int) coinsReturned.get(Coin.PENNY));
	}

	@Test
	public void insertingAQuarterThenPressingTheReturnButtonWillResetTheDisplay() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void pressingTheReturnCoinsButtonTwiceInARowWillOnlyReturnTheCoinsOnce() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void insertingAQuarterPressingReturnAndInsertertingAnotherQuarterAndPressingReturnWillResultInTwoQuartersInTheCoinReturn() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(2, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void insertingAQuarterThenReturningAndEmpyingTheCoinReturnTwiceWillEndUpWithOneQuarterEachTime() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.pressReturnCoinButton();
		coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void selectColaWithNoMoneyInTheVendingMachineWillDisplayThePrice() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.selectProduct(Product.COLA);
		assertEquals("PRICE $1.00", vendingMachine.getDisplay());
	}

	@Test
	public void checkingTheDisplayAfterThePriceForColaIsDisplayedWillDisplayInsertCoin() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.selectProduct(Product.COLA);
		assertEquals("PRICE $1.00", vendingMachine.getDisplay());
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void selectChipsWithNoMoneyInTheVendingMachineWillDisplayThePrice() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.selectProduct(Product.CHIPS);
		assertEquals("PRICE $0.50", vendingMachine.getDisplay());
	}

	@Test
	public void checkingTheDisplayAfterThePriceForChipsIsDisplayedWillDisplayInsertCoin() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.selectProduct(Product.CHIPS);
		assertEquals("PRICE $0.50", vendingMachine.getDisplay());
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void selectCandyWithNoMoneyInTheVendingMachineWillDisplayThePrice() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.selectProduct(Product.CANDY);
		assertEquals("PRICE $0.65", vendingMachine.getDisplay());
	}

	@Test
	public void checkingTheDisplayAfterThePriceForCandyIsDisplayedWillDisplayInsertCoin() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.selectProduct(Product.CANDY);
		assertEquals("PRICE $0.65", vendingMachine.getDisplay());
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void checkingTheDisplayWithSomeMoneyButInsufficientMoneyForAProduct() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.CANDY);
		assertEquals("PRICE $0.65", vendingMachine.getDisplay());
		assertEquals("0.25", vendingMachine.getDisplay());
	}

	@Test
	public void pressingColaWithExactChangeWillResultInTheDisplaySayingThankYou() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		assertEquals("THANK YOU", vendingMachine.getDisplay());
	}

	@Test
	public void afterDisplayingThankYouTheVendingMachineWillShowInsertCoinAgain() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		assertEquals("THANK YOU", vendingMachine.getDisplay());
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void pressingChipsWithExactChangeWillResultInTheDisplaySayingThankYou() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.CHIPS);
		assertEquals("THANK YOU", vendingMachine.getDisplay());
	}

	@Test
	public void pressingCandyWithExactChangeWillResultInTheDisplaySayingThankYou() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.CANDY);
		assertEquals("THANK YOU", vendingMachine.getDisplay());
	}

	@Test
	public void afterPurchasingAColaAColaIsInTheDispenser() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(1, (int) dispensedProducts.get(Product.COLA));
	}

	@Test
	public void afterPurchasingAChipsAChipsIsInTheDispenser() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.CHIPS);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(1, (int) dispensedProducts.get(Product.CHIPS));
	}

	@Test
	public void afterPurchasingACandyACandyIsInTheDispenser() {
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.CANDY);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(1, (int) dispensedProducts.get(Product.CANDY));
	}

	@Test
	public void afterPurchasingTwoColasThereAreTwoColasInTheDispenser() {
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(2, (int) dispensedProducts.get(Product.COLA));
	}

	@Test
	public void newVendingMachinesHaveNothingInTheDispenser() {
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(0, dispensedProducts.size());
	}

	@Test
	public void afterGettingProductsFromTheDispenserTheDispenserIsEmpty() {
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(1, (int) dispensedProducts.get(Product.COLA));
		dispensedProducts = vendingMachine.getDispensedProducts();
		assertEquals(0, dispensedProducts.size());
	}

	@Test
	public void afterDisplayingThankYouForChipsTheVendingMachineWillShowInsertCoinAgain() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.CHIPS);
		assertEquals("THANK YOU", vendingMachine.getDisplay());
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void afterDisplayingThankYouForCandyTheVendingMachineWillShowInsertCoinAgain() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		assertEquals("INSERT COIN", vendingMachine.getDisplay());
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.CANDY);
		assertEquals("THANK YOU", vendingMachine.getDisplay());
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void productsNotPurchasedWillNotBeDispensed() {
		vendingMachine.selectProduct(Product.COLA);
		vendingMachine.selectProduct(Product.CHIPS);
		vendingMachine.selectProduct(Product.CANDY);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(0, dispensedProducts.size());
	}

	@Test
	public void returnAQuarterAfterGivingTheMachine5QuartersForACola() {
		for (int i = 0; i < 5; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void whenPurchasingAProductWithToMuchMoneyTheMachineWillDisplayThankYouThenInsertCoins() {

		for (int i = 0; i < 5; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		assertEquals("THANK YOU", vendingMachine.getDisplay());
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void whenPurchasingAProductWithToMuchMoneyTheMachineWillDispenceTheProduct() {
		for (int i = 0; i < 5; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(1, (int) dispensedProducts.get(Product.COLA));
	}

	@Test
	public void returnsCorrectChangeAfterGivingTheMachine4QuartersAndANickleForACola() {
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void returnsCorrectChangeAfterGivingTheMachine5QuartersAndANickleForACola() {
		for (int i = 0; i < 5; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(2, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void returnsCorrectChangeAfterGivingTheMachine4QuartersAndADimeForACola() {
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.DIME));
	}

	@Test
	public void returnsCorrectChangeAfterGivingTheMachine6Quarters1DimeAnd1NickleForACola() {
		for (int i = 0; i < 6; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(3, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
		assertEquals(1, (int) coinsReturned.get(Coin.DIME));
		assertEquals(2, (int) coinsReturned.get(Coin.QUARTER));
	}

	@Test
	public void returnsCorrectChangeAfterGivingTheMachine1Quarter14DimeAnd2NickleForACola() {
		vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 14; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(2, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.QUARTER));
		assertEquals(5, (int) coinsReturned.get(Coin.DIME));
	}

	@Test
	public void returnsCorrectChangeAfterGivingTheMachine40NickleForACola() {
		for (int i = 0; i < 40; i++)
			vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(20, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void returnsCorrectChangeAfterGivingTheMachine40NickleAnd3DimeForACola() {
		for (int i = 0; i < 40; i++)
			vendingMachine.insertCoin(Coin.NICKEL);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(2, coinsReturned.size());
		assertEquals(3, (int) coinsReturned.get(Coin.DIME));
		assertEquals(20, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void whenChangeIsUnableToBeMadeNoPurchaseWillOccure() {
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(0, coinsReturned.size());
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(0, dispensedProducts.size());
	}

	@Test
	public void whenChangeIsUnableToBeMadeInsertedCoinsStayInMachine() {
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		vendingMachine.getDisplay();
		assertEquals("1.05", vendingMachine.getDisplay());
	}

	@Test
	public void whenChangeIsUnableToBeMadeNoPurchaseWillOccureWithExtraDimes() {
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(0, coinsReturned.size());
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(0, dispensedProducts.size());
	}

	@Test
	public void vendingMachineWillShowSoldOutWhenAttemptingToPurchaseAColaAndColaIsSoldOut() {
		vendingMachine = new VendingMachine();
		vendingMachine.selectProduct(Product.COLA);
		assertEquals("SOLD OUT", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineWillNotDispenceAProductWhenProductIsOutOfStock() {
		vendingMachine = new VendingMachine();
		vendingMachine.selectProduct(Product.COLA);
		assertEquals(0, vendingMachine.getDispensedProducts().size());
	}

	@Test
	public void afterDisplayingSoldOutDisplayWillGoBackToInsertCoinOnAMachineWithNoCoinsInserted() {
		vendingMachine = new VendingMachine();

		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 2);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		vendingMachine.selectProduct(Product.COLA);
		assertEquals("SOLD OUT", vendingMachine.getDisplay());
		assertEquals("INSERT COIN", vendingMachine.getDisplay());
	}

	@Test
	public void afterDisplayingSoldOutDisplayWillGoBackToCoinAmountOnAMachineWithCoinsInserted() {
		vendingMachine = new VendingMachine();
		vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		assertEquals("SOLD OUT", vendingMachine.getDisplay());
		assertEquals("0.25", vendingMachine.getDisplay());
	}

	@Test
	public void onlyOneColaCanBePurchasedIfTheMachineHasOneCola() {
		vendingMachine = new VendingMachine();
		TreeMap<Product, Integer> newInventory = new TreeMap<Product, Integer>();
		newInventory.put(Product.COLA, 1);
		vendingMachine.serviceProductInventory(newInventory);
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		vendingMachine.getDisplay();
		for (int i = 0; i < 4; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.COLA);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(1, (int) dispensedProducts.get(Product.COLA));
	}

	@Test
	public void givingTheMachine14QuartersAnd9DimesForACola() {
		for (int i = 0; i < 14; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 9; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(1, dispensedProducts.size());
		assertEquals(1, (int) dispensedProducts.get(Product.COLA));
	}

	@Test
	public void whenANickelIsPreloadedIntoTheMachineAnd3QuartersAnd3DimesAreUsedToBuyAColaANickelIsReturned() {

		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 1);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void vendingMachineWillReturnANickelFromFirstPurchaseAsChangeForSecondPurachase() {
		for (int i = 0; i < 20; i++)
			vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.COLA);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(1, (int) coinsReturned.get(Coin.NICKEL));

	}

	@Test
	public void vendingMachineWillReturnTwoNickelFromFirstPurchaseAsChangeForSecondPurachase() {
		for (int i = 0; i < 20; i++)
			vendingMachine.insertCoin(Coin.NICKEL);
		vendingMachine.selectProduct(Product.COLA);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		vendingMachine.selectProduct(Product.CANDY);
		Map<Coin, Integer> coinsReturned = vendingMachine.emptyCoinReturn();
		assertEquals(1, coinsReturned.size());
		assertEquals(2, (int) coinsReturned.get(Coin.NICKEL));
	}

	@Test
	public void vendingMachineWillFailToMakeChangeAfterBeingLoadedWithTheWrongTypeOfCoinsToMakeChange() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.QUARTER, 20);
		coinInventoryToAdd.put(Coin.DIME, 20);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);

		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.QUARTER);
		for (int i = 0; i < 3; i++)
			vendingMachine.insertCoin(Coin.DIME);
		vendingMachine.selectProduct(Product.COLA);
		Map<Product, Integer> dispensedProducts = vendingMachine
				.getDispensedProducts();
		assertEquals(0, dispensedProducts.size());
	}

	@Test
	public void vendingMachineWillNoCoinsAlreadyInsertedWillDisplayExactChangeOnlyInsteadOfInsertCoin() {
		assertEquals("EXACT CHANGE ONLY", vendingMachine.getDisplay());
	}

	@Test
	public void vendingMachineWillShowExactChangeOnlyIfOnly1NickelIsInserted() {
		TreeMap<Coin, Integer> coinInventoryToAdd = new TreeMap<Coin, Integer>();
		coinInventoryToAdd.put(Coin.NICKEL, 1);
		vendingMachine.serviceCoinInventory(coinInventoryToAdd);
		assertEquals("EXACT CHANGE ONLY", vendingMachine.getDisplay());
	}
}
