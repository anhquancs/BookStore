package com.bookstore.checkout;

import com.bookstore.checkout.model.response.InitiateTransactionResponse;
import com.bookstore.checkout.model.response.PaymentStatusResponse;
import com.bookstore.checkout.usecase.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentGateway paymentGateway;

//    @PostMapping("/payments")
//    public String initiateTransaction(@RequestParam("amount") int orderTotal,
//                                      @RequestParam("orderInfo") String orderInfo) {

    @PostMapping("/payments")
    public String initiateTransaction() {
        return "redirect:" + paymentGateway.initateTransaction(500000, "Thanh toan qua VNPAY").getPaymentUrl();
    }

    @GetMapping("/vnpay-payment")
    public String transactionStatus() {
        return paymentGateway.paymentStatus().getStatus().toString();
    }


}
