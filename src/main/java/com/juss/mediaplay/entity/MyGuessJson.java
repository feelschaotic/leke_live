package com.juss.mediaplay.entity;

import java.util.List;

/**
 * Created by ramo on 2016/8/6.
 */
public class MyGuessJson extends JsonBean{
    private List<MyGuess> list;

    public List<MyGuess> getList() {
        return list;
    }

    public void setList(List<MyGuess> list) {
        this.list = list;
    }
}
