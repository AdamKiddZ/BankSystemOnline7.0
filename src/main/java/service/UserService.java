package service;

import pojo.User;
import util.exception.BankException;

import java.util.List;

public interface UserService {
    int register(String username,String password) throws BankException;
    User login(int userID, String password) throws BankException;
    boolean isUserExisted(int userID) throws BankException;
    User adminLogin(int userID, String password) throws BankException;
    List<User> getAllUsers() throws BankException;
    boolean lockAccount(int userid) throws BankException;
    boolean unlockAccount(int userid) throws BankException;
}