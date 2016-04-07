package com.yaraku.nlp.trees;

import edu.stanford.nlp.trees.HeadFinder;
import edu.stanford.nlp.trees.Tree;

/**
 * A head finder that always returns the rightmost daughter as head.
 * For Japanese testing purposes.
 *
 * @author Mike Tian-Jian Jiang
 */
public class RightHeadFinder implements HeadFinder {

    public Tree determineHead(Tree t) {
        if (t.isLeaf()) {
            return null;
        } else {
            return t.children()[t.children().length - 1];
        }
    }

    public Tree determineHead(Tree t, Tree parent) {
        return determineHead(t);
    }

    private static final long serialVersionUID = 1L;
}

