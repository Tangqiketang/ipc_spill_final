package com.zdhk.ipc.controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-02-04 12:44
 */
public class GetTXT2FromWeb {

    public static String getMuLu(){
        String url = "https://www.biqu50.cc/book/60823815/";
        String encoding ="GBK";
        String html = getHTMLResourceByUrl(url,encoding);
        return html;
    }





    public static void insertOne(String title,String url,String filterUrl){
        String encoding ="GBK";
        String html = getHTMLResourceByUrl(url,encoding);

        Document document = Jsoup.parse(html);
        String temp = document.html().replace("<br>", "$$$$$");
        document = Jsoup.parse(temp);

        Element element = document.getElementById("content");
        String result = element.text().replace("$$$$$", "\n");

        System.out.println(result);
        saveAsFileWriter("\n");
        saveAsFileWriter(title);
        saveAsFileWriter("\n");

        saveAsFileWriter(result);
    }

    public static void main(String[] args) {
        Document document = Jsoup.parse(getMuLu());
        Element element = document.getElementsByClass("listmain").get(0);
        Elements ulList = element.getElementsByTag("a");
        for(int i=0;i<ulList.size();i++){
            //过滤前面最新章节
            if(i>11){
                // /chapter/60823815/62693703.html
                String url = "https://www.biqu50.cc"+ulList.get(i).attr("href");
                String zhangJie = ulList.get(i).text();
                insertOne(zhangJie,url,ulList.get(i).attr("href"));
            }
        }
    }

    private static String filePath = "D:\\慕容妤姬承玄.txt";

    private static void saveAsFileWriter(String content) {
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath, true);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //获取html
    public static String getHTMLResourceByUrl(String url,String encoding){
        StringBuffer sb = new StringBuffer();
        URL urlObj =null;
        URLConnection openConnection =null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            urlObj = new URL(url);
            openConnection = urlObj.openConnection();
            isr = new InputStreamReader(openConnection.getInputStream(),encoding);
            //建立文件缓冲流
            br = new BufferedReader(isr);
            //建立临时文件
            String temp = null;
            while((temp=br.readLine())!=null){
                sb.append(temp+"\n");
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                if(isr !=null){
                    isr.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}