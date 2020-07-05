package cn.neyzoter.aiot.fddp.biz.service.tensorflow;

import cn.neyzoter.aiot.common.util.PathUtil;
import cn.neyzoter.aiot.common.util.PropertiesUtil;
import cn.neyzoter.aiot.fddp.biz.service.bean.PropertiesLables;
import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Rt data bound table
 * @author Neyzoter Song
 * @date 2020-5-20
 */
@Component
public class RtDataBoundMap extends ConcurrentHashMap<String, RtDataBound> {
    private static final long serialVersionUID = -4477300658723541224L;
    public static final Logger logger = LoggerFactory.getLogger(RtDataBoundMap.class);

    @Autowired
    public RtDataBoundMap (PropertiesManager propertiesManager) {
        Map<String, String> absPaths = getAllPropAbsPath(this.getBoundPath(propertiesManager));
        Iterator<Map.Entry<String, String>> iter = absPaths.entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<String, String> entry = iter.next();
            String vtype = PathUtil.getFileNameNoEx(entry.getKey());
            String absFilePath = entry.getValue();
            PropertiesUtil boundProp = new PropertiesUtil(absFilePath);
            RtDataBound data = new RtDataBound(vtype, boundProp);
            this.put(vtype, data);
        }
    }

    /**
     * get model path
     * @param propertiesManager properties util
     * @return model path
     */
    public String getBoundPath (PropertiesManager propertiesManager) {
        String path = propertiesManager.readValue(PropertiesLables.DATA_BOUND_PATH);
        return path;
    }

    /**
     * get all properties file abs path
     * @param path abs path
     * @return abs path (key : vtype+".properties", value : abs path)
     */
    public static Map<String, String> getAllPropAbsPath(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        Map<String, String> fileNameMap = new HashMap<>();
        if (tempList != null) {
            for (File f : tempList) {
                if (f.isFile()) {
                    String absPath = f.getAbsolutePath();
                    if (absPath.endsWith(".properties")) {
                        System.out.println(absPath);
                        fileNameMap.put(f.getName(),f.getAbsolutePath());
                    }
                }
            }
        }
        return fileNameMap;
    }

}
