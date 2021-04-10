
import java.util.LinkedList;
import java.util.List;

public class Node {

    public String value;
    public Node parent;
    public List<Node> children;
    private List<Node> copyNodes;//для поиска

    public List<Node> getCopyNodes() {
        return copyNodes;
    }

    public void setCopyNodes(List<Node> nodes) {
        this.copyNodes = nodes;
    }

    public Node(String value) {
        this.value = value;
        this.children = new LinkedList<>();
        this.copyNodes = new LinkedList<>();
        this.copyNodes.add(this);
    }

    public Node() {
    }

    private void saveNode(Node node) { //сохраняем в поисковом листе
        copyNodes.add(node);
        if (this.parent != null) this.parent.saveNode(node);
    }

    public Node addNew(String newValue) {
        //находим parentNode - цепляем к нему, нет - к корню
        //parentNode находим по строке в findNote
        Node childNode = new Node(newValue);
        childNode.parent = this;
        this.children.add(childNode);
        this.saveNode(childNode);
        return childNode;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    public Node findNode(String data) {
        for (Node element : this.copyNodes) {
            if (element.value.equals(data)) return element;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.value != null ? this.value.toString() : "[null]";
    }
}

