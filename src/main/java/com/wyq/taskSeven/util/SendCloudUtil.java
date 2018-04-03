package com.wyq.taskSeven.util;

import com.sendcloud.sdk.builder.SendCloudBuilder;
import com.sendcloud.sdk.core.SendCloud;
import com.sendcloud.sdk.model.MailAddressReceiver;
import com.sendcloud.sdk.model.MailBody;
import com.sendcloud.sdk.model.SendCloudMail;
import com.sendcloud.sdk.model.TextContent;
import com.sendcloud.sdk.util.ResponseData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendCloudUtil {
    private static Logger logger = Logger.getLogger(SendCloudUtil.class);

    @Autowired
    private MailBody mailBody;

    public boolean sendMail(String address, String text) throws Throwable {
        MailAddressReceiver receiver = new MailAddressReceiver();
        receiver.addTo(address);// 添加收件人
//        receiver.addTo("1191661688@qq.com");
        // 添加抄送
//        receiver.addCc("c@ifaxin.com");
//        // 添加密送
//        receiver.addBcc("d@ifaxin.com");

//        MailBody body = new MailBody();
//        // 设置 From
//        body.setFrom("sendcloud@sendcloud.org");
//        // 设置 FromName
//        body.setFromName("SendCloud");
//        // 设置 ReplyTo
//        body.setReplyTo("reply@sendcloud.org");
//        // 设置标题
//        body.setSubject("来自 SendCloud SDK 的邮件");
        // 创建文件附件
//        body.addAttachments(new File("D:/1.png"));
//        body.addAttachments(new File("D:/2.png"));
        //// 创建流附件
        // body.addAttachments(new FileInputStream(new File("D:/ff.png")));
        System.out.println("发送邮件");
        TextContent content = new TextContent();
        content.setContent_type(TextContent.ScContentType.html);
        content.setText("<html><p>"+ text +"</p></html>");

        SendCloudMail mail = new SendCloudMail();
        mail.setTo(receiver);
        mail.setBody(mailBody);
        mail.setContent(content);

        SendCloud sc = SendCloudBuilder.build();
        ResponseData res = sc.sendMail(mail);

        logger.info("邮件发送状态：" + res.getResult() + res.getStatusCode() + res.getMessage() + res.getInfo());

        return res.getResult();
    }
}
