package com.wyq.taskSeven.verification;

import com.wyq.taskSeven.md5.MD5;
import com.wyq.taskSeven.util.AccountValidatorUtil;
import com.wyq.taskSeven.util.SendCloudUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailVerificationUtil {

    private static Logger logger = Logger.getLogger(EmailVerification.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SendCloudUtil sendCloudUtil;

    public boolean sendVerificationCode(EmailVerification emailVerification) throws Throwable {
//        String verificationCodeService = String.valueOf((int)((Math.random() * 9 + 1) * 100000));//六位数随机验证码
        String verificationCodeService = String.valueOf(111111);
        Map<String, String> map = pullMap(emailVerification, verificationCodeService);//将登陆信息放置到map中
        boolean flag = baseVerification(map);//验证登陆信息格式，邮箱等
        if (flag) {
            flag = sendCloudUtil.sendMail(map.get("email"), verificationCodeService);
        }
        if (flag) {
            map.remove("passwordAgain");//将发送成功的验证信息存到redis
            String md5Password = MD5.getResult(map.get("password"));
            map.put("password", md5Password);
            redisTemplate.opsForHash().putAll(map.get("email"), map);
        }
        logger.error("email " + flag );
        return flag;
    }

    private Map<String, String> pullMap(EmailVerification emailVerification, String verificationCodeService) {

        Map<String, String> map = new HashMap<String, String>();
        String phone = emailVerification.getEmail();
        String password = emailVerification.getPassword();
        String passwordAgain = emailVerification.getPasswordAgain();

        map.put("email", phone);
        map.put("password", password);
        map.put("passwordAgain", passwordAgain);
        map.put("verificationCodeService", verificationCodeService);
        return map;
    }

    private boolean baseVerification(Map<String, String> map) {
        if (!AccountValidatorUtil.isEmail(map.get("email"))) {
            return false;
        }
        if (!AccountValidatorUtil.isPassword(map.get("password"))) {
            return false;
        }
        if (!(map.get("password").equals(map.get("passwordAgain")))) {
            return false;
        }
        return true;
    }
}
