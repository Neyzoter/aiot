package cn.neyzoter.aiot.dal.dao.vehicle;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Vehicle pojo to InfluxDB
 * @author Neyzoter Song
 * @date 2019/9/15
 */
public class Vehicle2InfluxDb {
    private RestTemplate restTemplate;
    /**
     * Organization
     */
    private String org;
    /**
     * InfluxDB bucket
     */
    private String bucket;
    /**
     * precision
     */
    private String precision;

    /**
     * token
     */
    private String token;
    /**
     * Measurement
     */
    private String measurement;
    /**
     * tag set
     */
    private String tags;
    /**
     * field set
     */
    private String fields;

    /**
     * timestamp
     */
    private String timestamp;

    /**
     * Vehicle2InfluxDb constructor
     *
     * @param org org
     * @param bucket bucket
     * @param precision precision
     * @param token token
     */
    public Vehicle2InfluxDb(String org, String bucket, String precision, String token) {
        this.restTemplate = new RestTemplate();
        this.org = org;
        this.bucket = bucket;
        this.precision = precision;
        this.token = token;
    }

    /**
     * set org
     *
     * @param org org
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * get org
     *
     * @return String
     */
    public String getOrg() {
        return this.org;
    }

    /**
     * set bucket
     *
     * @param bucket bucket
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * get bucket
     *
     * @return String
     */
    public String getBucket() {
        return this.bucket;
    }

    /**
     * set precision
     *
     * @param precision precision
     */
    public void setPrecision(String precision) {
        this.precision = precision;
    }

    /**
     * get precision
     *
     * @return String
     */
    public String getPrecision() {
        return this.precision;
    }

    /**
     * set token
     *
     * @param token token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * get token
     *
     * @return
     */
    public String getToken() {
        return this.token;
    }

    /**
     * set measurement
     *
     * @param measurement measurement
     */
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    /**
     * get measurement
     *
     * @return String
     */
    public String getMeasurement() {
        return this.measurement;
    }

    /**
     * set tag set
     *
     * @param tags {@link String}
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * set tags
     *
     * @param tags {@link Map<String,String>}
     */
    public void setTags(Map<String, String> tags) {
        this.tags = "";

        for (String tagKey : tags.keySet()) {
            this.tags += tagKey + "=" + tags.get(tagKey) + ",";
        }
        // remove last ","
        this.tags = this.tags.substring(0, this.tags.length() - 1);
    }

    /**
     * get tag set
     *
     * @return String
     */
    public String getTags() {
        return this.tags;
    }

    /**
     * set field set
     *
     * @param fields fields
     */
    public void setFields(String fields) {
        this.fields = fields;
    }

    /**
     * set field set
     *
     * @param fields fields
     */
    public void setFields(Map<String, ?> fields) {
        this.fields = "";
        for (String tagKey : fields.keySet()) {
            this.fields += tagKey + "=" + fields.get(tagKey) + ",";
        }
        // remove last ","
        this.fields = this.fields.substring(0, this.fields.length() - 1);

    }

    /**
     * get field set
     *
     * @return String
     */
    public String getFields() {
        return this.fields;
    }

    /**
     * set timestammp
     *
     * @param timestamp timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * get timestamp
     *
     * @return String
     */
    public String getTimestamp() {
        return this.timestamp;
    }

    /**
     * get restTemplate
     *
     * @return restTemplate {@link RestTemplate}
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * set restTemplate
     *
     * @param restTemplate {@link RestTemplate}
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * clear body's data
     */
    public void clearBody() {
        this.measurement = "";
        this.tags = "";
        this.fields = "";
        this.timestamp = "";
    }

    /**
     * post data to InfluxDB
     *
     * @param measurement measurement
     * @param tags tags
     * @param fields fields
     * @param timestamp timestamp
     * @return ResponseEntity<String>
     */
    public ResponseEntity<String> postOnePoint2InfluxDb(String measurement, Map<String, String> tags, Map<String, ?> fields, String timestamp) {
        String url = String.format("http://localhost:9999/api/v2/write?org=%s&bucket=%s&precision=%s", org, bucket, precision);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Token " + token);

        this.setMeasurement(measurement);
        this.setTags(tags);
        this.setFields(fields);
        setTimestamp(timestamp);
        String lineProtoBody = this.getMeasurement() + "," + this.getTags() + " " + this.fields + this.timestamp;
        HttpEntity<String> request = new HttpEntity<>(lineProtoBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response;
    }

}