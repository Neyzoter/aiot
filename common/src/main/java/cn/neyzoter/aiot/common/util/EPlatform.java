package cn.neyzoter.aiot.common.util;


/**
 * 平台
 * @author isea533
 */
public enum EPlatform {
    /**
     * any
     */
    Any("any"),
    /**
     * Linux
     */
    Linux("Linux"),
    /**
     * Mac Os
     */
    Mac_OS("Mac OS"),
    /**
     * Mac Os X
     */
    Mac_OS_X("Mac OS X"),
    /**
     * Windows
     */
    Windows("Windows"),
    /**
     * Os/2
     */
    OS2("OS/2"),
    /**
     * Solaris
     */
    Solaris("Solaris"),
    /**
     * SunOs
     */
    SunOS("SunOS"),
    /**
     * MPE/iX
     */
    MPEiX("MPE/iX"),
    /**
     * HP-UX
     */
    HP_UX("HP-UX"),
    /**
     * AIX
     */
    AIX("AIX"),
    /**
     * OS/390
     */
    OS390("OS/390"),
    /**
     * FREE BSD
     */
    FreeBSD("FreeBSD"),
    /**
     * Irix
     */
    Irix("Irix"),
    /**
     * Digital Unix
     */
    Digital_Unix("Digital Unix"),
    /**
     * NetWare
     */
    NetWare_411("NetWare"),
    /**
     * OSF1
     */
    OSF1("OSF1"),
    /**
     * OpenVMS
     */
    OpenVMS("OpenVMS"),
    /**
     * Others
     */
    Others("Others");

    EPlatform(String desc){
        this.description = desc;
    }

    @Override
    public String toString(){
        return description;
    }

    private String description;
}
