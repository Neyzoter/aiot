package cn.neyzoter.aiot.iov.web.controller;

/**
 * restfull http api
 * @deprecated
 * @author Neyzoter Song
 * @date 2020-2-8
 */
public class IovRestHttpApi {
    /**
     * http api prefix
     */
    private final static String VEHICLE_REST_HTTP_API_PREFIX = "/iov/api/runtime-data";

    /**
     * the test api for vehicle
     */
    public final static String VEHICLE_TEST = VEHICLE_REST_HTTP_API_PREFIX + "/test";

    /**
     * collect json data
     */
    public final static String VEHICLE_COLLECT_JSON_DATA = VEHICLE_REST_HTTP_API_PREFIX + "/json";

}
