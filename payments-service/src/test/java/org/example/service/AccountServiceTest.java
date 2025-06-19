package org.example.service;

import org.example.dto.AccountDTO;
import org.example.entity.AccountEntity;
import org.example.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadAccount_New() {
        AccountEntity entity = AccountEntity.builder().userId(1L).account(0L).build();
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(entity);

        AccountDTO result = accountService.uploadAccount(entity);
        assertNotNull(result);
        assertEquals(1L, result.userId());
        assertEquals(0L, result.account());
    }

    @Test
    void testUploadAccount_Existing() {
        AccountEntity entity = AccountEntity.builder().userId(1L).account(100L).build();
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(entity));

        AccountDTO result = accountService.uploadAccount(entity);
        assertNotNull(result);
        assertEquals(1L, result.userId());
        assertEquals(100L, result.account());
    }

    @Test
    void testPutMoney() throws Exception {
        AccountEntity entity = AccountEntity.builder().userId(1L).account(100L).build();
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(entity));
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(entity);

        AccountDTO result = accountService.putMoney(1L, 50L);
        assertNotNull(result);
        assertEquals(1L, result.userId());
        assertEquals(150L, result.account());
    }

    @Test
    void testPutMoney_NegativeAmount() {
        Exception exception = assertThrows(Exception.class, () -> accountService.putMoney(1L, -10L));
        assertTrue(exception.getMessage().contains("cannot be negative"));
    }

    @Test
    void testPutMoney_UserNotFound() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> accountService.putMoney(1L, 10L));
        assertTrue(exception.getMessage().contains("user undefined"));
    }

    @Test
    void testWriteOffMoney() throws Exception {
        AccountEntity entity = AccountEntity.builder().userId(1L).account(100L).build();
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(entity));
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(entity);

        AccountDTO result = accountService.writeOffMoney(1L, 50L);
        assertNotNull(result);
        assertEquals(1L, result.userId());
        assertEquals(50L, result.account());
    }

    @Test
    void testWriteOffMoney_NegativeAmount() {
        Exception exception = assertThrows(Exception.class, () -> accountService.writeOffMoney(1L, -10L));
        assertTrue(exception.getMessage().contains("cannot be negative"));
    }

    @Test
    void testWriteOffMoney_UserNotFound() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> accountService.writeOffMoney(1L, 10L));
        assertTrue(exception.getMessage().contains("user undefined"));
    }
} 