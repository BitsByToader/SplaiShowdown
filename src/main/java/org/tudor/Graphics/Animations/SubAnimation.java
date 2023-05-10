package org.tudor.Graphics.Animations;

public class SubAnimation<K, T> {
    public K entity;
    public T target;
    public int durationMs;
    public SubAnimation<K, T> next;

    public SubAnimation(K entity, T target, int durationMs, SubAnimation<K, T> next) {
        this.entity = entity;
        this.target = target;
        this.durationMs = durationMs;
        this.next = next;
    }

    public SubAnimation(SubAnimation<K, T> copy) {
        this.entity = copy.entity;
        this.target = copy.target;
        this.durationMs = copy.durationMs;
        this.next = copy.next;
    }
}
