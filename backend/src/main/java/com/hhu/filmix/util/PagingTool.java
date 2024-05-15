package com.hhu.filmix.util;

public class PagingTool {
    public static int offset(int pageSize,int currentPage){
        return currentPage-1>=0 ? (currentPage-1) * pageSize : 0;
    }
    public static int totalPage(int recordCount, int pageSize){
        return recordCount/pageSize + 1;
    }
}
