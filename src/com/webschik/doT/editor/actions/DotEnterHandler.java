package com.webschik.doT.editor.actions;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.webschik.doT.psi.DotPsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for custom plugin actions when {@code Enter} is typed by the user
 */
public class DotEnterHandler extends EnterHandlerDelegateAdapter {

    public Result preprocessEnter(@NotNull final PsiFile file, @NotNull final Editor editor, @NotNull final Ref<Integer> caretOffset, @NotNull final Ref<Integer> caretAdvance,
                                  @NotNull final DataContext dataContext, final EditorActionHandler originalHandler) {

        if (file instanceof DotPsiFile && isBetweenDotTags(editor, file, caretOffset.get())) {
            originalHandler.execute(editor, dataContext);
            return Result.Default;
        }
        return Result.Continue;
    }

    /**
     * Checks to see if {@code Enter} has been typed while the caret is between an open and close tag pair.
     * @return true if between open and close tags, false otherwise
     */
    private static boolean isBetweenDotTags(Editor editor, PsiFile file, int offset) {
        if (offset == 0) return false;
        CharSequence chars = editor.getDocument().getCharsSequence();
        if (chars.charAt(offset - 1) != '}') return false;

        EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
        HighlighterIterator iterator = highlighter.createIterator(offset - 1);

        PsiElement element = file.findElementAt(iterator.getStart());

        if (element == null) {
            return false;
        }

        iterator.advance();

        if (iterator.atEnd()) {
            // no more tokens, so certainly no close tag
            return false;
        }

        element = file.findElementAt(iterator.getStart());

        // if we got this far, we're between open and close tags iff this is a close tag
        return element != null;
    }
}