package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    Connection connection = ConnectionUtil.getConnection();
    List<Account> accounts = new ArrayList<>();

    public List<Account> getAllAccounts(){
        try{
        String sql = "SELECT * FROM account;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Account account = new Account(rs.getInt("account_id"), 
                rs.getString("username"), 
                rs.getString("password"));
            accounts.add(account);
        }
    } catch(SQLException e){
        System.out.println(e.getMessage());
    }

        return accounts;
    }

    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account(username, password) VALUES(?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                int accountId = resultSet.getInt(1);   
                account.setAccount_id(accountId);
                return account;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isUsernameTaken(String username) {
        return accounts.stream().anyMatch(account -> account.getUsername().equals(username));
    }

    public Account findAccountByUsername(String username) {
        return accounts.stream()
                       .filter(account -> account.getUsername().equals(username))
                       .findFirst()
                       .orElse(null);
    }

}
