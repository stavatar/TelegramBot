package com.example.TelegramBot.Model;
import com.example.TelegramBot.Enums.StatusCounter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounterDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Integer data;
    private LocalDateTime date;
    @ManyToOne()
    @JoinColumn (name="user_id")
    private UserDTO user;
    @ManyToOne()
    @JoinColumn (name="service_id")
    private ServiceDTO serviceDTO;
    private StatusCounter status;
    private Integer number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterDTO that = (CounterDTO) o;
        boolean f1=id == that.id;
        boolean f2=Objects.equals(data, that.data);
        boolean f3=Objects.equals(date, that.date);
        boolean f4=Objects.equals(user, that.user);
        boolean f5=Objects.equals(serviceDTO, that.serviceDTO);
        boolean f6=status == that.status;
        return  f1&& f2 &&f3  && f4 &&f5  && f6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getData(), getDate(), getUser(), getServiceDTO(), getStatus());
    }
}
