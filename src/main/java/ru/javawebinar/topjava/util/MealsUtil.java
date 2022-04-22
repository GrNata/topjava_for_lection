package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {

    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//
//        mealsTo.stream()
//                .forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals,
                                                LocalTime startTime,
                                                LocalTime endTime,
                                                int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<MealTo> mealWithExcesses = new ArrayList<>();
        Map<LocalDate, Integer> map = new TreeMap<>();
        for (Meal meal : meals) {
            int count = map.get(meal.getDateTime().toLocalDate()) != null ?
                    map.get(meal.getDateTime().toLocalDate()) + meal.getCalories() : meal.getCalories();
            map.put(meal.getDateTime().toLocalDate(), count);
        }

        for (Meal meal : meals) {

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcesses.add(new MealTo(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        (map.get(meal.getDateTime().toLocalDate()) > 2000)));
            }
        }
        return mealWithExcesses;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate, Integer> map = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                        Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> createTo(meal, (map.get(meal.getDate()) > caloriesPerDay)))
                .collect(Collectors.toList());

    }


    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
               excess);
    }



}
