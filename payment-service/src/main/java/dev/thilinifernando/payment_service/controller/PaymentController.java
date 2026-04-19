package dev.thilinifernando.payment_service.controller;

import dev.thilinifernando.payment_service.model.PaymentRequest;
import dev.thilinifernando.payment_service.service.PaymentService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public ResponseEntity<?> proceedPaymentRequest(@RequestBody PaymentRequest paymentRequest) {
        try{
            paymentService.proceedPaymentRequest(paymentRequest); // call service layer
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
