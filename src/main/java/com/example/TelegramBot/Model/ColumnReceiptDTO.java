package com.example.TelegramBot.Model;

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

public class ColumnReceiptDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Float sum;
    private float volume;
    private float recalculation;
    @ManyToOne()
    @JoinColumn (name="service_id")
    private ServiceDTO serviceDTO;

    @ManyToOne()
    @JoinColumn (name="provider_id")
    private ProviderDTO providerDTO;

    @ManyToOne()
    @JoinColumn (name="receipt_id")
    private ReceiptDTO receiptDTO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnReceiptDTO that = (ColumnReceiptDTO) o;
        return id == that.id &&
                Float.compare(that.volume, volume) == 0 &&
                Float.compare(that.recalculation, recalculation) == 0 &&
                Objects.equals(sum, that.sum) &&
                Objects.equals(serviceDTO, that.serviceDTO) &&
                Objects.equals(providerDTO, that.providerDTO) &&
                Objects.equals(receiptDTO, that.receiptDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sum, volume, recalculation, serviceDTO, providerDTO, receiptDTO);
    }



}
