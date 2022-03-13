package com.example.TelegramBot.Service;

import com.example.TelegramBot.Model.ServiceDTO;
import com.example.TelegramBot.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceDTO getByName(String name){
        Optional<ServiceDTO> service= serviceRepository.findByName(name);
        return service.orElse(null);
    }
    public List< ServiceDTO> getAll(){
        return serviceRepository.findAll();
    }
}
