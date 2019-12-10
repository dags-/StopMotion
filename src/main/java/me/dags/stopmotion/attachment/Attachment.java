package me.dags.stopmotion.attachment;

public interface Attachment {

    boolean apply();

    boolean remove();

    void lock(boolean lock);
}
