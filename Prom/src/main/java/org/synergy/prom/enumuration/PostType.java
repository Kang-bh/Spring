package org.synergy.prom.enumuration;

public enum PostType {
    MUSIC("MUSIC"),
    WRITE("WRITE"),
    IMAGE("IMAGE"),
    SHORTFORM("SHORTFORM"),
    ARTWORK("ARTWORK");

    private String postType;

    PostType(String postType) {
        this.postType = postType;
    }

    public String getPostType() {
        return this.postType;
    }
}
