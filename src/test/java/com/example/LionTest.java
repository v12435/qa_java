package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Тесты для класса Lion

class LionTest {

    @ParameterizedTest
    @CsvSource({
            "Самец, true",
            "Самка, false"
    })
    void lionHasCorrectManeValue(String sex, boolean expectedHasMane) throws Exception {
        // Создаём мок Feline
        Feline felineMock = mock(Feline.class);

        // Создаём объект Lion
        Lion lion = new Lion(sex, felineMock);

        // Проверяем наличие гривы
        assertEquals(expectedHasMane, lion.doesHaveMane());
    }

    @Test
    void lionThrowsExceptionForInvalidSex() {
        // Создаём мок Feline
        Feline felineMock = mock(Feline.class);

        // Проверяем, что выбрасывается исключение
        Exception exception = assertThrows(Exception.class, () -> {
            new Lion("Неизвестно", felineMock);
        });

        // Проверяем текст исключения
        assertEquals(
                "Используйте допустимые значения пола животного - самец или самка",
                exception.getMessage()
        );
    }

    @Test
    void getKittensReturnsValueFromFeline() throws Exception {
        // Создаём мок Feline
        Feline felineMock = mock(Feline.class);
        when(felineMock.getKittens()).thenReturn(3);

        Lion lion = new Lion("Самец", felineMock);

        int kittens = lion.getKittens();

        assertEquals(3, kittens);
    }

    @Test
    void getFoodReturnsValueFromFeline() throws Exception {
        // Создаём мок Feline
        Feline felineMock = mock(Feline.class);
        List<String> expectedFood = List.of("Мясо", "Птица");
        when(felineMock.getFood("Хищник")).thenReturn(expectedFood);

        Lion lion = new Lion("Самка", felineMock);

        List<String> food = lion.getFood();

        assertEquals(expectedFood, food);
    }
}
