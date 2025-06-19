package ru.hse.shop.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import ru.hse.shop.dto.AccountDTO;
import ru.hse.shop.dto.OrderDTO;

import java.util.List;

@Service
public class PaymentService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.payments-service.url}")
    private String paymentsService;

    public List<AccountDTO> getAllAccount() {
        ResponseEntity<List<AccountDTO>> ans = restTemplate.exchange(
            paymentsService + "/balance/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountDTO>>() {}
        );

        return ans.getBody();
    }

    public AccountDTO putMoney(Long id, Long amount) {
        return restTemplate.postForObject(
                paymentsService + "/balance/put/money/" + id,
                new HttpEntity<>(amount),
                AccountDTO.class
        );
    }

}
