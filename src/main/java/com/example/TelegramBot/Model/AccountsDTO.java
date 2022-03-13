package com.example.TelegramBot.Model;
import com.example.TelegramBot.Enums.State;
import com.example.TelegramBot.Enums.StateAuth;
import com.example.TelegramBot.Enums.TypeProgram;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDTO {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private TypeProgram type;
    private State state;
    @ManyToOne(cascade = CascadeType.ALL,optional = true)
    @JoinColumn (name="user_id")
    private UserDTO user;

    private StateAuth  stateAuth;
    private String login;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountsDTO)) return false;
        AccountsDTO that = (AccountsDTO) o;
        return getId() == that.getId() &&
                getType() == that.getType() &&
                getState() == that.getState() &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getLogin(), that.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getState(), getUser(), getLogin());
    }
}
