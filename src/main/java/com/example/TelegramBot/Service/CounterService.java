package com.example.TelegramBot.Service;

import com.example.TelegramBot.Model.CounterDTO;
import com.example.TelegramBot.Model.ServiceDTO;
import com.example.TelegramBot.Model.UserDTO;
import com.example.TelegramBot.Repository.CounterRepository;
import com.example.TelegramBot.Enums.StatusCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CounterService {
    @Autowired
    private CounterRepository counterRepository;


    public List<CounterDTO> getByUser(UserDTO user){
        Optional<List<CounterDTO>> counters=counterRepository.findByUser(user);
        return counters.orElse(null);
    }

    public CounterDTO getEditingCounter(UserDTO user){
        Optional<CounterDTO> counter= getByUser(user).stream().filter((counterDTO -> counterDTO.getStatus()== StatusCounter.WRITE)).findFirst();
        return counter.orElse(null);
    }
    public void delete(CounterDTO counterDTO){
        counterRepository.deleteById(counterDTO.getId());
    }
    public CounterDTO getByServiceAndServiceUserAndStatus(ServiceDTO service,UserDTO user,StatusCounter statusCounter){
        Optional<List<CounterDTO>> opt= counterRepository.findByUserAndServiceDTOAndStatus(user,service,statusCounter);
        if (opt.isPresent()){
            if(opt.get().size()==0)
                return null;
             else return  opt.get().get(0);
        }else return null;
    }
    public void save(CounterDTO counter){
        counterRepository.save(counter);
    }

    public CounterDTO getByLastDateServiceUser(UserDTO user, ServiceDTO service){
        List<CounterDTO> counters=counterRepository.findByUserAndServiceDTO(user,service).get().stream().filter(counterDTO -> counterDTO.getDate()!=null).collect(Collectors.toList());
        return counters.stream().min(Comparator.comparing(CounterDTO::getDate)).get();
    }
    public List<CounterDTO> getAllByUserAndStatus(UserDTO user, StatusCounter statusCounter){
        Optional<List<CounterDTO>> opt=counterRepository.findByUserAndStatus(user,statusCounter);
        return opt.orElse(null);
    }
    public CounterDTO getByUserAndStatus(UserDTO user, StatusCounter statusCounter){
         Optional<List<CounterDTO>> opt=counterRepository.findByUserAndStatus(user,statusCounter);
        if (opt.isPresent()){
            if(opt.get().size()==0)
                return null;
            else return  opt.get().get(0);
        }else return null;
    }
}
