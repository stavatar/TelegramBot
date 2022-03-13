package com.example.TelegramBot.Service;

import com.example.TelegramBot.Model.AccountsDTO;
import com.example.TelegramBot.Model.UserDTO;
import com.example.TelegramBot.Repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private  AccountsRepository accountsRepository;

    public List<AccountsDTO> getByUser(UserDTO user){
        Optional<List<AccountsDTO>> accountsOpt=accountsRepository.getAccountsDTOSByUser(user);
        return accountsOpt.orElse(null);
    }

    public AccountsDTO getById(long Id){
        Optional<AccountsDTO> accounts=accountsRepository.findById(Id);
        return accounts.orElse(null);

    }
    public AccountsDTO save(AccountsDTO account){
        return accountsRepository.save(account);
    }

}
