package com.nt.binding;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {

	private Integer userId;
	private String name;
	private String email;
	private Long mobileNo;
	private String gender = "Female";
	private LocalDate dob = LocalDate.now();
	private Long aadharNo;

}
