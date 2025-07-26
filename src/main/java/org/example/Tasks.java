package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tasks {

    /** task1
     * Напишите метод, который принимает ArrayList<Integer> и
     * возвращает новый список без дубликатов,
     * сохраняя порядок первого появления элементов. **/
    public static List<Integer> removeDuplicates(List<Integer> list) {
        return new ArrayList<>(new LinkedHashSet<>(list));
    }

    /** task 2
     * Подсчет частоты элементов
     * Создайте метод, который принимает список строк и возвращает
     * Map<String, Integer> с количеством вхождений каждой строки.
     */
    public static Map<String, Integer> countFrequency(List<String> words) {
        Map<String, Integer> result = new HashMap<>();
        for (String word : words) {
            result.merge(word, 1, Integer::sum);
        }

        return result;
    }

    /**task3
     *  Объединение двух отсортированных списков
     *  Реализуйте метод для слияния двух отсортированных списков в один
     *  отсортированный список без использования Collections.sort().
     *  **/

    //TODO:
    public static List<Integer> mergeSortedLists(List<Integer> list1, List<Integer> list2) {
       List<Integer> result = new ArrayList<>();
    }

    /**### 4. Поиск пересечения множеств
    Напишите метод, который находит общие элементы в
    двух множествах и возвращает их в виде нового множества. **/
    public static Set<String> findIntersection(Set<String> set1, Set<String> set2) {
        Set<String> result = new HashSet<>(set1);
        result.retainAll(set2);

        return result;
    }

}
