package com.nt.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import com.nt.binding.ActivateUser;
import com.nt.binding.LoginCredentials;
import com.nt.binding.RecoverPassword;
import com.nt.binding.UserAccount;
import com.nt.entity.UserMaster;
import com.nt.repository.IUserMgmtRepository;
import com.nt.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements IUserMgmtService {

	@Autowired
	private IUserMgmtRepository userrepo;
	@Autowired
	private Environment prop;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public String registerUser(UserAccount user) throws Exception {

		// convert UserAccount data to UserMaster data type

		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(user, master);
		// use static method to generate password
		String tempPassword=generateRandomPwd(6);
		master.setPassword(tempPassword);
		master.setActive_sw("InActive");
		// save object
		UserMaster savedEntity = userrepo.save(master);

		// do send mail operation
		String subject = "user registration success";
		
		String body=readEmailMessageBody(prop.getProperty("mailBody.registerUser.location"),user.getName(),tempPassword);

		emailUtils.sendEmailMessage(user.getEmail(), subject, body);

		return savedEntity.getUserId()!= null ? "user registered with id value::" + savedEntity.getUserId()
				: "problem in user registration";
	}

	@Override
	public String activateUser(ActivateUser user) {

		UserMaster master = new UserMaster();
		master.setEmail(user.getEmail());
		master.setPassword(user.getTempPassword());
		// check whether the record with given credentials are available
		Example<UserMaster> example = Example.of(master);
		List<UserMaster> list = userrepo.findAll(example);
		// if email and new password are valid update and activate user

		if (list != null) {
			UserMaster userMaster = list.get(0);
			userMaster.setPassword(user.getConfirmPassword());
			userMaster.setActive_sw("Active");
			userrepo.save(userMaster);
			return "user is activated";
		}

		return "user not found for activation";
	}
	/*
	 * @Override public String activateUser(ActivateUser user) {
	 * 
	 * // use findByMethod UserMaster userMaster =
	 * userrepo.findByEmailAndPassword(user.getEmail(), user.getTempPassword()); if
	 * (userMaster == null) { return "user is not found for activation"; } else {
	 * userMaster.setPassword(user.getConfirmPassword());
	 * userMaster.setActive_sw("Active"); UserMaster savedEntity =
	 * userrepo.save(userMaster); return "user activated with new password"; }
	 * 
	 * }
	 */

	@Override
	public String login(LoginCredentials credentials) {

		// login by email and password
		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(credentials, master);
		Example<UserMaster> example = Example.of(master);
		List<UserMaster> listEntity = userrepo.findAll(example);
		if (listEntity.size() == 0) {
			return "invalid credentials";
		} else {
			UserMaster entity = listEntity.get(0);
			if (entity.getActive_sw().equalsIgnoreCase("Active")) {
				return "valid credentials and login successful";
			} else {
				return "user account is not active";
			}
		}

	}

	@Override
	public List<UserAccount> listUser() {
		/*
		 * //use stream api
		 * 
		 * List<UserAccount> list = userrepo.findAll().stream().map(res->{ UserAccount
		 * user=new UserAccount(); BeanUtils.copyProperties(res, user); return user;
		 * }).toList(); return list;
		 */

		List<UserMaster> listEntities = userrepo.findAll();
		// convert all entities to user account objects
		List<UserAccount> listUsers = new ArrayList<>();
		listEntities.forEach(entity -> {
			UserAccount user = new UserAccount();
			BeanUtils.copyProperties(entity, user);
			listUsers.add(user);
		});
		return listUsers;
	}

	@Override
	public UserAccount showUserById(Integer id) {
		Optional<UserMaster> opt = userrepo.findById(id);
		UserAccount user = null;
		if (opt.isPresent()) {
			BeanUtils.copyProperties(opt.get(), user);
			return user;

		}
		return user;
	}

	@Override
	public UserAccount showUserByEmail(String email) {
		UserMaster master = userrepo.findByEmail(email);
		UserAccount account = null;
		if (master != null) {
			BeanUtils.copyProperties(master, account);

		}
		return account;
	}

	@Override
	public String updateUser(UserAccount user) {
		UserMaster master = userrepo.findByEmail(user.getEmail());
		if (master != null) {
			BeanUtils.copyProperties(user, master);
			userrepo.save(master);
			return "user details are saved";
		}
		return "user not found for updation";
	}

	@Override
	public String deleteUserById(Integer id) {
		Optional<UserMaster> opt = userrepo.findById(id);
		if (opt.isPresent()) {
			userrepo.deleteById(id);
			return "user deleted";
		}
		return "user not found for deletion";
	}

	@Override
	public String changeUserStatus(Integer id, String status) {
		Optional<UserMaster> opt = userrepo.findById(id);
		if (opt.isPresent()) {
			UserMaster userMaster = opt.get();
			userMaster.setActive_sw(status);
			userrepo.save(userMaster);
			return "user status changed";

		}
		return "user status not changed";
	}

	@Override
	public String recoverPassword(RecoverPassword recover) throws Exception {

		UserMaster master = userrepo.findByEmail(recover.getEmail());
		if (master != null) {
			String password = master.getPassword();
			// do send mail with password

			String subject="recovery password mail";
			String pwd=master.getPassword();
			String mailBody=readEmailMessageBody(prop.getProperty("mailBody.recoverPassword.location"),recover.getName(),pwd);
			emailUtils.sendEmailMessage(recover.getEmail(), subject, mailBody);
			return password;
		}

		return "user's email not found";
	}

	private static String generateRandomPwd(int length) {

		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {

			int ch = (int) (alphaNumeric.length() * Math.random());
			sb.append(alphaNumeric.charAt(ch));
		}
		return sb.toString();
	}

	private String readEmailMessageBody(String fileName, String fullName, String password) throws Exception {

		String mailBody = null;
		String url = "";
		try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {

			StringBuffer buffer = new StringBuffer();
			String line = null;

			do {
				line = br.readLine();
				buffer.append(line);

			} while (line != null);

			mailBody = buffer.toString();
			mailBody=mailBody.replace("{FULL-NAME}", fullName);
			mailBody=mailBody.replace("{TEMP-PWD}", password);
			mailBody=mailBody.replace("{URL}",url);
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
		return mailBody;

	}
}
