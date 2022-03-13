package com.example.TelegramBot.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate dateCreate;
    private LocalDate datePaid;
    private Float sum;
    @OneToMany(mappedBy ="receiptDTO")
    private List<ColumnReceiptDTO> columns;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptDTO)) return false;
        ReceiptDTO that = (ReceiptDTO) o;
        return getId() == that.getId() &&
                Objects.equals(getDateCreate(), that.getDateCreate()) &&
                Objects.equals(getDatePaid(), that.getDatePaid()) &&
                Objects.equals(getSum(), that.getSum()) &&
                Objects.equals(getColumns(), that.getColumns());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateCreate(), getDatePaid(), getSum(), getColumns());
    }
}
