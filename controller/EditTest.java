package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EditTest {

	@RequestMapping("/wangedit")
	public String wangedit(){
		
		return "uedit";
	}
}
