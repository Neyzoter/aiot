package cn.neyzoter.aiot.common.util;

/**
 * path utils
 * @author Neyzoter Song
 * @date 20203-11
 */
public class PathUtil {
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    /**
     * trans path end with (back)slash, if no (back)slash in path then slash will not add
     * @param path path
     * @return path
     */
    public static String getPathEndWithSlash (String path) throws Exception{
        // below is determining slash or back slash
        boolean hasSlash = path.contains(SLASH);
        boolean hasBackslash = path.contains(BACKSLASH);
        String slash;
        if ( hasSlash && hasBackslash) {
            throw new Exception(String.format("path illegal : path has %s(slash) and %s(backslash)",SLASH, BACKSLASH));
        } else if (hasSlash){
            slash = SLASH;
        } else if (hasBackslash) {
            slash = BACKSLASH;
        } else {
            slash = "";
        }
        // trim the string
        path = path.trim();
        if (!path.endsWith(slash)) {
            // add slash
            path += slash;
        }
        return path;
    }
}
