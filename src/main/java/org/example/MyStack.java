package org.example;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/** ### 8. Реализация стека на основе ArrayList
 Создайте класс MyStack<T>,
 который реализует базовые операции стека (push, pop, peek, isEmpty) используя ArrayList.**/

public class MyStack<T> {
    private List<T> elements;
    public MyStack() {
        elements = new ArrayList<>();
    }

    public void push(T element){
        elements.add(element);
    }

    public T pop(){
        if(elements.isEmpty()){
            throw new EmptyStackException();
        }

        return elements.remove(elements.size() - 1);
    }

    public T peek(){
        if(!elements.isEmpty()) {
            return elements.getLast();
        }

        throw new EmptyStackException();
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

}
