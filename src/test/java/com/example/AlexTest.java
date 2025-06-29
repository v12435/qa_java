package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Тесты для класса Alex

class AlexTest {

    @Test
    void getKittensReturnsZero() throws Exception {
        // Создаём мок Feline
        Feline felineMock = mock(Feline.class);

        Alex alex = new Alex(felineMock);

        int kittens = alex.getKittens();

        assertEquals(0, kittens);
    }

    @Test
    void getFriendsReturnsCorrectList() throws Exception {
        Feline felineMock = mock(Feline.class);
        Alex alex = new Alex(felineMock);

        List<String> friends = alex.getFriends();

        List<String> expected = List.of("Марти", "Глория", "Мелман");

        assertEquals(expected, friends);
    }

    @Test
    void getPlaceOfLivingReturnsZoo() throws Exception {
        Feline felineMock = mock(Feline.class);
        Alex alex = new Alex(felineMock);

        String place = alex.getPlaceOfLiving();

        assertEquals("Нью-Йоркский зоопарк", place);
    }

    @Test
    void alexIsAlwaysMaleAndHasMane() throws Exception {
        Feline felineMock = mock(Feline.class);

        Alex alex = new Alex(felineMock);

        // Проверяем, что Алекс — самец и у него есть грива
        assertTrue(alex.doesHaveMane());
    }
}
