package com.nt.service;

import java.util.List;

import com.nt.binding.ActivateUser;
import com.nt.binding.LoginCredentials;
import com.nt.binding.RecoverPassword;
import com.nt.binding.UserAccount;


public interface IUserMgmtService {

	public String registerUser(UserAccount master) throws Exception;
	public String activateUser(ActivateUser user);
	public String login(LoginCredentials credentials);
	public List<UserAccount> listUser();
	public UserAccount showUserById(Integer id);
	public UserAccount showUserByEmail(String email);
	public String updateUser(UserAccount user);
	public String deleteUserById(Integer id);
	public String changeUserStatus(Integer id,String status);
	public String recoverPassword(RecoverPassword recover) throws Exception;
	
	
}
