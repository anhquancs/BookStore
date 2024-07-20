package com.bookstore.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.Utility;
import com.bookstore.entity.City;
import com.bookstore.entity.Customer;
import com.bookstore.sercurity.CustomerUserDetails;
import com.bookstore.sercurity.oauth.CustomerOAuth2User;
import com.bookstore.setting.EmailSettingBag;
import com.bookstore.setting.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private SettingService settingService;



    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        List<City> listCites = customerService.listAllCity();

        model.addAttribute("listCites", listCites);
        model.addAttribute("pageTitle", "Đăng kí tài khoản");
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model,
            HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);

        model.addAttribute("pageTitle", "Đăng ký thành công!");

        return "/register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer)
            throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Set UTF-8 encoding

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());

        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("to Address: " + toAddress);
        System.out.println("Verify URL: " + verifyURL);
    }

    @GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean verified = customerService.verify(code);
		
		return "register/" + (verified ? "verify_success"  : "verify_fail");
	}

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {
        String email = getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);
        List<City> listCites = customerService.listAllCity(); 

        model.addAttribute("listCites", listCites);
        model.addAttribute("customer", customer); 
        return "customer/account_form"; 
    }

    private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Object principal = request.getUserPrincipal(); 
        String customerEmail = null; 

        if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
           customerEmail = request.getUserPrincipal().getName();  
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) principal; 
            CustomerOAuth2User auth2User = (CustomerOAuth2User)auth2AuthenticationToken.getPrincipal(); 
            customerEmail = auth2User.getEmail();
        }   
        return customerEmail; 
    }

    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra, HttpServletRequest request) {
        customerService.update(customer);
        ra.addFlashAttribute("message", "Thông tin tài khoản đã được cập nhật. ");
        updateNameForAuthenticatedCustomer(customer, request);  
        return "redirect:/account_details";
    }

    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
        Object principal = request.getUserPrincipal(); 

        if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerDetailsObject(principal); 
            Customer authenticatedCustomer = userDetails.getCustomer(); 
            authenticatedCustomer.setLastName(customer.getLastName());
            authenticatedCustomer.setFirstName(customer.getFirstName());
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) principal; 
            CustomerOAuth2User auth2User = (CustomerOAuth2User)auth2AuthenticationToken.getPrincipal(); 
            String fullName = customer.getLastName() + " " + customer.getFirstName(); 
            auth2User.setFullName(fullName);;
        }   
    } 

    private CustomerUserDetails getCustomerDetailsObject(Object principal)  {
        CustomerUserDetails userDetails = null; 
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal; 
            userDetails = (CustomerUserDetails) token.getPrincipal(); 
        } else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal; 
            userDetails = (CustomerUserDetails) token.getPrincipal();
        }

        return userDetails; 
    }
}
