package animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
    private final TranslateTransition tTransition;

    public Shake(Node node) {
        tTransition = new TranslateTransition(Duration.millis(70), node);
        tTransition.setFromX(0f);
        tTransition.setByX(7f);
        tTransition.setCycleCount(4);
        tTransition.setAutoReverse(true);
    }

    public void playAnimation() {
        tTransition.playFromStart();
    }

}
