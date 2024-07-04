package me.zhengjie.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PageResult<T> {

    private  List<T> content;

    private long totalElements;

    private long totalPage;

    public PageResult(List<T> content, long totalElements){
        this.content = content;
        this.totalElements = totalElements;
    }

    public PageResult(List<T> content, long totalElements, long totalPage){
        this.content = content;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
    }
}
