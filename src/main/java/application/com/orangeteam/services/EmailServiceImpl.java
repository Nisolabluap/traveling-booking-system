package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.CustomerDTO;
import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.clients.Configuration;
import com.mailslurp.models.SendEmailOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    ApiClient defaultClient;
    UUID inboxId;
    @Autowired
    private TemplateEngine emailTemplateEngine;

    public EmailServiceImpl() {
        this.defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setApiKey("4657e79f8cf4ed42688b6babace42947370bb361ceebb5dec1780e13d0af5b45");
        this.inboxId = UUID.fromString("4e74bec5-9845-49b6-bc5b-c85d56ae8979");
    }


    private void sendEmail(String subject, String body, String eMailAddress) {
        InboxControllerApi inboxControllerApi = new InboxControllerApi(defaultClient);
        try {
            SendEmailOptions sendEmailOptions = new SendEmailOptions()
                    .isHTML(true)
                    .to(Collections.singletonList(eMailAddress))
                    .subject(subject)
                    .body(body);
            inboxControllerApi.sendTestEmail(inboxId);
            inboxControllerApi.sendEmail(inboxId, sendEmailOptions);

        } catch (ApiException apiException) {
            log.info("Cannot send email to ".concat(eMailAddress));
            log.info(apiException.toString());
        }
    }

    @Override
    public void sendWelcomeEmail(CustomerDTO customerDTO) {
        Context context = new Context();
        context.setVariable("firstName", customerDTO.getFirstName());
        context.setVariable("id", customerDTO.getId());
        String emailContent = emailTemplateEngine.process("welcome", context);
        sendEmail("Welcome to Travel Booking System", emailContent, customerDTO.getEmail());
    }

    @Override
    public void sendBookingConfirmationEmail(CustomerDTO customerDTO, BookingDTO bookingDTO) {
        Context context = new Context();
        context.setVariable("firstName", customerDTO.getFirstName());
//        context.setVariable("bookingId", BookingDTO.getId);
        String emailContent = emailTemplateEngine.process("booking-confirmation", context);
        sendEmail("Your booking is confirmed", emailContent, customerDTO.getEmail());
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
