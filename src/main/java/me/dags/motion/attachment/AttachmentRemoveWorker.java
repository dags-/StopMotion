package me.dags.motion.attachment;

import me.dags.motion.worker.Worker;

import java.util.List;

public class AttachmentRemoveWorker implements Worker {

    private final List<Attachment> attachments;

    private boolean hasWork = true;

    public AttachmentRemoveWorker(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean hasWork(long now) {
        return hasWork;
    }

    @Override
    public void work(long now) {
        hasWork = false;
        for (Attachment attachment : attachments) {
            attachment.remove();
            attachment.lock(true);
        }
    }
}
