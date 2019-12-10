package me.dags.motion.attachment;

public interface Attachment {

    boolean apply();

    boolean remove();

    void lock(boolean lock);
}
