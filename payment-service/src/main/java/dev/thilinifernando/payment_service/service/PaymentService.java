package dev.thilinifernando.payment_service.service;

import dev.thilinifernando.payment_service.model.PaymentRequest;
import dev.thilinifernando.payment_service.util.ExternalService;
import dev.thilinifernando.payment_service.util.response.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final ExternalService externalService;

    @Autowired
    public PaymentService(ExternalService externalService) {
        this.externalService = externalService;
    }

    public void proceedPaymentRequest(PaymentRequest paymentRequest) {
        AccountResponse accountResponse = externalService.getAccountDetails(paymentRequest.userId());
        if (accountResponse.balance().compareTo(paymentRequest.amount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }else {
            // payment processing logic goes here

        }
    }

}
