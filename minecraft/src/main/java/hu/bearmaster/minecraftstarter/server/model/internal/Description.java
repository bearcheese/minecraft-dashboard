package hu.bearmaster.minecraftstarter.server.model.internal;

public class Description {
    
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Description [text=" + text + "]";
    }

}
