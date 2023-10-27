package com.orangeteam.services;

import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.clients.Configuration;
import com.mailslurp.models.InboxDto;
import com.mailslurp.models.SendEmailOptions;
import com.orangeteam.models.dtos.BookingDTO;
import com.orangeteam.models.dtos.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    ApiClient defaultClient;
    UUID inboxId;

    public EmailServiceImpl() {
        this.defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setApiKey("4657e79f8cf4ed42688b6babace42947370bb361ceebb5dec1780e13d0af5b45");
        this.inboxId = UUID.fromString("4e74bec5-9845-49b6-bc5b-c85d56ae8979");
    }


    public void sendEmail(String subject, String body, String eMailAddress) {
        InboxControllerApi inboxControllerApi = new InboxControllerApi(defaultClient);
        try {

            SendEmailOptions sendEmailOptions = new SendEmailOptions()
                    .to(Collections.singletonList(eMailAddress))
                    .subject(subject)
                    .body(body);
            inboxControllerApi.sendTestEmail(inboxId);
            inboxControllerApi.sendEmail(inboxId, sendEmailOptions);

        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void sendWelcomeEmail(CustomerDTO customerDTO) {

    }

    @Override
    public void sendBookingConfirmationEmail(CustomerDTO customerDTO, BookingDTO bookingDTO) {

    }

    @Override
    public void sendPaymentReceiptEmail(CustomerDTO customerDTO) {

    }

    @Override
    public void sendPaymentFailedEmail(CustomerDTO customerDTO) {

    }

    @Override
    public void sendItineraryChangeEmail(CustomerDTO customerDTO, BookingDTO bookingDTO) {

    }
}
