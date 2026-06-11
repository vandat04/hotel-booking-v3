package hotel_booking.service;

import hotel_booking.entity.Booking;
import hotel_booking.repository.BookingRepository;
import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.SearchTerm;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import hotel_booking.entity.Payment;
import hotel_booking.repository.PaymentRepository;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailParserService {

    private static final Logger log = LoggerFactory.getLogger(EmailParserService.class);

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    // Cron job to run every 1 minute for faster updates
    @Scheduled(fixedRate = 60000)
    public void checkAndParseEmails() {
        log.info("Starting email parser scan...");
        if (mailUsername == null || mailUsername.isBlank() || mailPassword == null || mailPassword.isBlank()) {
            log.warn("Gmail username or password not configured. Skipping email parser scan.");
            return;
        }

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        try {
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", mailUsername, mailPassword);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE); // Read-Write to set SEEN flag after parsing

            // Search for all UNREAD emails
            SearchTerm unreadTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message[] messages = inbox.search(unreadTerm);

            log.info("Found {} unread emails in Gmail INBOX.", messages.length);

            for (Message message : messages) {
                try {
                    processMessage(message);
                } catch (Exception e) {
                    log.error("Error processing email message: {}", e.getMessage());
                }
            }

            inbox.close(true);
            store.close();
            log.info("Email parser scan completed.");

        } catch (Exception e) {
            log.error("Failed to connect to Gmail IMAP store: {}", e.getMessage());
        }
    }

    private void processMessage(Message message) throws Exception {
        String subject = message.getSubject();
        String from = message.getFrom()[0].toString().toLowerCase();

        log.info("Scanning unread email: Subject='{}', From='{}'", subject, from);

        String content = getEmailContent(message);
        if (content == null || content.isBlank()) {
            return;
        }

        boolean processed = false;

        // Check if Airbnb email
        if (from.contains("airbnb.com") || content.toLowerCase().contains("airbnb")) {
            parseAirbnbEmail(content);
            processed = true;
        } 
        // Check if Booking.com email
        else if (from.contains("booking.com") || content.toLowerCase().contains("booking.com")) {
            parseBookingComEmail(content);
            processed = true;
        }

        if (processed) {
            // Mark as SEEN so it doesn't scan again
            message.setFlag(Flags.Flag.SEEN, true);
        }
    }

    private String getEmailContent(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/html")) {
            return (String) part.getContent();
        }
        if (part.isMimeType("text/plain")) {
            return (String) part.getContent();
        }
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                String content = getEmailContent(multipart.getBodyPart(i));
                if (content != null) {
                    return content;
                }
            }
        }
        return null;
    }

    private void parseAirbnbEmail(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        String text = doc.text();

        // 1. Extract Reservation Code (Airbnb standard: HM + 8-10 alphanumeric characters)
        Pattern codePattern = Pattern.compile("HM[A-Z0-9]{8,10}");
        Matcher codeMatcher = codePattern.matcher(text);
        if (!codeMatcher.find()) {
            log.warn("Could not find Airbnb reservation code in email content.");
            return;
        }
        String reservationCode = codeMatcher.group();

        // 2. Extract Price (Looks for currency formats like $150 or 3.000.000đ or 3.000.000 VND)
        BigDecimal price = BigDecimal.ZERO;
        Pattern pricePattern = Pattern.compile("(?:\\$|đ|VND|VND\\s*)\\s*([0-9.,]+)|([0-9.,]+)\\s*(?:đ|VND|VND\\s*)");
        Matcher priceMatcher = pricePattern.matcher(text);
        if (priceMatcher.find()) {
            String val = priceMatcher.group(1) != null ? priceMatcher.group(1) : priceMatcher.group(2);
            val = val.replaceAll("[.,]", ""); // Clean currency delimiters
            try {
                price = new BigDecimal(val);
            } catch (Exception ignored) {}
        }

        // 3. Extract Guest Name (Pattern: Khách: <Name> or Guest: <Name>)
        String guestName = "OTA Guest (Airbnb)";
        Pattern guestPattern = Pattern.compile("(?:Khách|Guest|Khách hàng):\\s*([^\\n\\r|]+)");
        Matcher guestMatcher = guestPattern.matcher(text);
        if (guestMatcher.find()) {
            guestName = guestMatcher.group(1).trim();
        }

        // Find the matched reservation and enrich details
        Optional<Booking> bookingOpt = bookingRepository.findByBookingSourceAndUid("AIRBNB", reservationCode);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setCustomerName(guestName);
            booking.setTotalAmount(price);
            booking.setNotes(booking.getNotes() + "\nEnriched from Email. Code: " + reservationCode + ", Price: " + price);
            bookingRepository.save(booking);
            
            // Enrich transaction
            List<Payment> payments = paymentRepository.findByBookingId(booking.getId());
            if (!payments.isEmpty()) {
                Payment p = payments.get(0);
                p.setAmount(price);
                p.setNotes(p.getNotes() + "\nEnriched from Email. Code: " + reservationCode + ", Price: " + price);
                paymentRepository.save(p);
            } else {
                Payment p = Payment.builder()
                        .booking(booking)
                        .amount(price)
                        .paymentMethod("OTA")
                        .gatewayName("AIRBNB")
                        .paymentType("BOOKING")
                        .status("SUCCESS")
                        .transactionReference(reservationCode)
                        .paymentDate(LocalDateTime.now())
                        .notes("Created from Email parser enrichment. Code: " + reservationCode)
                        .build();
                paymentRepository.save(p);
            }
            
            log.info("Enriched Airbnb Booking ID={} with Guest='{}', Price={}", booking.getId(), guestName, price);
        } else {
            log.warn("Found email for Airbnb Code {} but no matching iCal Booking found in DB.", reservationCode);
        }
    }

    private void parseBookingComEmail(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        String text = doc.text();

        // 1. Extract Reservation Code (Booking.com standard: 10-digit number)
        Pattern codePattern = Pattern.compile("\\b[0-9]{10}\\b");
        Matcher codeMatcher = codePattern.matcher(text);
        if (!codeMatcher.find()) {
            log.warn("Could not find Booking.com reservation code in email content.");
            return;
        }
        String reservationCode = codeMatcher.group();

        // 2. Extract Price
        BigDecimal price = BigDecimal.ZERO;
        Pattern pricePattern = Pattern.compile("(?:\\$|đ|VND|VND\\s*)\\s*([0-9.,]+)|([0-9.,]+)\\s*(?:đ|VND|VND\\s*)");
        Matcher priceMatcher = pricePattern.matcher(text);
        if (priceMatcher.find()) {
            String val = priceMatcher.group(1) != null ? priceMatcher.group(1) : priceMatcher.group(2);
            val = val.replaceAll("[.,]", "");
            try {
                price = new BigDecimal(val);
            } catch (Exception ignored) {}
        }

        // 3. Extract Guest Name
        String guestName = "OTA Guest (Booking.com)";
        Pattern guestPattern = Pattern.compile("(?:Khách|Guest|Khách hàng):\\s*([^\\n\\r|]+)");
        Matcher guestMatcher = guestPattern.matcher(text);
        if (guestMatcher.find()) {
            guestName = guestMatcher.group(1).trim();
        }

        // Find the matched reservation and enrich details
        Optional<Booking> bookingOpt = bookingRepository.findByBookingSourceAndUid("BOOKING", reservationCode);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setCustomerName(guestName);
            booking.setTotalAmount(price);
            booking.setNotes(booking.getNotes() + "\nEnriched from Email. Code: " + reservationCode + ", Price: " + price);
            bookingRepository.save(booking);
            
            // Enrich transaction
            List<Payment> payments = paymentRepository.findByBookingId(booking.getId());
            if (!payments.isEmpty()) {
                Payment p = payments.get(0);
                p.setAmount(price);
                p.setNotes(p.getNotes() + "\nEnriched from Email. Code: " + reservationCode + ", Price: " + price);
                paymentRepository.save(p);
            } else {
                Payment p = Payment.builder()
                        .booking(booking)
                        .amount(price)
                        .paymentMethod("OTA")
                        .gatewayName("BOOKING")
                        .paymentType("BOOKING")
                        .status("SUCCESS")
                        .transactionReference(reservationCode)
                        .paymentDate(LocalDateTime.now())
                        .notes("Created from Email parser enrichment. Code: " + reservationCode)
                        .build();
                paymentRepository.save(p);
            }
            
            log.info("Enriched Booking.com Booking ID={} with Guest='{}', Price={}", booking.getId(), guestName, price);
        } else {
            log.warn("Found email for Booking.com Code {} but no matching iCal Booking found in DB.", reservationCode);
        }
    }
}
