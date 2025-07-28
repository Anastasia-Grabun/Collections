package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static org.example.Tasks.areAnagrams;
import static org.example.Tasks.countFrequency;
import static org.example.Tasks.findIntersection;
import static org.example.Tasks.findSecondLargest;
import static org.example.Tasks.groupByGrade;
import static org.example.Tasks.mergeSortedLists;
import static org.example.Tasks.removeDuplicates;
import static org.example.Tasks.rotateLeft;

public class Main {
    public static void main(String[] args) {

        //task1
        List<Integer> numbers = Arrays.asList(1, 3, 2, 3, 4, 1, 5);
        System.out.println("Original list: " + numbers);
        System.out.println("Without duplicates: " + removeDuplicates(numbers));

        //task2
        List<String> words = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple");
        System.out.println("\nWord frequencies: " + countFrequency(words));

        //task3
        List<Integer> list1 = Arrays.asList(1, 3, 5, 7);
        List<Integer> list2 = Arrays.asList(2, 4, 6, 8);
        System.out.println("\nMerged sorted lists: " + mergeSortedLists(list1, list2));

        //task4
        Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        Set<String> set2 = new HashSet<>(Arrays.asList("c", "d", "e", "f"));
        System.out.println("\nSet intersection: " + findIntersection(set1, set2));

        //task5
        List<Student> students = Arrays.asList(
                new Student("Alice", 5),
                new Student("Bob", 4),
                new Student("Charlie", 5),
                new Student("David", 3)
        );
        System.out.println("\nStudents by grade: " + groupByGrade(students));

        //task8
        MyStack<Integer> stack = new MyStack<>();

        System.out.println("Testing MyStack:");
        System.out.println("Initial isEmpty(): " + stack.isEmpty());

        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("After pushing 10, 20, 30:");
        System.out.println("peek(): " + stack.peek());
        System.out.println("pop(): " + stack.pop());
        System.out.println("peek(): " + stack.peek());
        System.out.println("isEmpty(): " + stack.isEmpty());

        stack.push(40);
        System.out.println("After pushing 40:");
        System.out.println("peek(): " + stack.peek());

        System.out.println("pop(): " + stack.pop());
        System.out.println("pop(): " + stack.pop());
        System.out.println("Final isEmpty(): " + stack.isEmpty());

        try {
            System.out.println("Attempting pop on empty stack:");
            stack.pop();
        } catch (EmptyStackException e) {
            System.out.println("Caught EmptyStackException as expected");
        }

        //task6
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("Original list: " + list);
        rotateLeft(list, 2);
        System.out.println("After rotating left by 2: " + list);

        //task7
        System.out.println("\nAnagram check:");
        System.out.println("'listen' and 'silent': " + areAnagrams("listen", "silent"));
        System.out.println("'hello' and 'world': " + areAnagrams("hello", "world"));

        //task9
        List<Integer> numbers9 = Arrays.asList(5, 2, 10, 8, 3);
        System.out.println("\nSecond largest in " + numbers9 + ": " + findSecondLargest(numbers));

        //task10
        LRUCache<String, Integer> cache = new LRUCache<>(3);

        System.out.println("=== Test 1: Basic insertion ===");
        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);
        System.out.println("Cache content: " + cache.getCacheContent());

        System.out.println("\n=== Test 2: Capacity overflow ===");
        cache.put("D", 4);
        System.out.println("After adding D: " + cache.getCacheContent());

        System.out.println("\n=== Test 3: Access order change ===");
        cache.get("B");
        cache.put("E", 5);
        System.out.println("After accessing B and adding E: " + cache.getCacheContent());

        System.out.println("\n=== Test 4: Update existing key ===");
        cache.put("B", 22);
        cache.put("F", 6);
        System.out.println("After updating B and adding F: " + cache.getCacheContent());

        System.out.println("\n=== Test 5: Non-existent key ===");
        System.out.println("Get X: " + cache.get("X"));

        //task11
        EventCounter counter = new EventCounter();

        try {
            counter.startAutoCleanup(30, TimeUnit.SECONDS);

            counter.setDefaultTTL(60_000);
            counter.incrementEvent("user_login");
            counter.incrementEvent("page_view", 120_000);

            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("Current stats: " + counter.getAllCounts());
            }

        } catch (InterruptedException e) {
            System.out.println("The app was interrupted");
            Thread.currentThread().interrupt();
        } finally {
            counter.stop();
            System.out.println("Counter was interrupted. Final statistics:");
            System.out.println(counter.getAllCounts());
        }

    }

}