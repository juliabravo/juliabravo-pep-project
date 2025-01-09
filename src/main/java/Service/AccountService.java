package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account) {
        if(account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }
        if(accountDAO.checkUserAccount(account.getUsername()) != null) {
            return null;     
        }

        return accountDAO.createAccount(account);
    }

    public Account login(String username, String password) {
        if(username == null || username.isBlank() || password == null || password.isBlank()) {
            return null;
        }

        return accountDAO.login(username, password);
    }

    public boolean doesUserExist(int account_id) {
        return accountDAO.doesUserExist(account_id);
    }
}
