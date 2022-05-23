package spellcheck;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private Character info;
    private Boolean isValidWord;
    private Map<Character, Node> children;

    public Node(Character info){
        this.info = info;
        this.isValidWord = false;
        this.children = new HashMap<>();
    }
    public void addChild(Node n){
        this.children.putIfAbsent(n.info, n);
    }
    public void addWord(String word){
        try {
            if(word.length() < 1) return;

            char firstChar = word.charAt(0);
            Node matchingChild = this.children.get(firstChar);
            if(matchingChild == null){
                matchingChild = new Node(firstChar);
                children.put(firstChar, matchingChild);
            }

            if(word.length() == 1){
                matchingChild.isValidWord = true;
            } else if(word.length() > 1){
                matchingChild.addWord(word.substring(1));
            }
            // if word.length() < 1, do nothing
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public Boolean checkWord(String word){
        word = word.trim();
        if(word.isEmpty()) return true;

        boolean isEnded = false;
        boolean isWordExists = false;
        Node current = this;

        while(!isEnded){
            if(current == null){
                isEnded = true;
                isWordExists = false;
            } else if(word.length() == 0){
                isEnded = true;
                isWordExists = current.isValidWord;
            } else {
                current = current.children.get(word.charAt(0));
                word = word.substring(1);
            }
        }

        return isWordExists;
    }

    public Character getInfo() {
        return info;
    }
    public Boolean isValidWord(){
        return this.isValidWord;
    }

    public void printWordList(String prefix){
        if(prefix == null){
            prefix = "";
        }

        if(this.info != null){
            prefix += this.info;
        }
        if(this.isValidWord){
            System.out.println(prefix);
        }
        for(Map.Entry<Character, Node> child : this.children.entrySet()){
            child.getValue().printWordList(prefix);
        }
    }

    public void printTree(int level){
        String current = "";
        for(int i = 0; i < level; i++){
            current += "  ";
        }
        if(this.info != null){
            current += info;
        }
        if(this.isValidWord){
            current += "[]";
        }
        System.out.println(current);
        for(Map.Entry<Character, Node> child : this.children.entrySet()){
            child.getValue().printTree(level+1);
        }
    }

    public static void main(String[] args) {
        Node dict = new Node(null);
        dict.addWord("aku");
        dict.addWord("akun");
        dict.addWord("mantel");
        dict.addWord("kamu");
        dict.addWord("keren");
        dict.addWord("mantap");
        dict.printWordList(null);
        dict.printTree(0);

        System.out.println("Ada kata aku: " + dict.checkWord("aku"));
        System.out.println("Ada kata akun: " + dict.checkWord("akun"));
        System.out.println("Ada kata ak: " + dict.checkWord("ak"));
        System.out.println("Ada kata hebat: " + dict.checkWord("hebat"));
    }
}
