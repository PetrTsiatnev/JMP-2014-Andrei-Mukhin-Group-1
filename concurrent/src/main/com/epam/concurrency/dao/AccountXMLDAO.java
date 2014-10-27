/**
 * AccountXMLDAO
 */
package com.epam.concurrency.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.bind.JAXBException;
import com.epam.concurrency.exceptions.DAOException;
import com.epam.concurrency.model.Account;
import com.epam.concurrency.model.jaxb.Accounts;
import com.epam.concurrency.utils.ConfigurationManager;
import com.epam.concurrency.utils.JAXBSerializationHelper;
import com.epam.concurrency.utils.ModelIdUtil;


/**
 * @author I7eter
 *
 */
public final class AccountXMLDAO implements IAccountDAO {

	/**
	 * 
	 */
	private static File accountFile = new File(
			ConfigurationManager.getProperty(ConfigurationManager.XML_ACCOUNT_PATH));

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	public AccountXMLDAO() {
	}

	/* (non-Javadoc)
	 * @see com.epam.concurrency.dao.IAccountDAO#getList()
	 */
	@Override
	public List<Account> getList() throws DAOException {
		List<Account> accounts = null;
		try {
			readLock.lock();
			accounts = unmarshal();
		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
		return accounts;
	}

	/* (non-Javadoc)
	 * @see com.epam.concurrency.dao.IAccountDAO#save(com.epam.concurrency.model.Account)
	 */
	@Override
	public long save(Account account) throws DAOException {
		writeLock.lock();
		try {
			List<Account> storedAccounts = getList();
			long curAccountId = ModelIdUtil.getMaxAccountId(storedAccounts) + 1;
			/** obtain new accountId */
			account.setAccountId( curAccountId + 1);
			/** add to existed collection of accounts */
			storedAccounts.add(account);
			marshal(storedAccounts);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writeLock.unlock();
		}
		return account.getAccountId();
	}

	/* (non-Javadoc)
	 * @see com.epam.concurrency.dao.IAccountDAO#remove(java.lang.long)
	 */
	@Override
	public boolean remove(long id) throws DAOException {

		List<Account> storedAccounts = getList();
		for(Account account : storedAccounts) {
			if(account.getAccountId() == id) {
				storedAccounts.remove(account);
				writeLock.lock();
				try {
					marshal(storedAccounts);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JAXBException e) {
					e.printStackTrace();
				} finally {
					writeLock.unlock();
				}
			}
		}
		return false;
	}


	@Override
	public List<Account> fetchByPersonId(long id) throws DAOException {
		List<Account> fetchedAaccounts = new ArrayList<Account>();

		List<Account> allAaccounts = getList();
		for(Account account : allAaccounts) {
			if((account.getOwner() != null) && (account.getOwner().getPersonId() == id)) {
				fetchedAaccounts.add(account);
			}
		}

		return fetchedAaccounts;
	}
	
	@Override
	public Account fetchById(long id) throws DAOException {

		List<Account> allAaccounts = getList();
		if(allAaccounts.size() > 0) {
			for(Account account : allAaccounts) {
				if(account.getAccountId() == id) {
					return account;
				}
			}
		}

		return new Account();
	}

	public static void marshal(List<Account> accounts)
			throws IOException, JAXBException {
		JAXBSerializationHelper.marshal(new Accounts(accounts), Accounts.class, accountFile);
	}

	public static List<Account> unmarshal() throws JAXBException {
		if(accountFile.exists() && (accountFile.length() > 0)) {
			 Accounts accounts = ((Accounts) JAXBSerializationHelper
					 .unmarshal(Accounts.class, accountFile));
			return accounts.getAccounts();
		}

		return new ArrayList<Account>();
	}

	@Override
	public boolean edit(Account currentAccount) throws DAOException {
		writeLock.lock();
		try {
			List<Account> accounts = getList();
			for(Account account : accounts) {
				if(account.getAccountId() == currentAccount.getAccountId()) {
					accounts.remove(account);
					accounts.add(currentAccount);
					try {
						marshal(accounts);
						return true;
					} catch (JAXBException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} finally {
			writeLock.unlock();
		}
		return false;
	}

}
