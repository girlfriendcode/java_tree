import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {
    static Node appTree;

    public App() {
        appTree = null;
    }

    public static void show(Node tree, Boolean hasBeenPrinted) {
        if (tree == null) {
            return;
        }
        if (!hasBeenPrinted) {
            System.out.println(tree.value);
        }
        for (Node node : tree.children) {
            String offset = createOffset(node.getLevel());
            System.out.println(offset + node.value);
            show(node, true);
        }
    }

    private static String createOffset(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static void find() {
        if (appTree == null) {
            System.out.println("Tree is empty!");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Which node do you want to find? Input its value ");
            String data = scanner.nextLine();
            if (appTree.findNode(data) == null) {
                System.out.println("The node with value " + data + " wasn't found!");
            } else {
                System.out.println("The node with value " + appTree.findNode(data) + " was found!");
            }
        }

    }

    public static void add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Which node you want to add? Input its value");
        String data = scanner.nextLine();
        if (appTree == null) {
            Node newRoot = new Node(data);
            appTree = newRoot;
            System.out.println("Tree was created!");
        } else {
            if (appTree.findNode(data) == null) {
                System.out.println("And to which node you want to add? Input its value");
                String parentValue = scanner.nextLine();
                if (appTree.findNode(parentValue) == null) {
                    System.out.println("Can't find such parent, new node will be added to root");
                    appTree.addNew(data);
                } else {
                    Node parentNode = appTree.findNode(parentValue);
                    parentNode.addNew(data);
                }
                System.out.println("New node was added!");
            } else System.out.println("The node with such value is already here!");
        }
    }

    public static void saveToFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the file name");
        String fileName = scanner.nextLine();//указывать полный путь
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            writeToFile(appTree, false, fos);
            System.out.println("Tree was saved in file!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Can't save the tree to this file!");

        }
    }

    //представление дерева в виде строк и запись в файл
    public static void writeToFile(Node tree, Boolean hasBeenWritten, FileOutputStream fos) {
        try {
            if (tree == null) {
                System.out.println("There is nothing to show!");
                return;
            }
            if (!hasBeenWritten) {
                String text = tree.value + "\n";
                byte[] buffer = text.getBytes();
                fos.write(buffer, 0, buffer.length);
            }
            for (Node node : tree.children) {
                String text = createOffset(node.getLevel()) + node.value + "\n";
                byte[] buffer = text.getBytes();
                fos.write(buffer, 0, buffer.length);
                writeToFile(node, true, fos);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Can't save the tree to this file!");
        }
    }

    public static void read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the name of file");
        String fileName = scanner.nextLine();
        try  {
            readFile(fileName);
            System.out.println("Tree was loaded!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Can't load the tree from this file!");
        }
    }
    public static int countOffsets(String str){
        int count = 0;
        for(int i = 0; i < str.length(); i++) {
            if(Character.isWhitespace(str.charAt(i))) count++;
        }
        return count;
    }

    public static List<String> readFile(String name){
        List<String> textTree = new LinkedList<>();
        try{
            File  file = new File(name);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while(line!=null){
                textTree.add(line);
                line = reader.readLine();
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        for(String i :textTree){
            System.out.println(i);
        }
        return textTree;
    }

    public static void remove() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which node do you want to delete? ");
        String value = scanner.nextLine();
        if (appTree.findNode(value) == null) {
            System.out.println("This node doesn't exist!");
        } else {
            deleteNode(appTree.findNode(value));
        }
    }

    public static void deleteNode(Node node) {
        Node parent = null;
        for (Node pnode : appTree.getCopyNodes()) {
            for (Node p : pnode.children) {
                if (p.value.equals(node.value)) {
                    parent = pnode; //родитель удаляемоего узла
                }
            }
        }
        if (parent == null) {//хотим удалить корень
            Node newRoot = null;
            if (!node.children.isEmpty()) {
                newRoot = appTree.children.get(0);//получаем первого ребенка
            } else {//нет детей
                appTree = null;
                System.out.println("Tree was deleted!");
                return;
            }
            appTree = newRoot;
            appTree.getCopyNodes().remove(node);
            return;
        }
        appTree.getCopyNodes().remove(node);
        List<Node> newList = new LinkedList<Node>(node.children);
        parent.children.remove(node);
        parent.children.addAll(newList);
    }

    private static boolean switchCommand(int num) {
        switch (num) {
            case 1:
                add();
                return true;
            case 2:
                find();
                return true;
            case 3:
                show(appTree, false);
                return true;
            case 4:
                saveToFile();
                return true;
            case 5:
                read();
                return true;
            case 6:
                remove();
                return true;
            case 7:
                System.out.println("Goodbye!");
                return false;
            default:
                System.out.println("Can't resolve this command, try again");
                return true;
        }

    }

    public static void main(String[] args) {
        App app = new App();
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        while (flag) {
            System.out.println("Switch command, input number");
            System.out.println("1. Add");
            System.out.println("2. Find");
            System.out.println("3. Show");
            System.out.println("4. Save");
            System.out.println("5. Load from file");
            System.out.println("6. Delete");
            System.out.println("7. Exit");
            try {
                int switchNum = scanner.nextInt();
                flag = switchCommand(switchNum);
            }
            catch(Exception ex){
                System.out.println("Input format isn't correct");
                flag = false;
            }

        }
    }
}
