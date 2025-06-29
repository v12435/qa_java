package com.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Исправленный тестовый класс для проверки методов класса Cat

class CatTest {

    @Test
    void getSoundReturnsMeow() {
        // Создаём мок Feline
        Feline felineMock = mock(Feline.class);

        // Создаём объект Cat с моком
        Cat cat = new Cat(felineMock);

        // Проверяем звук кота
        String sound = cat.getSound();
        assertEquals("Мяу", sound);
    }

    @Test
    void getFoodReturnsPredatorFood() throws Exception {
        // Создаём мок Feline
        Feline felineMock = mock(Feline.class);

        // Ожидаемый список еды
        List<String> expectedFood = List.of("Мясо", "Птица", "Рыба");

        // Настраиваем мок, чтобы метод eatMeat возвращал наш список
        when(felineMock.eatMeat()).thenReturn(expectedFood);

        // Создаём Cat с замоканным Feline
        Cat cat = new Cat(felineMock);

        // Вызываем метод getFood()
        List<String> actualFood = cat.getFood();

        // Проверяем результат
        assertEquals(expectedFood, actualFood);
    }
}
