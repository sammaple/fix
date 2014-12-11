
package com.util;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

public class JsonUtil {

    private static final Logger logger = Logger.getLogger(JsonUtil.class);
    public static String LEAGUE = "league";
    public static String TEAMNAME = "teamName";
    public static String TYPE = "type";
    public static String TITLE = "title";
    public static String KEYWORD = "keyword";
    public static String CONTENT = "content";
    public static String COMMENT = "comment";
    public static String PREDICTION = "prediction";
    public static String REVIEWINFO = "reviewInfo";
    public static String DATE = "date";
    public static String PIC = "pic";

    public static String FROM = "from";
    public static String FACTOR = "factor";
    public static String RANK = "rank";

    public static String NEWSID = "newsid";

    public static String ISPREDIC = "isWPrediction";
    public static String ISREVIEW = "isReview";

    /**
     * 发送失败时的Http响应
     * 
     * @param response Http响应
     * @throws UnsupportedEncodingException
     */
    public static String SendJsonResponse(Boolean successful, String title)
            throws UnsupportedEncodingException {

        String str = "";

        str = "{\"successful\":\"" + successful + "\",\"title\":\"" + title + "\" }";

        return new String(str.getBytes("utf-8"), "ISO-8859-1");

    }

}
