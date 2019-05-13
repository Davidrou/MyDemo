package com.example.luxiaolin.handlerdemo.network;

import com.example.luxiaolin.handlerdemo.network.bean.Translation;
import com.example.luxiaolin.handlerdemo.network.bean.Translation2;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by luozhanwei on 19-4-28.
 */
// URL模板
//http://fy.iciba.com/ajax.php

// URL实例
  //      http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world

// 参数说明：
// a：固定值 fy
// f：原文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
// t：译文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
// w：查询内容
public interface MyNetInterface {
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    Observable<Translation> translate();


    @GET("ajax.php?a=fy&f=auto&t=auto&w=reactive%20java")
    Observable<Translation2> translate2();

}
