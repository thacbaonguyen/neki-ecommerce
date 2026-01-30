package com.thacbao.neki.services;

public interface EmailService {

    void sendOtpEmail(String to, String otp);

    void sendPasswordResetEmail(String to, String otp);

    void sendWelcomeEmail(String to, String fullName);

    void sendPasswordChangedEmail(String to, String fullName);

    void sendAccountBlockedEmail(String to, String fullName);

    void sendOrderConfirmationEmail(String to, String orderNumber, String totalAmount);
}