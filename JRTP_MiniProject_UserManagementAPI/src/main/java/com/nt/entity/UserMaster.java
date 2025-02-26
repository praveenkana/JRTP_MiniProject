package com.nt.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;
	@Column(length=30)
	private String name;
	@Column(length=30)
	private String password;
	@Column(length=30,unique=true,nullable = false)
	private String email;
	private Long mobileNo;
	private Long aadharNo;
	@Column(length=10)
	private String gender;
	private LocalDate dob;
	@Column(length=10)
	private String active_sw; 
	
	//metadata
	@Column(length=20)
	private String createdBy;
	@Column(length=20)
	private String updatedBy;
	@Column(insertable=true,updatable=false)
	private LocalDate updatedOn;
	@Column(updatable = false,insertable=true)
	@CreationTimestamp
	private LocalDate createdOn;

}
