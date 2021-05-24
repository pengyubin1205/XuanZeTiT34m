package controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Pojo.User;
import service.UserService;
import utils.ImageUtil;

@Controller
@RequestMapping("zuke")
public class SignController {
	@Autowired
	private UserService userService;

	@RequestMapping("sign")
	public String signView(){
		
		return "signup";
	}
	
	/**
	 * 产生随机验证码
	 * 使用程序产生随机的数据 将数据响应给客户端
	 * 将产生的数据 保存在session域中 
	 * 用户输入的数据和从session域中的数据进行比较 
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("getrandom")
	public void genActiveCode(HttpServletResponse response,HttpServletRequest request){
		try {
		//获取到响应的对象 
		//获取到响应的输出流 用来将验证码的图片响应到客户端
		ServletOutputStream out = response.getOutputStream();
		//产生随机字符串 
		String imageCode = ImageUtil.getRundomStr();
		System.err.println(imageCode);
		//将随机字符串 存放到session域中 
		HttpSession session = request.getSession();
		session.setAttribute("imageCode", imageCode);
		//生成图片
		ImageUtil.getImage(imageCode, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客注册 
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("signRegister")
	@ResponseBody
	public boolean signRegister(String userName,String password){
		User user = new User();
		user.setUsername(userName);
		user.setPassword(password);
		user.setType("zuke");
		int count = userService.addZukeUser(user);
		return count==1?true:false;
	}
	
	/**
	 * 验证码检查
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("signcheckCode")
	@ResponseBody
	public Boolean signcheckCode(String vercode,HttpServletResponse response,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		String imageCode =(String)session.getAttribute("imageCode");
		System.err.println(imageCode);
		System.err.println(vercode);
		if(vercode.toLowerCase().equals(imageCode.toLowerCase())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 租客名称检查  
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("signcheckName")
	@ResponseBody
	public Boolean signcheckName(String userName){
		User user = userService.findByuserName(userName);
		if(user==null){
			return true;
		}else{
			return false;
		}
	}
	
	
	
}
