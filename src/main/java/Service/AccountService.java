package Service;

import Model.Account;
import DAO.AccountDAO;


public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        if ((password.length() < 4) || (username.length() == 0)) {
            return null;
        }
        else {
            return accountDAO.insertAccount(account);
        }
    }

    public Account checkAccount(Account account) {
        return accountDAO.lookUpAccount(account);
    }
    
}
