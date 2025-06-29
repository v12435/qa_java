package com.example;

import java.util.List;

// Класс Alex — лев из Мадагаскара

public class Alex extends Lion {

    public Alex(Feline feline) throws Exception {
        // Алекс — всегда самец
        super("Самец", feline);
    }

    // У Алекса нет львят

    @Override
    public int getKittens() {
        return 0;
    }

    // Возвращает друзей Алекса

    public List<String> getFriends() {
        return List.of("Марти", "Глория", "Мелман");
    }

    // Возвращает место, где живёт Алекс

    public String getPlaceOfLiving() {
        return "Нью-Йоркский зоопарк";
    }
}
