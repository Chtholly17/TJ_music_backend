package com.example.tj_music.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
    * @author chtholly
    * @date 2021/4/2
    * @description: result class for return to front end
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    // success for select
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    // success for insert, update, delete
    public static Result success() {
        return new Result(1, "success", null);
    }

    // fail
    public static Result fail(String msg) {
        return new Result(0, msg, null);
    }

}
