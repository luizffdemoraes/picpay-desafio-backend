package br.com.lffm.picpaydesafiobackend.notification;

import br.com.lffm.picpaydesafiobackend.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    private RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder
            .baseUrl("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc")
            .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-desafio-backend")
    public void receiveNotication(Transaction transaction) {
        LOGGER.info("notifying transaction {}...", transaction);

        var response = restClient.get()
            .retrieve()
            .toEntity(Notification.class);

        if (response.getStatusCode().isError() || response.getBody().message().isBlank()) {
            throw new NotficationException("Error sending notification!");
        }

        LOGGER.info("notification has been sent {}...", response.getBody());
    }
}
