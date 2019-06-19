package com.example.sms2017;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {

	@RequestMapping("/")
	public ModelAndView firstPage() {
		return new ModelAndView("welcome");
	}
}
