package com.epam.concurrency.menu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.concurrency.menu.action.AddAccountAction;
import com.epam.concurrency.menu.action.AddClientAction;
import com.epam.concurrency.menu.action.AssignPersonAction;
import com.epam.concurrency.menu.action.ExchangeCurrenciesAction;
import com.epam.concurrency.menu.action.SelectAccountAction;
import com.epam.concurrency.menu.action.SelectBankAction;
import com.epam.concurrency.menu.action.ShowAccountsAction;
import com.epam.concurrency.menu.action.ShowPersonsAction;

public class MenuManager {

	private MenuManager() {
	}
	
	public static Menu createMenu() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		/** Menu creation */
		Menu root = new Menu("root");
		Menu banks = new Menu("Banks", (SelectBankAction)context.getBean("selectBankAction"));
		root.addItem(banks);
		/** creating menu 'clients'*/
        Menu clients = new Menu("Clients", (ShowPersonsAction)context.getBean("showPersonsAction"));
        banks.addItem(clients);

        AbstractMenuItem addClientMenu = new MenuItem("Add Client", (AddClientAction)context.getBean("addClientAction"));
        clients.addItem(addClientMenu);
        clients.addItem(new MenuItem(MenuItemType.BACK));
        clients.addItem(new MenuItem(MenuItemType.EXIT));
        
        Menu accounts = new Menu("Accounts", (ShowAccountsAction)context.getBean("showAccountsAction"));
        banks.addItem(accounts);
        Menu selectAccountMenu = new Menu("Select Account", (SelectAccountAction)context.getBean("selectAccountAction"));
        Menu addAccountMenu = new Menu("Add Account", (AddAccountAction)context.getBean("addAccountAction"));
        Menu assignPersonMenu = new Menu("Assign Person", (AssignPersonAction)context.getBean("assignPersonAction"), MenuItemType.BACK);
        assignPersonMenu.addItem(new MenuItem(MenuItemType.BACK));
        assignPersonMenu.addItem(new MenuItem(MenuItemType.EXIT));
        addAccountMenu.addItem(assignPersonMenu);
        addAccountMenu.addItem(new MenuItem(MenuItemType.BACK));
        addAccountMenu.addItem(new MenuItem(MenuItemType.EXIT));

        Menu exchangeMenu = new Menu("Exchange currency", (ExchangeCurrenciesAction)context.getBean("exchangeCurrenciesAction"), MenuItemType.BACK);
        exchangeMenu.addItem(new MenuItem(MenuItemType.BACK));
        exchangeMenu.addItem(new MenuItem(MenuItemType.EXIT));
        selectAccountMenu.addItem(assignPersonMenu);
        selectAccountMenu.addItem(exchangeMenu);
        selectAccountMenu.addItem(new MenuItem(MenuItemType.BACK));
        selectAccountMenu.addItem(new MenuItem(MenuItemType.EXIT));
        accounts.addItem(addAccountMenu);
        accounts.addItem(selectAccountMenu);
        accounts.addItem(new MenuItem(MenuItemType.BACK));
        accounts.addItem(new MenuItem(MenuItemType.EXIT));
        
        Menu currencies = new Menu("Currencies");
        banks.addItem(currencies);
        AbstractMenuItem addCurrencyMenu = new MenuItem("Add Currency");
        currencies.addItem(addCurrencyMenu);
        currencies.addItem(new MenuItem(MenuItemType.BACK));
        currencies.addItem(new MenuItem(MenuItemType.EXIT));

        banks.addItem(new MenuItem(MenuItemType.BACK));
        banks.addItem(new MenuItem(MenuItemType.EXIT));
        root.addItem(new MenuItem(MenuItemType.EXIT));

        return root;
	}

}
