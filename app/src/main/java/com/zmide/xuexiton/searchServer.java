package com.zmide.xuexiton;
import com.zmide.xuexiton.searchDao;


public class searchServer {

    public String searchDao(String question) throws Exception {
        searchDao searchDao = new searchDao();
        String answer = searchDao.searchDao(question);
        return answer;
    }
}
