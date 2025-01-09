package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;


public class AccountDAO {
    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL logic here
            String sql = "INSERT INTO account (username, password) VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2,account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()) {
                account.setAccount_id(pkeyResultSet.getInt(1));
                return account;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                ); 
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account checkUserAccount(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,username);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean doesUserExist(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL
            String sql = "SELECT 1 FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,account_id);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();


        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
}
