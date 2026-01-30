package com.thacbao.neki.services.impl;

import com.thacbao.neki.exceptions.user.EmailSenderException;
import com.thacbao.neki.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:NEKI E-Commerce}")
    private String appName;

    @Value("${app.url:http://localhost:3000}")
    private String appUrl;

    @Override
    @Async
    public void sendOtpEmail(String to, String otp) {
        try {
            String subject = "Xác thực tài khoản - " + appName;
            String content = buildOtpEmailTemplate(otp, "xác thực tài khoản");

            sendHtmlEmail(to, subject, content);
            log.info("OTP email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new EmailSenderException(e);
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String to, String otp) {
        try {
            String subject = "Đặt lại mật khẩu - " + appName;
            String content = buildOtpEmailTemplate(otp, "đặt lại mật khẩu");

            sendHtmlEmail(to, subject, content);
            log.info("Password reset email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", to, e);
            throw new EmailSenderException(e);
        }
    }

    @Override
    @Async
    public void sendWelcomeEmail(String to, String fullName) {
        try {
            String subject = "Chào mừng đến với " + appName;
            String content = buildWelcomeEmailTemplate(fullName);

            sendHtmlEmail(to, subject, content);
            log.info("Welcome email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", to, e);
            // Don't throw exception for welcome email
        }
    }

    @Override
    @Async
    public void sendPasswordChangedEmail(String to, String fullName) {
        try {
            String subject = "Mật khẩu đã được thay đổi - " + appName;
            String content = buildPasswordChangedEmailTemplate(fullName);

            sendHtmlEmail(to, subject, content);
            log.info("Password changed email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password changed email to: {}", to, e);
        }
    }

    @Override
    @Async
    public void sendAccountBlockedEmail(String to, String fullName) {
        try {
            String subject = "Tài khoản của bạn đã bị khóa - " + appName;
            String content = buildAccountBlockedEmailTemplate(fullName);

            sendHtmlEmail(to, subject, content);
            log.info("Account blocked email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send account blocked email to: {}", to, e);
        }
    }

    @Override
    @Async
    public void sendOrderConfirmationEmail(String to, String orderNumber, String totalAmount) {
        try {
            String subject = "Xác nhận đơn hàng #" + orderNumber + " - " + appName;
            String content = buildOrderConfirmationEmailTemplate(orderNumber, totalAmount);

            sendHtmlEmail(to, subject, content);
            log.info("Order confirmation email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send order confirmation email to: {}", to, e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    private String buildOtpEmailTemplate(String otp, String purpose) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                        .otp-box { background: white; border: 2px dashed #667eea; border-radius: 8px; padding: 20px; text-align: center; margin: 20px 0; }
                        .otp-code { font-size: 32px; font-weight: bold; color: #667eea; letter-spacing: 8px; }
                        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                        .warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 12px; margin: 20px 0; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>%s</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào,</p>
                            <p>Bạn đã yêu cầu %s. Vui lòng sử dụng mã OTP bên dưới:</p>
                            
                            <div class="otp-box">
                                <div class="otp-code">%s</div>
                            </div>
                            
                            <div class="warning">
                                <strong>Lưu ý:</strong> Mã OTP này có hiệu lực trong 10 phút. 
                                Không chia sẻ mã này với bất kỳ ai!
                            </div>
                            
                            <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>
                            
                            <p style="margin-top: 30px;">
                                Trân trọng,<br>
                                <strong>%s Team</strong>
                            </p>
                        </div>
                        <div class="footer">
                            <p>© %d %s. All rights reserved.</p>
                            <p>Email này được gửi tự động, vui lòng không trả lời.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(appName, purpose, otp, appName, LocalDateTime.now().getYear(), appName);
    }

    private String buildWelcomeEmailTemplate(String fullName) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 40px; text-align: center; border-radius: 10px 10px 0 0; }
                        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                        .button { display: inline-block; background: #667eea; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                        .features { background: white; border-radius: 8px; padding: 20px; margin: 20px 0; }
                        .feature-item { padding: 10px 0; border-bottom: 1px solid #eee; }
                        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>chào mừng đến với %s!</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Cảm ơn bạn đã đăng ký tài khoản tại %s. Chúng tôi rất vui khi có bạn!</p>
                            
                            <div class="features">
                                <h3>✨ Với tài khoản của bạn, bạn có thể:</h3>
                                <div class="feature-item">Mua sắm hàng nghìn sản phẩm thời trang</div>
                                <div class="feature-item"> Lưu sản phẩm yêu thích</div>
                                <div class="feature-item">Theo dõi đơn hàng dễ dàng</div>
                                <div class="feature-item">Nhận ưu đãi độc quyền</div>
                                <div class="feature-item">Đánh giá và review sản phẩm</div>
                            </div>
                            
                            <div style="text-align: center;">
                                <a href="%s" class="button">Bắt đầu mua sắm</a>
                            </div>
                            
                            <p style="margin-top: 30px;">
                                Nếu bạn có bất kỳ câu hỏi nào, đừng ngần ngại liên hệ với chúng tôi!
                            </p>
                            
                            <p>
                                Trân trọng,<br>
                                <strong>%s Team</strong>
                            </p>
                        </div>
                        <div class="footer">
                            <p>© %d %s. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(appName, fullName, appName, appUrl, appName, LocalDateTime.now().getYear(), appName);
    }

    private String buildPasswordChangedEmailTemplate(String fullName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: #28a745; color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                        .alert { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }
                        .info-box { background: white; border-radius: 8px; padding: 15px; margin: 20px 0; }
                        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🔐 Mật khẩu đã được thay đổi</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Mật khẩu tài khoản của bạn đã được thay đổi thành công.</p>
                            
                            <div class="info-box">
                                <strong>Thông tin thay đổi:</strong><br>
                                Thời gian: %s<br>
                            </div>
                            
                            <div class="alert">
                                <strong>Bạn không thực hiện thao tác này?</strong><br>
                                Vui lòng liên hệ với chúng tôi ngay lập tức để bảo vệ tài khoản của bạn.
                            </div>
                            
                            <p style="margin-top: 30px;">
                                Trân trọng,<br>
                                <strong>%s Team</strong>
                            </p>
                        </div>
                        <div class="footer">
                            <p>© %d %s. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(fullName, timestamp, appName, LocalDateTime.now().getYear(), appName);
    }

    private String buildAccountBlockedEmailTemplate(String fullName) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: #dc3545; color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                        .warning { background: #f8d7da; border-left: 4px solid #dc3545; padding: 15px; margin: 20px 0; }
                        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🚫 Tài khoản đã bị khóa</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào <strong>%s</strong>,</p>
                            
                            <div class="warning">
                                <strong>Tài khoản của bạn đã bị khóa</strong><br>
                                Tài khoản của bạn đã bị khóa bởi quản trị viên do vi phạm điều khoản sử dụng.
                            </div>
                            
                            <p>Nếu bạn cho rằng đây là một nhầm lẫn, vui lòng liên hệ với bộ phận hỗ trợ của chúng tôi.</p>
                            
                            <p style="margin-top: 30px;">
                                Trân trọng,<br>
                                <strong>%s Team</strong>
                            </p>
                        </div>
                        <div class="footer">
                            <p>© %d %s. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(fullName, appName, LocalDateTime.now().getYear(), appName);
    }

    private String buildOrderConfirmationEmailTemplate(String orderNumber, String totalAmount) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                        .order-box { background: white; border-radius: 8px; padding: 20px; margin: 20px 0; }
                        .button { display: inline-block; background: #667eea; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                        .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Đơn hàng đã được xác nhận!</h1>
                        </div>
                        <div class="content">
                            <p>Cảm ơn bạn đã mua hàng tại %s!</p>
                            
                            <div class="order-box">
                                <h3>Thông tin đơn hàng:</h3>
                                <p><strong>Mã đơn hàng:</strong> #%s</p>
                                <p><strong>Tổng tiền:</strong> %s VNĐ</p>
                                <p><strong>Trạng thái:</strong> Đang xử lý</p>
                            </div>
                            
                            <p>Chúng tôi đang xử lý đơn hàng của bạn và sẽ giao hàng trong thời gian sớm nhất.</p>
                            
                            <div style="text-align: center;">
                                <a href="%s/orders/%s" class="button">Xem chi tiết đơn hàng</a>
                            </div>
                            
                            <p style="margin-top: 30px;">
                                Trân trọng,<br>
                                <strong>%s Team</strong>
                            </p>
                        </div>
                        <div class="footer">
                            <p>© %d %s. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(appName, orderNumber, totalAmount, appUrl, orderNumber, appName,
                LocalDateTime.now().getYear(), appName);
    }
}