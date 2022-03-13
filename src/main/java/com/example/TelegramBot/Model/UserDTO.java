package com.example.TelegramBot.Model;

import com.example.TelegramBot.Enums.StatusPaid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String login;
    private String password;
    private Integer sizeRoom;
    private StatusPaid Status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn (name= "user_id")
    private List<ReceiptDTO> receiptDTOList;

    @OneToMany(mappedBy ="user",cascade = CascadeType.ALL)
    private List<AccountsDTO> account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id &&
                Objects.equals(name, userDTO.name) &&
                Objects.equals(login, userDTO.login) &&
                Objects.equals(password, userDTO.password) &&
                Status == userDTO.Status &&
                receiptDTOList.containsAll(userDTO.receiptDTOList) && userDTO.receiptDTOList.containsAll(receiptDTOList)&&
                Objects.equals(account, userDTO.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLogin(), getPassword(), getStatus(), getReceiptDTOList(), getAccount());
    }
}
