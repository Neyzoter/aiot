package cn.neyzoter.aiot.fddp.biz.service.tensorflow;


import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RtDataBound Alive check
 * @author Charles Song
 * @date 2020-05-20
 */
@Setter
@Getter
@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.tensorflow,cn.neyzoter.aiot.fddp.biz.service.properties")
@Component
public class RtDataBoundAliveChecker implements Runnable{
    public static final Logger logger = LoggerFactory.getLogger(RtDataBoundAliveChecker.class);
    private RtDataBoundTable rtDataBoundTable;
    private PropertiesManager propertiesUtil;
    @Autowired
    public RtDataBoundAliveChecker (RtDataBoundTable rtDataBoundTable, PropertiesManager propertiesUtil) {
        this.rtDataBoundTable = rtDataBoundTable;
        this.propertiesUtil = propertiesUtil;
    }
    @Override
    public void run () {
        // check all manager is alive, if not, rm it
        List<String> rmList = rtDataBoundTable.aliveIncCheck();
        for (String vtype : rmList) {
            logger.info(String.format("  %s ModelManager has been timeout", vtype));
        }
    }
}
