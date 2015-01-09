package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.BlockExtractingRule;
import com.opensymphony.module.sitemesh.html.Tag;

public class ContentBlockExtractingRule extends BlockExtractingRule {

    protected static final String MAIN_CONTENT_BLOCK_ID = "content";
    
    private final PageBuilder page;
    private final SitemeshBufferFragment.Builder mainContent;

    private String contentBlockId;
    private boolean seenOpeningTag;

    public ContentBlockExtractingRule(PageBuilder page, SitemeshBufferFragment.Builder mainContent) {
        super(false, "content");
        this.page = page;
        this.mainContent = mainContent;
    }
    
    public void process(Tag tag) {
        if (tag.getType() == Tag.OPEN) {
            contentBlockId = tag.getAttributeValue("tag", false);
        }

        boolean isMainContent = MAIN_CONTENT_BLOCK_ID.equals(contentBlockId);
        if (isMainContent) {
            seenOpeningTag = false;
            if (tag.getType() == Tag.OPEN || tag.getType() == Tag.EMPTY) {
                context.currentBuffer().setStart(tag.getPosition() + tag.getLength());
                mainContent.markStart(tag.getPosition() + tag.getLength());
            } else {
                mainContent.end(tag.getPosition());
                context.pushBuffer(SitemeshBufferFragment.builder());
            }
        } else {
            if (tag.getType() == Tag.OPEN) {
                context.currentBuffer().markStartDelete(tag.getPosition());
                context.pushBuffer(createBuffer(context.getSitemeshBuffer()).markStart(tag.getPosition() + tag.getLength()));
                seenOpeningTag = true;
            } else if (tag.getType() == Tag.CLOSE && seenOpeningTag) {
                context.currentBuffer().end(tag.getPosition());
                page.addProperty("page." + contentBlockId, getCurrentBufferContent());
                context.popBuffer();
                context.currentBuffer().endDelete(tag.getPosition() + tag.getLength());
                seenOpeningTag = false;
            } else {
                context.currentBuffer().delete(tag.getPosition(), tag.getLength());
            }
        }
    }

}
