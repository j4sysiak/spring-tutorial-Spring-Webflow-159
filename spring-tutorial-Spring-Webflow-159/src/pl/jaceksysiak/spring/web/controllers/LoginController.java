package pl.jaceksysiak.spring.web.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jaceksysiak.spring.web.dao.FormValidationGroup;
import pl.jaceksysiak.spring.web.dao.User;
import pl.jaceksysiak.spring.web.service.UsersService;

@Controller
public class LoginController {
	
	private UsersService usersService;
	
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	@RequestMapping("/login")
	public String showLogin(){
		return "login";
	}
	
	
	@RequestMapping("/denied")
	public String showDenied(){
		return "denied";
	}

	@RequestMapping("/admin")
	public String showAdmin(Model model){
		
		List<User> users = usersService.getAllUsers();
		model.addAttribute("users", users);
			
		return "admin";
	}
	
	@RequestMapping("/loggedout")
	public String showLoggedOut(){
		return "loggedout";
	}
	
	@RequestMapping("/newaccount")
	public String showNewAccount(Model model){
		
		model.addAttribute("user", new User());
		return "newaccount";
	}
	
   
	
	
	@RequestMapping(value="/createaccount", method=RequestMethod.POST)
	public String createAccount(@Validated(FormValidationGroup.class) User user, BindingResult result){
		
		//System.out.println(offer);
		
		if(result.hasErrors()){
			return "newaccount";
		}
		
		user.setAuthority("ROLE_USER");
		user.setEnabled(true);
		
		if(usersService.exists(user.getUsername())){
			System.out.println("Caught duplicate username!");
			result.rejectValue("username", "DuplicateKey.user.username");
			return "newaccount";
		}
		
		try {
			usersService.create(user);
		} catch (DuplicateKeyException e) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return "newaccount";
		}
		
		
		return "accountcreated";
	}
}




































