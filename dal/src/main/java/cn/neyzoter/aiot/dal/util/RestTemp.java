package cn.neyzoter.aiot.dal.util;

import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

/**
 * Rest Template has serialization ability
 * @author Neyzter Song
 * @date 2020-2-26
 */
public class RestTemp extends RestTemplate implements Serializable {
    private static final long serialVersionUID = 2858219440433992489L;
}
