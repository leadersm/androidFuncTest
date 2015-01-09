package com.example.email;


public class MailSender {
	
	public static void sendAuthReq() {
		// 设置邮件服务器信息
		MailInfo mailInfo = new MailInfo();
		mailInfo.setMailServerHost("smtp.sina.net");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);

		// 邮箱用户名
		mailInfo.setUserName("lvshimin@twsm.com.cn");
		// 邮箱密码
		mailInfo.setPassword("Leader.s.m");
		// 发件人邮箱
		mailInfo.setFromAddress("lvshimin@twsm.com.cn");
		// 收件人邮箱
		mailInfo.setToAddress(new String[]{"lvshimin@twsm.com.cn"});
		// 邮件标题
		mailInfo.setSubject("舆情PAD申请开通");
		// 邮件内容
		StringBuffer buffer = new StringBuffer();
		buffer.append("deviceId:");
		buffer.append("\nmac:");
		mailInfo.setContent(buffer.toString());

		// 发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		// 发送文体格式
		sms.sendTextMail(mailInfo);
		System.out.println("邮件发送完毕");
	}
	
}