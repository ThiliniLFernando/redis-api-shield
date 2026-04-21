package dev.thilinifernando.payment_service.controller;

import dev.thilinifernando.payment_service.model.PaymentRequest;
import dev.thilinifernando.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/request")
    public ResponseEntity<?> proceedPaymentRequest(@Valid @RequestBody PaymentRequest paymentRequest) {
        try{
            paymentService.proceedPaymentRequest(paymentRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // debugging purposes
            return ResponseEntity.badRequest().build();
        }
    }

}
