package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.dto.AccountDTO;
import org.example.entity.AccountEntity;
import org.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private AccountRepository accountRepository;

    public AccountDTO uploadAccount(AccountEntity accountEntity) {
        Optional<AccountEntity> findAccount = accountRepository.findByUserId(accountEntity.getUserId());
        if(findAccount.isEmpty()) {
            AccountEntity result = accountRepository.save(accountEntity);
            return buildAccountDTOFromEntity(result);
        } else {
            return buildAccountDTOFromEntity(findAccount.get());
        }
    }

    @Transactional
    public AccountDTO putMoney(Long userId, Long amount) throws Exception {
        if (amount < 0) {
            throw new BadRequestException("replenishment of money cannot be negative");
        }
        Optional<AccountEntity> findAccount = accountRepository.findByUserId(userId);
        if (findAccount.isPresent()) {
            AccountEntity accountEntity = findAccount.get();
            accountEntity.setAccount(accountEntity.getAccount() + amount);
            return buildAccountDTOFromEntity(accountRepository.save(accountEntity));
        } else {
            throw new EntityNotFoundException("user undefined");
        }
    }

    @Transactional
    public AccountDTO writeOffMoney(Long userId, Long amount) throws Exception {
        if (amount < 0) {
            throw new BadRequestException("replenishment of money cannot be negative");
        }
        Optional<AccountEntity> findAccount = accountRepository.findByUserId(userId);
        if (findAccount.isPresent()) {
            AccountEntity accountEntity = findAccount.get();
            accountEntity.setAccount(accountEntity.getAccount() - amount);
            return buildAccountDTOFromEntity(accountRepository.save(accountEntity));
        } else {
            throw new EntityNotFoundException("user undefined");
        }
    }

    private AccountDTO buildAccountDTOFromEntity(AccountEntity accountEntity) {
        return new AccountDTO(accountEntity.getUserId(), accountEntity.getAccount());
    }
    //ну тут мб кафка будет
}
