package edu.qut.cab302.wehab;
import javafx.scene.text.Text;

public class WrappingText extends Text {
    public WrappingText(String text, double wrappingWidth) {
        super(text);
        this.setWrappingWidth(wrappingWidth);
    }
}
