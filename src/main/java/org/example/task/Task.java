package org.example.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Task {

    private final String id;
    private final int priority;
    private final Set<String> dependencies;
    private TaskStatus status;
    long timeoutMs;
    long createdAt;

}
