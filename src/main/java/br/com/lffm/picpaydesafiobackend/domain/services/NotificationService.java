package br.com.lffm.picpaydesafiobackend.domain.services;


import br.com.lffm.picpaydesafiobackend.domain.dtos.NotificationDTO;
import br.com.lffm.picpaydesafiobackend.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

       ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6", notificationRequest, String.class);

       if(!notificationResponse.getStatusCode().equals(HttpStatus.OK)) {
           LOGGER.error("Erro ao enviar notificação");
           throw new Exception("Service de notificação está fora do ar.");
       }
    }
}
