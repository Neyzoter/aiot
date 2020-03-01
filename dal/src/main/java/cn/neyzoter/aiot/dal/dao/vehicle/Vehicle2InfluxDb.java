package cn.neyzoter.aiot.dal.dao.vehicle;

import cn.neyzoter.aiot.dal.util.RestTemp;
import org.springframework.http.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Vehicle pojo to InfluxDB
 * @author Neyzoter Song
 * @date 2019/9/15
 */
public class Vehicle2InfluxDb implements Serializable {
    private static final long serialVersionUID = -7688376447776899417L;

    public final static String HEADERS_AUTHORIZATION = "Authorization";
    /**
     * prefix of the token, note the last char (space)
     */
    public final static String HEADERS_AUTHORIZATION_TOKEN_PREFIX = "Token ";
    /**
     * influxdb format
     */
    public final static String INFLUXDB_V2_URL_FORMAT = "http://%s:%s/api/v2/write?org=%s&bucket=%s&precision=%s";

    /**
     * measurement,tag[,tag...] field[,field...] [timestamp]
     */
    public final static String INFLUXDB_V2_BODY_LINE_PROTO_FORMAT = "%s,%s %s %s";
    /**
     * rest template
     */
    private RestTemp restTemplate;
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
     * header
     */
    private HttpHeaders headers;
    /**
     * host ip
     */
    private String host;
    /**
     * port
     */
    private String port;

    /**
     * url : come from this.host port org bucket precision
     */
    private String url;
    /**
     * read and write lock
     */
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Vehicle2InfluxDb constructor
     *
     * @param org org
     * @param bucket bucket
     * @param precision precision
     * @param token token
     * @param host host
     * @param port post
     */
    public Vehicle2InfluxDb(String org, String bucket, String precision, String token, String host, String port) {
        this(org,bucket,precision,token,host);
        this.port = port;
        this.updateUrl();
    }

    /**
     * Vehicle2InfluxDb constructor <br/>port is 9999
     * @param org org
     * @param bucket bucket
     * @param precision precision
     * @param token token
     * @param host host
     */
    public Vehicle2InfluxDb(String org, String bucket, String precision, String token, String host) {
        this.restTemplate = new RestTemp();
        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HEADERS_AUTHORIZATION, HEADERS_AUTHORIZATION_TOKEN_PREFIX + token);
        this.org = org;
        this.bucket = bucket;
        this.precision = precision;
        this.host = host;
        this.port = "9999";
        this.updateUrl();
    }

    /**
     * set org
     *
     * @param org org
     */
    public void setOrg(String org) {
        lock.writeLock().lock();
        try {
            this.org = org;
            this.updateUrl();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
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
        lock.writeLock().lock();
        try {
            this.bucket = bucket;
            this.updateUrl();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }

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
        lock.writeLock().lock();
        try {
            this.precision = precision;
            this.updateUrl();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
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
    public void setHeaders(String token) {
        lock.writeLock().lock();
        try {
            this.headers.set(HEADERS_AUTHORIZATION, token);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * get token
     *
     * @return
     */
    public HttpHeaders getHeaders() {
        return this.headers;
    }


    /**
     * get tags
     *
     * @param tags {@link Map<String,String>}
     */
    public static String getTags(Map<String, String> tags) {
        return getFields(tags);
//        String tagsStr = "";
//        Iterator<Map.Entry<String, String>> iter = tags.entrySet().iterator();
//        for (;iter.hasNext();) {
//            Map.Entry<String, String> entry= iter.next();
//            if (iter.hasNext()) {
//                tagsStr += entry.getKey() + "=" + entry.getValue() + ",";
//            } else {
//                tagsStr += entry.getKey() + "=" + entry.getValue();
//            }
//        }
//        return tagsStr;
    }


    /**
     * get field set
     *
     * @param fields fields
     */
    public static String getFields(Map<String, String> fields) {
        String fieldsStr = "";
        Iterator<Map.Entry<String, String>> iter = fields.entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<String, String> entry= iter.next();
            if (iter.hasNext()) {
                fieldsStr += entry.getKey() + "=" + entry.getValue() + ",";
            } else {
                fieldsStr += entry.getKey() + "=" + entry.getValue();
            }
        }
        return fieldsStr;
//        for (String tagKey : fields.keySet()) {
//            fieldsStr += tagKey + "=" + fields.get(tagKey) + ",";
//        }
//        // remove last ","
//        fieldsStr = fieldsStr.substring(0, fieldsStr.length() - 1);

    }

    /**
     * get restTemplate
     *
     * @return restTemplate {@link RestTemplate}
     */
    public RestTemp getRestTemplate() {
        return restTemplate;
    }

    /**
     * set restTemplate
     *
     * @param restTemplate {@link RestTemplate}
     */
    public void setRestTemplate(RestTemp restTemplate) {
        lock.writeLock().lock();
        try {
            this.restTemplate = restTemplate;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * get host
     */
    public String getHost() {
        return host;
    }

    /**
     * set host
     * @param host host (ip)
     */
    public void setHost(String host) {
        lock.writeLock().lock();
        try {
            this.host = host;
            this.updateUrl();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * get port
     * @return port
     */
    public String getPort() {
        return port;
    }

    /**
     * set pot
     * @param port pot
     */
    public void setPort(String port) {
        lock.writeLock().lock();
        try {
            this.port = port;
            this.updateUrl();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * get url
     * @return String
     */
    public String getUrl () {
        return this.url;
    }

    /**
     * update url by this.host port org bucket precision
     * @return url
     */
    public String updateUrl () {
        lock.writeLock().lock();
        try {
            this.url = String.format(INFLUXDB_V2_URL_FORMAT, this.host, this.port, this.org, this.bucket, this.precision);
            return this.url;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
        return null;
    }
    /**
     * set url
     * @param url url
     */
    public void setUrl (String url) {
        lock.writeLock().lock();
        try {
            this.url = url;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * set url
     * @param host host
     * @param port port
     * @param org org
     * @param bucket bucket
     * @param precision precision
     */
    public void setUrl (String host, String port, String org, String bucket, String precision) {
        lock.writeLock().lock();
        try {
            this.url = String.format(INFLUXDB_V2_URL_FORMAT, host, port, org, bucket, precision);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * post one point data to InfluxDB
     *
     * @param measurement measurement
     * @param tags tags
     * @param fields fields
     * @param timestamp timestamp
     * @return ResponseEntity<String>
     */
    public ResponseEntity<String> postOnepoint2InfluxDb(String measurement, Map<String, String> tags, Map<String, String> fields, String timestamp) {
        lock.readLock().lock();
        try {
            String tagsStr = getTags(tags);
            String fieldsStr = getFields(fields);
            String lineProtoBody = String.format(INFLUXDB_V2_BODY_LINE_PROTO_FORMAT,measurement,tagsStr,fieldsStr,timestamp);
            HttpEntity<String> request = new HttpEntity<>(lineProtoBody, this.headers);
            ResponseEntity<String> response = restTemplate.postForEntity(this.url, request, String.class);
            return response;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    /**
     * post multi-points data to InfluxDB
     * @param measurements measurement array
     * @param tagses tags array
     * @param fieldses fields array
     * @param timestamp timestamp array
     * @return {@link ResponseEntity}
     * @throws Exception Length of the parameters arrays is not equal
     */
    public ResponseEntity<String> postMultipoints2InfluxDB(String[] measurements, Map<String, String>[] tagses, Map<String, String>[] fieldses, String[] timestamps) {
        lock.readLock().lock();
        try {
            int len = measurements.length;
            if (len != tagses.length || len != fieldses.length || len != timestamps.length) {
                throw new Exception("Length is not equal");
            }
            String lineProtoBody = "";
            for (int i = 0 ; i < len ; i ++) {
                String tagsStr = getTags(tagses[i]);
                String fieldsStr = getFields(fieldses[i]);
                lineProtoBody += String.format(INFLUXDB_V2_BODY_LINE_PROTO_FORMAT,measurements[i],tagsStr,fieldsStr,timestamps[i]);
                if (i != len - 1) {
                    lineProtoBody += "\n";
                }
            }
            HttpEntity<String> request = new HttpEntity<>(lineProtoBody, this.headers);
            ResponseEntity<String> response = restTemplate.postForEntity(this.url, request, String.class);
            return response;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }
    /**
     * post multi lines to influxdb<br/> line must match "$measurement,$tags $field $tiemstamp"
     * @param lines line Protocal
     * @return {@link ResponseEntity<>}
     */
    public ResponseEntity<String> postMultilines2InfluxDB(String[] lines) {
        lock.readLock().lock();
        try {
            String lineProtoBody="";
            int num = lines.length;
            for (int i = 0 ; i < num ; i++) {
                lineProtoBody += lines[i];
                // not last line , add \n
                if (i != num - 1) {
                    lineProtoBody += "\n";
                }
            }
            HttpEntity<String> request = new HttpEntity<>(lineProtoBody, this.headers);
            ResponseEntity<String> response = restTemplate.postForEntity(this.url, request, String.class);
            return response;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }
    /**
     * post multi lines to influxdb<br/> line must match "$measurement,$tags $field $tiemstamp"
     * @param lines line Protocal list
     * @return {@link ResponseEntity<>}
     */
    public ResponseEntity<String> postMultilines2InfluxDB(List<String> lines) {
        lock.readLock().lock();
        try {
            String lineProtoBody="";
            int num = lines.size();
            for (int i = 0 ; i < num ; i++) {
                lineProtoBody += lines.get(i);
                // not last line , add \n
                if (i != num - 1) {
                    lineProtoBody += "\n";
                }
            }
            HttpEntity<String> request = new HttpEntity<>(lineProtoBody, this.headers);
            ResponseEntity<String> response = restTemplate.postForEntity(this.url, request, String.class);
            return response;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }
}