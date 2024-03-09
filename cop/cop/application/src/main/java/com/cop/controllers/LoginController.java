package com.cop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class LoginController {
	@GetMapping("/")
    public String home1() {
        return "/login";
    }

    @GetMapping("/home")
    public String home() {
        return "/home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin-home";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/login")
    public String login() {
//        return "/login";
    	return "/MainLogin";
//    	return "/NewFile";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
    
//    @RequestMapping("/login")
//	public String login(Model model) {
//		model.addAttribute("classActiveLogin", true);
//		System.out.println("login");
//		return "admin-home";
//	}
    
    @RequestMapping("/forgetPassword")
	public String forgetPassword(Model model) {
		model.addAttribute("classActiveForgetPassword", true);
		return "myAccount";
	} 
	


}
