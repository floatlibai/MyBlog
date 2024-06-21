package com.dev.myblog.vo;

public class BlogQuery {

    private String title;
    private Long typeId;
    private boolean recommendable;

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommendable() {
        return recommendable;
    }

    public void setRecommendable(boolean recommendable) {
        this.recommendable = recommendable;
    }
}
