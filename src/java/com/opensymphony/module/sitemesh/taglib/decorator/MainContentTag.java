/*
 * Title:        MainContentTag
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.decorator;

import com.opensymphony.module.sitemesh.taglib.AbstractTag;

public class MainContentTag extends AbstractTag {
    public final int doEndTag() {
        try {
            getPage().writeMainContent(getOut());
        }
        catch (Exception e) {
            trace(e);
        }
        return EVAL_PAGE;
    }

}