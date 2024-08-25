package com.bookstore.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bookstore.Utility;
import com.bookstore.address.AddressService;
import com.bookstore.checkout.usecase.PaymentGateway;
import com.bookstore.customer.CustomerService;
import com.bookstore.entity.Address;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.Customer;
import com.bookstore.entity.ShippingRate;
import com.bookstore.entity.order.Order;
import com.bookstore.entity.order.PaymentMethod;
import com.bookstore.order.OrderService;
import com.bookstore.setting.CurrencySettingBag;
import com.bookstore.setting.EmailSettingBag;
import com.bookstore.setting.SettingService;
import com.bookstore.shipping.ShippingRateService;
import com.bookstore.shoppingcart.ShoppingCartService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {

	@Autowired
	private PaymentGateway paymentGateway;

	@Autowired
	private CheckoutService checkoutService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ShippingRateService shipService;
	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SettingService settingService;

	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			model.addAttribute("shippingAddress", customer.toString());
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}

		if (shippingRate == null) {
			return "redirect:/cart";
		}

		List<CartItem> cartItems = cartService.listByCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);

		System.out.print("paymentTotal debug" + checkoutInfo.getPaymentTotal());

		return "checkout/checkout";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}

	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}

		List<CartItem> cartItems = cartService.listByCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

		Order createdOrder = null;
		String paymentUrl = null;

		if (paymentMethod == PaymentMethod.VNPAY) {
			// Tạo đơn hàng trước khi chuyển hướng đến VNPay
			createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
			paymentUrl = paymentGateway
					.initateTransaction(Math.round(checkoutInfo.getPaymentTotal()), createdOrder.getId().toString())
					.getPaymentUrl();

			cartService.deleteByCustomer(customer);
			sendOrderConfirmationEmail(request, createdOrder);
			// Chuyển hướng đến VNPay để thanh toán
			return "redirect:" + paymentUrl;
		} else {
			// Xử lý thanh toán bằng phương thức khác
			createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
			cartService.deleteByCustomer(customer);
			sendOrderConfirmationEmail(request, createdOrder);
			return "checkout/order_completed";
		}
	}

	private void sendOrderConfirmationEmail(HttpServletRequest request, Order order)
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");

		String toAddress = order.getCustomer().getEmail();
		String subject = emailSettings.getOrderConfirmationSubject();
		String content = emailSettings.getOrderConfirmationContent();

		subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
		String orderTime = dateFormatter.format(order.getOrderTime());

		CurrencySettingBag currencySettings = settingService.getCurrencySettings();
		String totalAmount = Utility.formatCurrency(order.getTotal(), currencySettings);

		content = content.replace("[[name]]", order.getCustomer().getFullName());
		content = content.replace("[[orderId]]", String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]", orderTime);
		content = content.replace("[[shippingAddress]]", order.getShippingAddress());
		content = content.replace("[[total]]", totalAmount);
		content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());

		helper.setText(content, true);
		mailSender.send(message);
	}
}