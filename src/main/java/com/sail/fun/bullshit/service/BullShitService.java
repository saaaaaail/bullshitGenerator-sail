package com.sail.fun.bullshit.service;

import com.alibaba.fastjson.JSONObject;
import com.sail.fun.bullshit.utils.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BullShitService {

    private List<String> famousWords;

    private List<String> boshWords;

    private List<String> afterWords;

    private List<String> beforeWords;

    private void initJson() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static"+ File.separator +"data.json");
        JSONObject dataObj = JSONUtil.readToJSON(classPathResource.getFile());
        famousWords = dataObj.getJSONArray("famous").stream().map(Object::toString).collect(Collectors.toList());
        boshWords = dataObj.getJSONArray("bosh").stream().map(Object::toString).collect(Collectors.toList());
        afterWords = dataObj.getJSONArray("after").stream().map(Object::toString).collect(Collectors.toList());
        beforeWords = dataObj.getJSONArray("before").stream().map(Object::toString).collect(Collectors.toList());
    }



    public String generator(String subject){
        try {
            initJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //名言
        Iterator<String> famousIt = shuffleAndGenerateIt(famousWords);
        //废话
        Iterator<String> boshIt = shuffleAndGenerateIt(boshWords);


        //文章正文
        StringBuilder body = new StringBuilder();
        //段落暂存
        StringBuilder para = null;
        while (body.length()<=6000){
            if (para == null){
                para = new StringBuilder();
            }
            Integer swit = (int)(Math.random()*100);
            if (swit<5&&para.length()>=200){
                String paragraph = anotherParagraph(para.toString());
                if (StringUtils.isNotEmpty(paragraph)){
                    body.append(paragraph);
                    para = null;
                }
            }else if (swit<20){
                //下一句名言
                String nextFamous = nextFamousWord(famousIt);
                para.append(nextFamous);
            }else {
                String nextBoshs = nextBoshWord(boshIt,subject);
                para.append(nextBoshs);
            }
        }

        return body.toString();

    }

    private String anotherParagraph(String para){
        para = para.trim();
        char end = para.charAt(para.length() - 1);
        if (end=='。'){
            para = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + para+"\r\n";
            return para;
        }
        return null;
    }

    private String nextFamousWord(Iterator<String> iterator){
        String next = null;
        if (iterator.hasNext()){
            next = iterator.next();
        }else {
            iterator = shuffleAndGenerateIt(famousWords);
            next = iterator.next();
        }
        next = StringUtils.replace(next, "a", beforeWords.get((int) (Math.random() * (beforeWords.size() - 1))));
        next = StringUtils.replace(next, "b", afterWords.get((int) (Math.random() * (beforeWords.size() - 1))));
        next = StringUtils.replace(next,".","。");

        return next;
    }

    private String nextBoshWord(Iterator<String> iterator,String subject){
        String next = null;
        if (iterator.hasNext()){
            next = iterator.next();
        }else {
            iterator = shuffleAndGenerateIt(boshWords);
            next = iterator.next();
        }
        next = StringUtils.replace(next,"x",subject);
        next = StringUtils.replace(next,".","。");
        return next;
    }

    private Iterator<String> shuffleAndGenerateIt(List<String> list){
        shuffle(list);
        return list.iterator();
    }

    private void shuffle(List<String> list){
        for (int i=0;i<list.size();i++){
            int j = (int)(Math.random()*(list.size()-1));
            Collections.swap(list,i,j);
        }
    }




}
