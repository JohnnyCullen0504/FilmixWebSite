package com.hhu.filmix.util;

import cn.hutool.crypto.digest.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoder {
    BCryptPasswordEncoder(){
    }
    public String encode(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }
    public Boolean check(String input,String password){
        return BCrypt.checkpw(input,password);
    }


}
