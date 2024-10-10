package edu.qut.cab302.wehab.util;
import javafx.scene.text.Text;

/**
 * A custom text class that supports wrapping.
 * Extends the JavaFX Text class to provide automatic text wrapping.
 */
public class WrappingText extends Text {
    /**
     * Constructs a WrappingText object with the specified text and wrapping width.
     *
     * @param text the text content to display
     * @param wrappingWidth the width at which the text should wrap
     */
    public WrappingText(String text, double wrappingWidth) {
        super(text);
        this.setWrappingWidth(wrappingWidth);
    }
}
