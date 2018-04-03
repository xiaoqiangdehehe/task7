package com.wyq.taskSeven.controller;

import com.wyq.taskSeven.pojo.Student;
import com.wyq.taskSeven.service.BaseService;
import com.wyq.taskSeven.token.JwtToken;
import com.wyq.taskSeven.util.AliyumOSSUtil;
import com.wyq.taskSeven.verification.EmailVerification;
import com.wyq.taskSeven.verification.EmailVerificationUtil;
import com.wyq.taskSeven.verification.PhoneVerification;
import com.wyq.taskSeven.verification.PhoneVerificationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Controller
@RequestMapping("/authority")
public class BaseController {

    private static Logger logger = Logger.getLogger(BaseService.class);

    @Autowired
    private PhoneVerificationUtil phoneVerificationUtil;

    @Autowired
    private EmailVerificationUtil emailVerificationUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AliyumOSSUtil aliyumOSSUtil;

    @Autowired
    private BaseService studentService;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tiles_index");
        return modelAndView;
    }

    @RequestMapping("/sign_up_by_phone")
    public ModelAndView signUpByPhone() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tiles_sign_up_by_phone");
        return modelAndView;
    }

    @RequestMapping(value = "/sign_up_by_phone/send_verification_code", method = RequestMethod.POST)
    public ModelAndView sendPhoneVerificationCode(@ModelAttribute("phoneVerification") PhoneVerification phoneVerification) {
        ModelAndView modelAndView = new ModelAndView();
//        校验注册信息
        boolean flag = phoneVerificationUtil.sendVerificationCode(phoneVerification);
        if (flag) {
//            输入验证码的界面
            modelAndView.addObject("phoneVerification", phoneVerification);
            modelAndView.setViewName("tiles_sign_up_by_phone_commit");
        } else {
//            注册页面
            modelAndView.setViewName("redirect:/authority/sign_up_by_phone");
        }
        return modelAndView;
    }

    @RequestMapping("/sign_up_by_phone/checking")
    public ModelAndView phoneChecking(@RequestParam("phone") String phone, @RequestParam("verificationCodeClient") String verificationCodeClient,
                                      HttpServletResponse response) {
        Map<String, String> check = redisTemplate.opsForHash().entries(phone);
        ModelAndView modelAndView = new ModelAndView();
        if ((check.get("verificationCodeService")).equals(verificationCodeClient)) {//check验证码
            Student student = new Student();
            student.setStudentPhone(Long.valueOf(check.get("phone")));
            student.setStudentPassword(check.get("password"));
            studentService.addStudent(student);
            redisTemplate.delete(phone);
            String Token = JwtToken.createToken(student.getStudentId());
            Cookie cookie = new Cookie("Token", Token);
            cookie.setMaxAge(5 * 60);//秒
            cookie.setPath("/task_seven");
            response.addCookie(cookie);
            modelAndView.setViewName("redirect:/authority/u/personal_info");//跳转到用户信息页面
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/authority/sign_up_by_phone");
        return modelAndView;//跳转到注册页面
    }

    @RequestMapping("/sign_up_by_email")
    public ModelAndView signUpByEmail() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tiles_sign_up_by_email");
        return modelAndView;
    }

    @RequestMapping(value = "/sign_up_by_email/send_verification_code", method = RequestMethod.POST)
    public ModelAndView sendEmailVerificationCode(@ModelAttribute("emailVerification") EmailVerification emailVerification) throws Throwable {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(emailVerification.getEmail());
//        校验注册信息
        boolean flag = emailVerificationUtil.sendVerificationCode(emailVerification);
        if (flag) {
//            输入验证码的界面
            modelAndView.addObject("emailVerification", emailVerification);
            modelAndView.setViewName("tiles_sign_up_by_email_commit");
        } else {
//            注册页面
            modelAndView.setViewName("redirect:/authority/sign_up_by_email");
        }
        return modelAndView;
    }

    @RequestMapping("/sign_up_by_email/checking")
    public ModelAndView emailChecking(@RequestParam("email") String email, @RequestParam("verificationCodeClient") String verificationCodeClient,
                                      HttpServletResponse response) {
        Map<String, String> check = redisTemplate.opsForHash().entries(email);
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(check.get("email"));
        System.out.println(check.get("verificationCodeService"));
        System.out.println(verificationCodeClient);
        if ((check.get("verificationCodeService")).equals(verificationCodeClient)) {//check验证码
            Student student = new Student();
            student.setStudentEmail(check.get("email"));
            student.setStudentPassword(check.get("password"));
            studentService.addStudent(student);
            redisTemplate.delete(email);
            String Token = JwtToken.createToken(student.getStudentId());
            Cookie cookie = new Cookie("Token", Token);
            cookie.setMaxAge(5 * 60);//秒
            cookie.setPath("/task_seven");
            response.addCookie(cookie);
            modelAndView.setViewName("redirect:/authority/u/personal_info");//跳转到用户信息页面
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/authority/sign_up_by_email");
        return modelAndView;//跳转到注册页面
    }

    @RequestMapping("/sign_out")
    public ModelAndView signOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("token")) {
                c.setMaxAge(0);
                c.setValue(null);
                response.addCookie(c);
            }
        }
        return new ModelAndView("redirect:/school/index");//带着response跳转
    }

    @RequestMapping("/u/personal_info")
    public ModelAndView personalInfo(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView();
        Long studentId = null;
        Student student = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("Token")) {
                String token = c.getValue();
                studentId = Long.valueOf(JwtToken.getId(token));
                System.out.println("token" + studentId);
                student = studentService.selectByStudentId(studentId);
            }
        }
        modelAndView.addObject("student", student);
        modelAndView.setViewName("tiles_personal_info");
        return modelAndView;
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam(value = "studentPortrait", required = false) MultipartFile studentPortrait,
                         HttpServletRequest httpServletRequest) {
//        CommonsMultipartFile cf = (CommonsMultipartFile) studentPortrait;//此处file 是你的MultipartFile
//        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
//        File myfile = fi.getStoreLocation();//会在项目的根目录的临时文件夹下生成一个临时文件 *.tmp
//        aliyumOSSUtil.upload("firstUpload", myfile);
//        System.out.println(aliyumOSSUtil.download("firstUpload").toString());
        String studentId = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("Token")) {
                String token = c.getValue();
                studentId = JwtToken.getId(token);
            }
        }
        String fileName = studentPortrait.getOriginalFilename();
        String url = null;
        try {
            InputStream inputStream = studentPortrait.getInputStream();
            boolean flag = aliyumOSSUtil.upload(studentId, fileName, inputStream);
            logger.error("photo " + flag);
            url = aliyumOSSUtil.getUrl(studentId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
