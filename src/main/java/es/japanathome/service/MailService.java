package es.japanathome.service;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private MandrillApi mandrillApi;

    @Inject
    private MessageSource messageSource;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setHtml( content );
        message.setAutoText(true);
        message.setFromEmail("info@japanathome.es");
        message.setFromName("Japan at home!");

        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(to);
        recipients.add(recipient);
        message.setTo( recipients );

        try
        {
            log.info( "Sending mail!" );
            MandrillMessageStatus[] messageStatusReports = mandrillApi
                    .messages()
                    .send(message, false);
            log.debug("Sent e-mail to User '{}'!", to);

            for ( MandrillMessageStatus messageStatusReport : messageStatusReports)
            {
                log.info( "Mail status: " + messageStatusReport.getStatus() );
            }
        }
        catch (MandrillApiError e)
        {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
        catch (IOException e)
        {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(final String email, String content, Locale locale)
    {
        log.debug("Sending activation e-mail to '{}'", email);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(email, subject, content, false, true);
    }

    @Async
    public void sendOrderEmail(final String to, String subject, String content)
    {
        log.debug("Sending order e-mail to '{}'", content);
        sendEmail( to, subject, content, false, false );
    }
}
