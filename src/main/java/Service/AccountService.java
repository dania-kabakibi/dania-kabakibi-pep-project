package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    public Account loginAccount(Account account) {
        if(!account.getUsername().isBlank()) { 
            Account test_account = accountDAO.getAccountByUsername(account.getUsername());
            if(test_account != null) {
                if(account.getPassword().equals(test_account.getPassword())){ // passwords match
                    return test_account;
                }                
            }
        }
        return null;
    }

    public boolean accountExist(int account_id) {
        if(account_id >= 0){ 
            Account test_account = accountDAO.getAccountById(account_id);
            return test_account != null;
        }
        return false;
    }

}
