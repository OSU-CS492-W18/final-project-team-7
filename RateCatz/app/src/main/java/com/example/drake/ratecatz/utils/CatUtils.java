package com.example.drake.ratecatz.utils;

/**
 * Created by aleaweeks on 3/8/18.
 */
import android.net.Uri;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.Context;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

//import okhttp3.HttpUrl;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;


public class CatUtils {

    private static final String TAG = CatUtils.class.getSimpleName();


    private static final String CAT_API_KEY_PARAM = "api_key";
    private static final String CAT_API_KEY = "MjgxMDc3";

    // Strings for gettings images api call
    private static final String CAT_API_GET_IMAGES_BASE_URL = "http://thecatapi.com/api/images/get";
    private static final String CAT_API_FORMAT_PARAM = "format";
    private static final String CAT_API_FORMAT = "xml";
    private static final String CAT_API_RESULTS_NUM_PARAM = "results_per_page";
    private static final String CAT_API_RESULTS_NUM= "2";
    private static final String CAT_API_TAG_PARAM= "category";
    private String CAT_API_TAG= ""; // no tags for default



    public static class CatPhoto implements Serializable {
        public String id;
        //public boolean favorite;
        public String url;
    }

    //adjust cat api to add list="$specified filter"
    public static String buildGetCatImagesURL() {

        return Uri.parse(CAT_API_GET_IMAGES_BASE_URL).buildUpon()
                .appendQueryParameter(CAT_API_KEY_PARAM, CAT_API_KEY)
                .appendQueryParameter(CAT_API_FORMAT_PARAM, CAT_API_FORMAT)
                .appendQueryParameter(CAT_API_RESULTS_NUM_PARAM, CAT_API_RESULTS_NUM)
                //.appendQueryParameter(CAT_API_TAG_PARAM, CAT_API_TAG)
                .build()
                .toString();
    }


    // xml nesting response > data > images > image > url, id
    // code influenced by: https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
    public static ArrayList<CatPhoto> parseCatAPIGetImageResultXML(String catResultXML) throws IOException, SAXException {
        ArrayList<CatPhoto> catPhotoResultList = new ArrayList<>();
        //CatPhoto catPhoto = new CatPhoto();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream catResultsStream = new ByteArrayInputStream(catResultXML.getBytes("UTF-8"));
            Document xml = builder.parse(catResultsStream);

            NodeList catList = xml.getElementsByTagName("image");
            for(int i=0; i <catList.getLength(); i++) {

               CatPhoto catPhoto = new CatPhoto();
               Element cat = (Element)catList.item(i);

               String url = cat.getElementsByTagName("url").item(0).getTextContent();
               String id = cat.getElementsByTagName("id").item(0).getTextContent();
               Log.d(TAG, "url: " + url);
               Log.d(TAG, "id: " + id);
               catPhoto.url = url;
               catPhoto.id = id;

               catPhotoResultList.add(catPhoto);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return catPhotoResultList;
    }

}

