package de.uniks.stp24.util;

import org.jetbrains.annotations.NotNull;

public class ComparableWrapper<T, Integer> implements Comparable<T> {
    private T value;
    private Integer priority;
    public ComparableWrapper(T value, Integer priority) {
        this.value = value;
        this.priority = priority;
    }
    public T getValue() {
        return value;
    }
    public Integer getPriority() {
        return priority;
    }
    public int compareTo(@NotNull ComparableWrapper<T, Integer> o) {
        return (int) this.getPriority() - (int) o.getPriority();
    }
    @Override
    public int compareTo(@NotNull Object o) {
        if(o instanceof ComparableWrapper) {
            return this.compareTo((ComparableWrapper<T, Integer>) o);
        }
        return 0;
    }
}
