package org.example;

import java.util.ArrayList;
import java.util.Collections;
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

   /** ### 6. Поворот списка
    Реализуйте метод, который поворачивает
    список на k позиций влево. Например, [1,2,3,4,5] с k=2 становится [3,4,5,1,2].
    **/
    public static void rotateLeft(List<Integer> list, int k) {
        if(list.isEmpty() || k == 0){
            return;
        }

        k = k % list.size();
        Collections.reverse(list.subList(0, k));
        Collections.reverse(list.subList(k, list.size()));
        Collections.reverse(list);
    }



/** ### 7. Проверка на анаграммы
    Напишите метод, который проверяет, являются ли две строки анаграммами,
    используя Map для подсчета символов.
 **/
    public static boolean areAnagrams(String str1, String str2) {
        str1.toLowerCase();
        str2.toLowerCase();

        if(str1.length() != str2.length()){
            return false;
        }

        return countLetters(str1).equals(countLetters(str2));
    }

    private static Map<Character, Integer> countLetters(String s){
        Map<Character, Integer> map = new HashMap<>();

        for(char c : s.toCharArray()){
            if(c != ' '){
                map.put(c, map.getOrDefault(c, 0) + 1);
            }
        }

        return map;
    }


/**
### 9. Поиск второго по величине элемента
    Напишите метод, который находит второй по величине элемент в списке целых чисел без сортировки.
**/
    public static Integer findSecondLargest(List<Integer> numbers) {
        if(numbers == null || numbers.size() < 2){
            return null;
        }

        int max1 = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;

        for(int num : numbers){
            if(num > max1){
                max2 = max1;
                max1 = num;
            } else if(num > max2 && num != max1){
                max2 = num;
            }
        }

        return max2 == Integer.MIN_VALUE ? null : max2;
    }

}
