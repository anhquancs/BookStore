package com.bookstore.checkout;

import com.bookstore.checkout.model.response.InitiateTransactionResponse;
import com.bookstore.checkout.model.response.PaymentStatusResponse;
import com.bookstore.checkout.usecase.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentGateway paymentGateway;

    @PostMapping("/payments")
    public ResponseEntity<InitiateTransactionResponse> initiateTransaction(@RequestParam("amount") int orderTotal,
                                                                           @RequestParam("orderInfo") String orderInfo) {
        return ResponseEntity.ok(paymentGateway.initateTransaction(orderTotal, orderInfo));
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<PaymentStatusResponse> transactionStatus() {
        return ResponseEntity.ok(paymentGateway.paymentStatus());
    }


}
