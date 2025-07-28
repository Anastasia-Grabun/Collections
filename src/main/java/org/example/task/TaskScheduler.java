package org.example.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**### 12. Система управления задачами с приоритетами и зависимостями
 Реализуйте систему управления задачами,
 где каждая задача имеет приоритет и может зависеть от других задач.
 Система должна уметь планировать выполнение задач с учетом зависимостей и приоритетов.

 Дополнительные требования:
 Обнаружение циклических зависимостей
 Эффективная обработка больших объемов задач
 Возможность отмены задач и каскадная отмена зависимых задач
 Реализация timeout для задач
 Метрики производительности системы
 **/
public class TaskScheduler {

    private final Map<String, Task> tasks = new ConcurrentHashMap<>();

    private final PriorityQueue<Task> readyQueue = new PriorityQueue<>(
            Comparator.comparingInt((Task t) -> t.getPriority()).reversed()
    );

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final ScheduledExecutorService timeoutChecker = Executors.newSingleThreadScheduledExecutor();
    private final Map<TaskStatus, AtomicInteger> stats = new ConcurrentHashMap<>();

    public TaskScheduler() {
        Arrays.stream(TaskStatus.values()).forEach(s -> stats.put(s, new AtomicInteger(0)));
        timeoutChecker.scheduleAtFixedRate(this::checkTimeouts, 1, 1, TimeUnit.SECONDS);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        stats.get(TaskStatus.PENDING).incrementAndGet();
        if (task.getDependencies().isEmpty()) {
            readyQueue.add(task);
        }
    }

    public void markTaskCompleted(String taskId) {
        updateTaskStatus(taskId, TaskStatus.COMPLETED);
        processDependents(taskId);
    }

    public void markTaskFailed(String taskId) {
        updateTaskStatus(taskId, TaskStatus.FAILED);
        cascadeCancel(taskId);
    }

    public List<Task> getReadyTasks() {
        List<Task> readyTasks = new ArrayList<>();
        synchronized (readyQueue) {
            while (!readyQueue.isEmpty()) {
                readyTasks.add(readyQueue.poll());
            }
        }
        return readyTasks;
    }

    public boolean hasCyclicDependency() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String taskId : tasks.keySet()) {
            if (hasCycle(taskId, visited, recursionStack)) {
                return true;
            }
        }

        return false;
    }

    public List<String> getExecutionOrder() {
        List<String> executionOrder = new ArrayList<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Queue<Task> queue = new PriorityQueue<>(Comparator.comparingInt(t -> t.getPriority()));

        tasks.forEach((id, task) -> inDegree.put(id, task.getDependencies().size()));
        tasks.values().stream()
                .filter(t -> t.getDependencies().isEmpty())
                .forEach(queue::add);

        while (!queue.isEmpty()) {
            Task current = queue.poll();
            executionOrder.add(current.getId());

            tasks.values().stream()
                    .filter(t -> t.getDependencies().contains(current.getId()))
                    .forEach(t -> {
                        inDegree.compute(t.getId(), (k, v) -> v - 1);
                        if (inDegree.get(t.getId()) == 0) {
                            queue.add(t);
                        }
                    });
        }

        return executionOrder.size() == tasks.size() ? executionOrder : Collections.emptyList();
    }

    public Map<TaskStatus, Integer> getTaskStatistics() {
        return stats.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get()
                ));
    }

    private void updateTaskStatus(String taskId, TaskStatus status) {
        Task task = tasks.get(taskId);
        if (task != null) {
            stats.get(task.getStatus()).decrementAndGet();
            task.setStatus(status);
            stats.get(status).incrementAndGet();
        }
    }

    private void processDependents(String completedTaskId) {
        tasks.values().stream()
                .filter(t -> t.getDependencies().contains(completedTaskId))
                .filter(t -> t.getDependencies().stream().allMatch(depId -> {
                    Task dep = tasks.get(depId);
                    return dep != null && dep.getStatus() == TaskStatus.COMPLETED;
                }))
                .forEach(t -> {
                    synchronized (readyQueue) {
                        readyQueue.add(t);
                    }
                });
    }

    private void cascadeCancel(String taskId) {
        tasks.values().stream()
                .filter(t -> t.getDependencies().contains(taskId))
                .forEach(t -> {
                    updateTaskStatus(t.getId(), TaskStatus.FAILED);
                    cascadeCancel(t.getId());
                });
    }

    private void checkTimeouts() {
        long now = System.currentTimeMillis();
        tasks.values().stream()
                .filter(t -> t.getStatus() == TaskStatus.RUNNING)
                .filter(t -> now - t.createdAt > t.timeoutMs)
                .forEach(t -> updateTaskStatus(t.getId(), TaskStatus.PENDING));
    }

    private boolean hasCycle(String taskId, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(taskId)) return true;
        if (visited.contains(taskId)) return false;

        visited.add(taskId);
        recursionStack.add(taskId);

        Task task = tasks.get(taskId);
        if (task != null) {
            for (String depId : task.getDependencies()) {
                if (hasCycle(depId, visited, recursionStack)) {
                    return true;
                }
            }
        }

        recursionStack.remove(taskId);

        return false;
    }

}
