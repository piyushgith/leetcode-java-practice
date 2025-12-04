package org.java.design.pattern.structural.composite;


import java.util.ArrayList;
import java.util.List;

// Common interface for both leaf and composite objects
interface FileSystemComponent {
    void display(String indent);

    int getSize();
}

// Leaf node - cannot have children
class File implements FileSystemComponent {
    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "File: " + name + " (" + size + " KB)");
    }

    @Override
    public int getSize() {
        return size;
    }
}


// Composite node - can contain children (files and folders)
class Folder implements FileSystemComponent {
    private String name;
    private List<FileSystemComponent> children = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Folder: " + name);
        // Recursively display all children
        for (FileSystemComponent child : children) {
            child.display(indent + "  ");
        }
    }

    @Override
    public int getSize() {
        int totalSize = 0;
        // Recursively calculate size of all children
        for (FileSystemComponent child : children) {
            totalSize += child.getSize();
        }
        return totalSize;
    }
}

/**
 * Client code to demonstrate the Composite Pattern
 * In this example, we create a file system hierarchy with folders and files.
 * We can perform operations like displaying the structure and calculating total size
 * on both individual files and entire folders uniformly.
 */

public class CompositePatternDemo {
    public static void main(String[] args) {
        // Build a file system hierarchy
        Folder root = new Folder("root");

        Folder documents = new Folder("Documents");
        Folder photos = new Folder("Photos");

        File resume = new File("resume.pdf", 200);
        File letter = new File("cover_letter.doc", 150);

        File vacation = new File("vacation.jpg", 2000);
        File family = new File("family.jpg", 1800);

        // Build tree structure
        documents.add(resume);
        documents.add(letter);

        photos.add(vacation);
        photos.add(family);

        root.add(documents);
        root.add(photos);

        // Display entire structure - single operation on root
        System.out.println("=== File System Structure ===");
        root.display("");

        // Calculate total size - single operation on root
        System.out.println("\n=== Total Size ===");
        System.out.println("Total: " + root.getSize() + " KB");
    }
}
