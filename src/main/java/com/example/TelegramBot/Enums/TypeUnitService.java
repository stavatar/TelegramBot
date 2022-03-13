package com.example.TelegramBot.Enums;

public enum TypeUnitService {
    RUB_KILOVAT("руб/киловат"),RUB_LITR("руб/литр"),RUB_KVM("руб/м.кв.");
    private final String name;

    TypeUnitService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
