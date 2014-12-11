package com.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dao.PmDao;
import com.dao.UserDao;
import com.entity.PackageInfoData;
import com.entity.PmEntity;
import com.entity.UserEntity;

@Controller
public class BugReporterControl {

	private static final Log logger = LogFactory.getLog(BugReporterControl.class);

	@Autowired
	PmDao pmDao;

	@Autowired
	UserDao userDao;
	
	@RequestMapping(value = "/checkself")
	@ResponseBody
	public Map<String, String> checkSelf(HttpServletRequest req,
			HttpServletResponse reponse, String version) throws IOException {
		logger.debug("checkSelf get version！" + version);
		File file = new File("tmp.txt");
		String filename = file.getAbsolutePath().replace("tmp.txt", "");
		logger.debug("checkSelf get path！" + filename);

		InputStream in = new BufferedInputStream(new FileInputStream(filename
				+ "bugreporter_version_config.properties"));
		Properties p = new Properties();
		p.load(in);
		String info = p.getProperty("version_" + version);
		if (info == null) {
			Map<String, String> value = new HashMap<String, String>();
			value.put("result", "false");
			value.put("newversion", "unkown");
			value.put("isUpdate", "false");

			return value;
		}
		String[] infoList = info.split(",");
		Map<String, String> value = new HashMap<String, String>();
		value.put("result", "true");
		value.put("newversion", infoList[0]);
		value.put("isUpdate", infoList[1]);

		return value;

	}
	
	

    @RequestMapping(value="/upPm",produces="application/json")
    @ResponseBody
    public Map<String, String> upload(HttpServletRequest req, HttpServletResponse reponse,
    		String deviceid , @RequestBody PackageInfoData[] pmList) throws UnsupportedEncodingException {

        logger.debug("start upPm deviceid ！"+deviceid);

        logger.debug(pmList.length);

        for (PackageInfoData snapInfo : pmList) {

            logger.debug(snapInfo.toString());
		}
        

		Map<String, String> value = new HashMap<String, String>();

		
        if(deviceid == null||deviceid.isEmpty()){

    		value.put("successful", "false");
    		value.put("title", "fail");
        }else{
        	
        	if(pmList.length >= 1 && pmList[0].deviceid != null && !pmList[0].deviceid.isEmpty()){

        		UserEntity user = (UserEntity) pmList[0];

                Date nowTime=new Date();
                SimpleDateFormat time=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                user.setDatetime(time.format(nowTime));
                user.setTime(String.valueOf(nowTime.getTime()));
                user.setIp(req.getRemoteAddr());
                
        		userDao.save(user);
        		
        		if(pmList.length > 1){

            	    PmEntity entity = new PmEntity();
                    ArrayList<PackageInfoData> list =  new ArrayList(Arrays.asList(pmList));
                    
                    PackageInfoData boardinfo = list.get(0);
                    list.remove(boardinfo);
                    entity.setDeviceid(user.deviceid);
                    entity.setPmList(list);
                    
                    entity.setDatetime(time.format(nowTime));
                    entity.setTime(String.valueOf(nowTime.getTime()));
                    
            		pmDao.save(entity);
        		}

        		value.put("successful", "true");
        		value.put("title", "success");
        		
        	}else{

                logger.error("unkown request!");

        		value.put("successful", "true");
        		value.put("title", "unkown request");
        	}

        }
        
        /*reponse.setContentType("text/html; charset=utf-8");
        reponse.setCharacterEncoding("utf-8");

        return new String(result.getBytes("utf-8"), "iso-8859-1");*/

		return value;
	}
	

}
