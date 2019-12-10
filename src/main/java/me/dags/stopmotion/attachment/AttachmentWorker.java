package me.dags.stopmotion.attachment;

import me.dags.stopmotion.worker.Worker;

import java.util.List;
import java.util.function.Consumer;

public class AttachmentWorker implements Worker {

    private final List<Attachment> attachments;
    private final Consumer<Attachment> action;

    private boolean hasWork = true;

    private AttachmentWorker(List<Attachment> attachments, Consumer<Attachment> action) {
        this.attachments = attachments;
        this.action = action;
    }

    @Override
    public boolean hasWork(long now) {
        return hasWork;
    }

    @Override
    public void work(long now) {
        hasWork = false;
        attachments.forEach(action);
    }

    public static AttachmentWorker apply(List<Attachment> attachments) {
        return new AttachmentWorker(attachments, attachment -> {
            attachment.lock(false);
            attachment.apply();
        });
    }

    public static AttachmentWorker remove(List<Attachment> attachments) {
        return new AttachmentWorker(attachments, attachment -> {
            attachment.remove();
            attachment.lock(true);
        });
    }
}
