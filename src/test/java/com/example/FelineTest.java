package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Тестовый класс для проверки методов класса Feline

class FelineTest {

    @Test
    void eatMeatReturnsPredatorFood() throws Exception {
        // Создаём spy-объект Feline
        Feline feline = spy(Feline.class);

        // Ожидаемый результат работы метода getFood()
        List<String> expectedFood = List.of("Животные", "Птицы", "Рыба");

        // Подменяем вызов метода getFood("Хищник"), чтобы он возвращал наш список
        doReturn(expectedFood).when(feline).getFood("Хищник");

        // Вызываем тестируемый метод
        List<String> actualFood = feline.eatMeat();

        // Проверяем, совпадает ли результат с ожидаемым
        assertEquals(expectedFood, actualFood);
    }

    @Test
    void getFamilyReturnsFelineFamily() {
        // Создаём объект Feline
        Feline feline = new Feline();

        // Вызываем метод
        String family = feline.getFamily();

        // Проверяем, что возвращается строка "Кошачьи"
        assertEquals("Кошачьи", family);
    }

    @Test
    void getKittensWithoutArgumentReturnsOne() {
        // Создаём объект Feline
        Feline feline = new Feline();

        // Вызываем метод без параметров
        int kittens = feline.getKittens();

        // Проверяем, что возвращается 1
        assertEquals(1, kittens);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 100})
    void getKittensWithArgumentReturnsCorrectNumber(int kittensCount) {
        // Создаём объект Feline
        Feline feline = new Feline();

        // Вызываем метод с параметром
        int result = feline.getKittens(kittensCount);

        // Проверяем, что возвращается переданное значение
        assertEquals(kittensCount, result);
    }
}
