package com.webschik.doT.psi.impl;

import com.intellij.lang.ASTNode;
import com.webschik.doT.psi.DotPartialName;
import org.jetbrains.annotations.NotNull;

public class DotPartialNameImpl extends DotPsiElementImpl implements DotPartialName {
    public DotPartialNameImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public String getName() {
        return getText();
    }
}
