package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.nt.entity.UserMaster;

public interface IUserMgmtRepository extends JpaRepository<UserMaster, Integer>{
	
	public UserMaster findByEmailAndPassword(String mail,String password);

	public UserMaster findByEmail(String email);

}
