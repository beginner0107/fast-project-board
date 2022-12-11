package com.fast.projectboard.domain.constant;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"), 
    CONTENT("내용"), 
    ID("아이디"), 
    NICKNAME("닉네임"), 
    HASHTAG("해시태그");
    
    @Getter private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
