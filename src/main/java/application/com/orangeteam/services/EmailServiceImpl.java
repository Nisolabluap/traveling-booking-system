package application.com.orangeteam.services;

import application.com.orangeteam.models.dtos.BookingDTO;
import application.com.orangeteam.models.dtos.CustomerDTO;
import application.com.orangeteam.models.dtos.PaymentDTO;
import application.com.orangeteam.models.dtos.TravelPackageDTO;
import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.clients.Configuration;
import com.mailslurp.models.SendEmailOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    ApiClient defaultClient;
    UUID inboxId;
    @Autowired
    private TemplateEngine emailTemplateEngine;

    @Autowired
    public EmailServiceImpl(
            @Value("${mail.slurp.apiKey}") String mailSlurpApiKey,
            @Value("${mail.slurp.apiInbox}") String mailSlurpInboxId) {
        this.defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setApiKey(mailSlurpApiKey);
        this.inboxId = UUID.fromString(mailSlurpInboxId);
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
        context.setVariable("bookingId", bookingDTO.getId());
        String emailContent = emailTemplateEngine.process("booking-confirmation", context);
        sendEmail("Your booking is confirmed", emailContent, customerDTO.getEmail());
    }

    @Override
    public void sendPaymentReceiptEmail(CustomerDTO customerDTO, PaymentDTO paymentDTO) {
        Context context = new Context();
        context.setVariable("firstName", customerDTO.getFirstName());
        context.setVariable("bookingID", paymentDTO.getBookingID());
        context.setVariable("paymentID", paymentDTO.getId());
        String emailContent = emailTemplateEngine.process("payment-successful", context);
        sendEmail("Payment successful", emailContent, customerDTO.getEmail());
    }

    @Override
    public void sendPaymentFailedEmail(CustomerDTO customerDTO, PaymentDTO paymentDTO) {
        Context context = new Context();
        context.setVariable("firstName", customerDTO.getFirstName());
        context.setVariable("bookingID", paymentDTO.getBookingID());
        context.setVariable("paymentID", paymentDTO.getId());
        String emailContent = emailTemplateEngine.process("payment-failed", context);
        sendEmail("Payment failed", emailContent, customerDTO.getEmail());
    }

    @Override
    public void sendItineraryChangeEmail(CustomerDTO customerDTO, BookingDTO bookingDTO,
                                         TravelPackageDTO oldTravelPackageDTO, TravelPackageDTO updatedTravelPackage) {
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("oldStartDate", oldTravelPackageDTO.getStartingDate().toString());
        contextMap.put("oldEndDate", oldTravelPackageDTO.getEndingDate().toString());
        contextMap.put("oldDestination", oldTravelPackageDTO.getDestination());
        contextMap.put("updatedStartDate", updatedTravelPackage.getStartingDate().toString());
        contextMap.put("updatedEndDate", updatedTravelPackage.getEndingDate().toString());
        contextMap.put("updatedDestination", updatedTravelPackage.getDestination());
        contextMap.put("firstName", customerDTO.getFirstName());
        contextMap.put("bookingID", bookingDTO.getId());

        Context context = new Context();
        context.setVariables(contextMap);
        String emailContent = emailTemplateEngine.process("itinerary-change", context);
        sendEmail("Itinerary change for your booking", emailContent, customerDTO.getEmail());
    }
}
