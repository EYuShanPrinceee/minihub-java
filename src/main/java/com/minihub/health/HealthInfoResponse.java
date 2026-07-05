package com.minihub.health;

public class HealthInfoResponse {
    private String name;
    private String type;
    private String author;

    public HealthInfoResponse(String name, String type, String author) {{
    this.name = name;
    this.type = type;
    this.author = author;}
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getAuthor() {
        return author;
    }
}
