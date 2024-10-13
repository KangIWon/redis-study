package com.sparta.redis.dto;

import com.sparta.redis.entity.Memo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoResponseDto {
    private Long id;
    private String name;
    private String title;
    private String contents;

    public MemoResponseDto(Memo memo) {
        id = memo.getId();
        name = memo.getName();
        title = memo.getTitle();
        contents = memo.getContents();
    }
}
