package com.nt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.service.IUserMgmtService;
import com.nt.binding.ActivateUser;
import com.nt.binding.LoginCredentials;
import com.nt.binding.RecoverPassword;
import com.nt.binding.UserAccount;

@RequestMapping("/user-api")
@RestController
public class UserMgmtOpsController {
	
	@Autowired
	private IUserMgmtService service;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody UserAccount user){
		
		try {
			String msg = service.registerUser(user);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
		
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	@PostMapping("/activate")
	public ResponseEntity<String> activateUser(@RequestBody ActivateUser user){
		
		try {
			String msg = service.activateUser(user);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
		
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
		
	@PostMapping("/login")
	public ResponseEntity<String> activateUser(@RequestBody LoginCredentials credentials){
		
		try {
			String msg = service.login(credentials);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
		
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/reportById/{id}")
	public ResponseEntity<?> showUsersById(@PathVariable Integer id){
		
		try {
			 UserAccount userAccount = service.showUserById(id);
			return new ResponseEntity<UserAccount>(userAccount,HttpStatus.OK);
		} catch (Exception e) {
	
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@GetMapping("/report")
	public ResponseEntity<?> showUsers(){
		
		try {
			List<UserAccount> listUser = service.listUser();
			return new ResponseEntity<List<UserAccount>>(listUser,HttpStatus.OK);
		} catch (Exception e) {
	
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	@GetMapping("/reportByEmail/{email}")
	public ResponseEntity<?> showUsersByEmail(@PathVariable String email){
		
		try {
			UserAccount userAccount = service.showUserByEmail(email);
			return new ResponseEntity<UserAccount>(userAccount,HttpStatus.OK);
		} catch (Exception e) {
		
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/update")
	public ResponseEntity<String> updateUserDetails(@RequestBody UserAccount user){
		
		try {
			String msg = service.updateUser(user);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer id){
		
		try {
			String msg = service.deleteUserById(id);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@PatchMapping("/chage-status/{id}/{status}")
	public ResponseEntity<String> changeUserStatus(@PathVariable Integer id,@PathVariable String status){
		
		try {
			String msg = service.changeUserStatus(id, status);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
	
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("recover-password")
	public ResponseEntity<String> recoverPassword(@RequestBody RecoverPassword password){
		
		try {
			String msg = service.recoverPassword(password);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		} catch (Exception e) {
	
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
